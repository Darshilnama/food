package com.foodapp.servlet.restaurant;

import com.foodapp.dto.RestaurantCardDTO;
import com.foodapp.entity.Restaurant;
import com.foodapp.service.RestaurantService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/restaurants")
public class RestaurantListServlet extends HttpServlet {

    @Inject
    private RestaurantService restaurantService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Read filter params
        String cuisine   = req.getParameter("cuisine");
        String ratingStr = req.getParameter("rating");
        String distanceStr = req.getParameter("distance");
        String fastStr   = req.getParameter("fast");
        String vegOnlyStr = req.getParameter("vegOnly");
        String search    = req.getParameter("search");

        // Base list
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();

        // Apply filters in-memory
        if (search != null && !search.isBlank()) {
            String q = search.toLowerCase();
            restaurants = restaurants.stream()
                    .filter(r -> r.getName().toLowerCase().contains(q)
                            || (r.getCuisineType() != null && r.getCuisineType().toLowerCase().contains(q)))
                    .collect(Collectors.toList());
        }

        if (cuisine != null && !cuisine.isBlank()) {
            restaurants = restaurants.stream()
                    .filter(r -> cuisine.equalsIgnoreCase(r.getCuisineType()))
                    .collect(Collectors.toList());
        }

        if (ratingStr != null && !ratingStr.isBlank()) {
            BigDecimal minRating = new BigDecimal(ratingStr);
            restaurants = restaurants.stream()
                    .filter(r -> r.getRating() != null && r.getRating().compareTo(minRating) >= 0)
                    .collect(Collectors.toList());
        }

        if (distanceStr != null && !distanceStr.isBlank()) {
            BigDecimal maxDist = new BigDecimal(distanceStr);
            restaurants = restaurants.stream()
                    .filter(r -> r.getDistanceKm() != null && r.getDistanceKm().compareTo(maxDist) <= 0)
                    .collect(Collectors.toList());
        }

        if ("true".equals(fastStr)) {
            restaurants = restaurants.stream()
                    .filter(r -> r.getAvgDeliveryTime() != null && r.getAvgDeliveryTime() <= 25)
                    .sorted((a, b) -> Integer.compare(a.getAvgDeliveryTime(), b.getAvgDeliveryTime()))
                    .collect(Collectors.toList());
        }

        boolean vegOnly = "true".equals(vegOnlyStr);
        if (vegOnly) {
            restaurants = restaurants.stream()
                    .filter(r -> r.getVegOnly() != null && r.getVegOnly())
                    .collect(Collectors.toList());
        }

        // Convert to view DTOs
        List<RestaurantCardDTO> cards = restaurants.stream()
                .map(RestaurantCardDTO::new)
                .collect(Collectors.toList());

        // Send to JSP
        req.setAttribute("restaurants", cards);
        req.setAttribute("cuisine", cuisine);
        req.setAttribute("rating", ratingStr);
        req.setAttribute("distance", distanceStr);
        req.setAttribute("fast", fastStr);
        req.setAttribute("vegOnly", vegOnly);
        req.setAttribute("search", search);

        req.getRequestDispatcher("/WEB-INF/jsp/restaurant/list.jsp").forward(req, resp);
    }
}