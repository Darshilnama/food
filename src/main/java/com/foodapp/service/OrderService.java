package com.foodapp.service;

import com.foodapp.dao.CartDAO;
import com.foodapp.dao.OrderDAO;
import com.foodapp.entity.*;
import com.foodapp.enums.OrderStatus;
import com.foodapp.dto.BillDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class OrderService {

    @Inject private OrderDAO orderDAO;
    @Inject private CartDAO cartDAO;
    @Inject private VoucherService voucherService;
    @Inject private AddressService addressService;
    @Inject private com.foodapp.dao.AddressDAO addressDAO;


    @jakarta.transaction.Transactional
    public Order placeOrder(Long userId, Long addressId, String voucherCode) {

        // 1. Load cart inside the transaction
        Cart cart = cartDAO.findByUserId(userId);
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new IllegalStateException("Your cart is empty.");
        }

        // 2. Load address INSIDE the transaction (managed entity, avoids detached issues)
        Address address = addressDAO.findById(addressId);
        if (address == null) {
            throw new IllegalArgumentException("Address not found.");
        }
        if (address.getUser() == null
                || !address.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("Address does not belong to you.");
        }

        // 3. Compute subtotal
        BigDecimal subtotal = cart.getItems().stream()
                .map(i -> i.getMenuItem().getPrice()
                        .multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4. Apply voucher
        BigDecimal discount = voucherService.calculateDiscount(voucherCode, subtotal);
        if (discount == null) discount = BigDecimal.ZERO;
        BigDecimal finalAmount = subtotal.subtract(discount);

        // 5. Create the order
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setRestaurant(cart.getRestaurant());
        order.setAddress(address);
        order.setTotalAmount(subtotal);
        order.setDiscountAmount(discount);
        order.setFinalAmount(finalAmount);
        order.setVoucherCode(voucherCode);
        order.setStatus(OrderStatus.PENDING);

        // 6. Copy cart items -> order items
        for (CartItem ci : cart.getItems()) {
            OrderItem oi = new OrderItem();
            oi.setItemId(ci.getMenuItem().getItemId());
            oi.setItemName(ci.getMenuItem().getName());
            oi.setQuantity(ci.getQuantity());
            oi.setUnitPrice(ci.getMenuItem().getPrice());
            order.addItem(oi);
        }

        Order saved = orderDAO.save(order);

        // 7. Clear cart
        cart.getItems().clear();
        cart.setRestaurant(null);
        cartDAO.update(cart);

        return saved;
    }

    public BillDTO generateBill(Long orderId) {
        Order order = orderDAO.findByIdWithDetails(orderId);
        if (order == null) throw new IllegalArgumentException("Order not found");
        return new BillDTO(order);
    }

    public List<Order> getOrderHistory(Long userId) {
        return orderDAO.findByUserId(userId);
    }

    public Order getOrder(Long orderId) {
        return orderDAO.findById(orderId);
    }

    @Transactional
    public void updateStatus(Long orderId, OrderStatus status) {
        Order order = orderDAO.findById(orderId);
        if (order != null) {
            order.setStatus(status);
            orderDAO.update(order);
        }
    }
    /**
     * Loads an order only if it belongs to the given user (safe ownership check).
     */
    public Order getOrderForUser(Long orderId, Long userId) {
        return orderDAO.findByIdForUser(orderId, userId);
    }
}