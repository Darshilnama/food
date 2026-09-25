package com.foodapp.servlet.order;

import com.foodapp.entity.Order;
import com.foodapp.service.OrderService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/orders")
public class OrderHistoryServlet extends HttpServlet {

    @Inject private OrderService orderService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute("userId");
        List<Order> orders = orderService.getOrderHistory(userId);
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/WEB-INF/jsp/order/history.jsp").forward(req, resp);
    }
}