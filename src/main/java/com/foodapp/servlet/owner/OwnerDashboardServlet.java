package com.foodapp.servlet.owner;

import com.foodapp.entity.Restaurant;
import com.foodapp.service.MenuService;
import com.foodapp.service.RestaurantService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/owner/dashboard")
public class OwnerDashboardServlet extends HttpServlet {

    @Inject private RestaurantService restaurantService;
    @Inject private MenuService menuService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Restaurant restaurant = restaurantService.getByOwner(userId);
        req.setAttribute("restaurant", restaurant);

        if (restaurant != null) {
            req.setAttribute("menuItems", menuService.getMenuForRestaurant(restaurant.getRestaurantId()));
        }

        req.getRequestDispatcher("/WEB-INF/jsp/owner/dashboard.jsp").forward(req, resp);
    }
}