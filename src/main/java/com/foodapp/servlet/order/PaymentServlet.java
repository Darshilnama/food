package com.foodapp.servlet.order;

import com.foodapp.entity.Order;
import com.foodapp.entity.Payment;
import com.foodapp.enums.PaymentMethod;
import com.foodapp.service.OrderService;
import com.foodapp.service.PaymentService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {

    @Inject private OrderService orderService;
    @Inject private PaymentService paymentService;

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

        req.setAttribute("order", order);
        req.setAttribute("methods", PaymentMethod.values());
        req.getRequestDispatcher("/WEB-INF/jsp/order/payment.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long userId = (Long) req.getSession().getAttribute("userId");
        Long orderId = parseLong(req.getParameter("orderId"));
        String methodStr = req.getParameter("method");

        if (orderId == null || methodStr == null || userId == null) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        Order order = orderService.getOrderForUser(orderId, userId);
        if (order == null) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        try {
            PaymentMethod method = PaymentMethod.valueOf(methodStr);
            Payment payment = paymentService.processPayment(orderId, method);
            resp.sendRedirect(req.getContextPath() + "/bill?orderId=" + orderId);
        } catch (Exception e) {
            log("payment failed", e);
            req.setAttribute("order", order);
            req.setAttribute("methods", PaymentMethod.values());
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/order/payment.jsp").forward(req, resp);
        }
    }

    private Long parseLong(String s) {
        try { return s == null ? null : Long.parseLong(s); }
        catch (NumberFormatException e) { return null; }
    }
}