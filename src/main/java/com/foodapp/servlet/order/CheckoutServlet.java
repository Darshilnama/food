package com.foodapp.servlet.order;

import com.foodapp.dto.CartDTO;
import com.foodapp.dto.CheckoutDTO;
import com.foodapp.entity.Address;
import com.foodapp.entity.Cart;
import com.foodapp.entity.Order;
import com.foodapp.service.AddressService;
import com.foodapp.service.CartService;
import com.foodapp.service.OrderService;
import com.foodapp.service.VoucherService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    @Inject private CartService cartService;
    @Inject private AddressService addressService;
    @Inject private VoucherService voucherService;
    @Inject private OrderService orderService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute("userId");
        CheckoutDTO dto = buildCheckoutDTO(userId, null, null);
        if (dto == null) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }
        req.setAttribute("checkout", dto);
        req.getRequestDispatcher("/WEB-INF/jsp/order/checkout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long userId = (Long) req.getSession().getAttribute("userId");
        String action = req.getParameter("action");

        if ("addAddress".equals(action)) {
            handleAddAddress(req, resp, userId);
        } else if ("applyVoucher".equals(action)) {
            handleApplyVoucher(req, resp, userId);
        } else if ("placeOrder".equals(action)) {
            handlePlaceOrder(req, resp, userId);
        } else {
            resp.sendRedirect(req.getContextPath() + "/checkout");
        }
    }

    // ---------------- Handlers ----------------

    private void handleAddAddress(HttpServletRequest req, HttpServletResponse resp, Long userId)
            throws ServletException, IOException {
        try {
            addressService.addAddress(
                    userId,
                    req.getParameter("label"),
                    req.getParameter("street"),
                    req.getParameter("city"),
                    req.getParameter("state"),
                    req.getParameter("zipCode"),
                    "true".equals(req.getParameter("isDefault"))
            );
            resp.sendRedirect(req.getContextPath() + "/checkout");
        } catch (Exception e) {
            log("addAddress failed", e);
            forwardCheckoutWithError(req, resp, userId, null,
                    "Failed to add address: " + rootMessage(e));
        }
    }

    private void handleApplyVoucher(HttpServletRequest req, HttpServletResponse resp, Long userId)
            throws ServletException, IOException {
        String code = req.getParameter("voucherCode");
        CheckoutDTO dto = buildCheckoutDTO(userId, code, null);
        if (dto == null) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }
        req.setAttribute("checkout", dto);
        req.getRequestDispatcher("/WEB-INF/jsp/order/checkout.jsp").forward(req, resp);
    }

    private void handlePlaceOrder(HttpServletRequest req, HttpServletResponse resp, Long userId)
            throws ServletException, IOException {

        String addressIdStr = req.getParameter("addressId");
        String voucherCode  = req.getParameter("voucherCode");

        // Guard: no address selected
        if (addressIdStr == null || addressIdStr.isBlank()) {
            forwardCheckoutWithError(req, resp, userId, voucherCode,
                    "Please select a delivery address before placing the order.");
            return;
        }

        try {
            Long addressId = Long.parseLong(addressIdStr);
            Order order = orderService.placeOrder(userId, addressId, voucherCode);

            req.getSession().setAttribute("cartCount", 0);
            resp.sendRedirect(req.getContextPath() + "/payment?orderId=" + order.getOrderId());

        } catch (Exception e) {
            // Log the REAL error so we can see it in the TomEE console
            log("placeOrder failed for user " + userId, e);

            CheckoutDTO dto = buildCheckoutDTO(userId, voucherCode, null);
            if (dto == null) {
                // Cart is now empty (order may have partially succeeded). Redirect to cart.
                req.getSession().setAttribute("flashError",
                        "Could not place order: " + rootMessage(e));
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            }
            dto.setGeneralError("Could not place order: " + rootMessage(e));
            req.setAttribute("checkout", dto);
            req.getRequestDispatcher("/WEB-INF/jsp/order/checkout.jsp").forward(req, resp);
        }
    }

    // ---------------- Helpers ----------------

    private void forwardCheckoutWithError(HttpServletRequest req, HttpServletResponse resp,
                                          Long userId, String voucherCode, String error)
            throws ServletException, IOException {
        CheckoutDTO dto = buildCheckoutDTO(userId, voucherCode, null);
        if (dto == null) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }
        dto.setGeneralError(error);
        req.setAttribute("checkout", dto);
        req.getRequestDispatcher("/WEB-INF/jsp/order/checkout.jsp").forward(req, resp);
    }

    private CheckoutDTO buildCheckoutDTO(Long userId, String voucherCode, Long selectedAddressId) {
        Cart cart = cartService.getCart(userId);
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            return null;
        }

        CheckoutDTO dto = new CheckoutDTO();
        CartDTO cartDTO = new CartDTO(cart);
        dto.setCart(cartDTO);
        dto.setSubtotal(cartDTO.getSubtotal());

        List<Address> addresses = addressService.getAddressesForUser(userId);
        dto.setAddresses(addresses);

        Long defaultId = selectedAddressId;
        if (defaultId == null) {
            defaultId = addresses.stream()
                    .filter(a -> a.getIsDefault() != null && a.getIsDefault())
                    .map(Address::getAddressId)
                    .findFirst()
                    .orElse(addresses.isEmpty() ? null : addresses.get(0).getAddressId());
        }
        dto.setSelectedAddressId(defaultId);

        dto.setVoucherCode(voucherCode);
        BigDecimal discount = BigDecimal.ZERO;
        if (voucherCode != null && !voucherCode.isBlank()) {
            discount = voucherService.calculateDiscount(voucherCode, cartDTO.getSubtotal());
            if (discount == null) discount = BigDecimal.ZERO;
            if (discount.compareTo(BigDecimal.ZERO) > 0) {
                dto.setVoucherSuccess("Voucher applied — you saved ₹" + discount);
            } else {
                dto.setVoucherError("Invalid or ineligible voucher code.");
            }
        }
        dto.setDiscount(discount);

        BigDecimal afterDiscount = cartDTO.getSubtotal().subtract(discount);
        BigDecimal gst = afterDiscount.multiply(BigDecimal.valueOf(0.05))
                .setScale(2, RoundingMode.HALF_UP);
        dto.setGst(gst);
        dto.setGrandTotal(afterDiscount.add(gst).add(dto.getDeliveryFee()));

        return dto;
    }

    private String rootMessage(Throwable t) {
        Throwable root = t;
        while (root.getCause() != null) root = root.getCause();
        String msg = root.getMessage();
        return msg != null ? msg : root.getClass().getSimpleName();
    }
}