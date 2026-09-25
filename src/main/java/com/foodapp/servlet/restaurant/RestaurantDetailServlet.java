package com.foodapp.servlet.restaurant;

import com.foodapp.entity.Restaurant;
import com.foodapp.service.MenuService;
import com.foodapp.service.RestaurantService;
import com.foodapp.service.RatingService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/restaurant")
public class RestaurantDetailServlet extends HttpServlet {

    @Inject private RestaurantService restaurantService;
    @Inject private MenuService menuService;
    @Inject private RatingService ratingService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendRedirect(req.getContextPath() + "/restaurants");
            return;
        }

        Long restaurantId;
        try {
            restaurantId = Long.parseLong(idParam);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/restaurants");
            return;
        }

        Restaurant restaurant = restaurantService.getById(restaurantId);
        if (restaurant == null) {
            resp.sendRedirect(req.getContextPath() + "/restaurants");
            return;
        }

        req.setAttribute("restaurant", restaurant);
        req.setAttribute("menuItems", menuService.getMenuForRestaurant(restaurantId));
        req.setAttribute("foodCategories", menuService.getAllFoodCategories());
        req.setAttribute("reviews", ratingService.getReviewsForRestaurant(restaurantId));

        req.getRequestDispatcher("/WEB-INF/jsp/restaurant/detail.jsp").forward(req, resp);
    }
}