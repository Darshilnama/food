package com.foodapp.servlet.order;

import com.foodapp.dto.BillDTO;
import com.foodapp.entity.Order;
import com.foodapp.service.OrderService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import com.foodapp.dao.PaymentDAO;
import com.foodapp.entity.Payment;

@WebServlet("/bill")
public class BillServlet extends HttpServlet {

    @Inject private OrderService orderService;
    @Inject private PaymentDAO paymentDAO;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long userId = (Long) req.getSession().getAttribute("userId");
        Long orderId = parseLong(req.getParameter("orderId"));

        if (orderId == null || userId == null) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        Order order = orderService.getOrderForUser(orderId, userId);
        if (order == null) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        Payment payment = paymentDAO.findByOrderId(orderId);
        BillDTO bill = orderService.generateBill(orderId);
        req.setAttribute("bill", bill);
        req.setAttribute("order", order);
        req.setAttribute("payment", payment);
        req.getRequestDispatcher("/WEB-INF/jsp/order/bill.jsp").forward(req, resp);
    }

    private Long parseLong(String s) {
        try { return s == null ? null : Long.parseLong(s); }
        catch (NumberFormatException e) { return null; }
    }
}