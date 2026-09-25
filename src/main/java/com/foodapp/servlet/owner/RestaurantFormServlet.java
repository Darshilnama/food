package com.foodapp.servlet.owner;

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

@WebServlet("/owner/restaurant")
public class RestaurantFormServlet extends HttpServlet {

    @Inject private RestaurantService restaurantService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute("userId");
        Restaurant existing = restaurantService.getByOwner(userId);
        req.setAttribute("restaurant", existing);
        req.getRequestDispatcher("/WEB-INF/jsp/owner/restaurant-form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute("userId");

        try {
            Restaurant existing = restaurantService.getByOwner(userId);

            Restaurant data = new Restaurant();
            data.setName(req.getParameter("name"));
            data.setDescription(req.getParameter("description"));
            data.setAddress(req.getParameter("address"));
            data.setPhone(req.getParameter("phone"));
            data.setCuisineType(req.getParameter("cuisineType"));
            data.setImageUrl(req.getParameter("imageUrl"));
            data.setAvgDeliveryTime(parseInt(req.getParameter("avgDeliveryTime"), 30));
            data.setDistanceKm(parseDecimal(req.getParameter("distanceKm"), "0.0"));
            data.setVegOnly("true".equals(req.getParameter("vegOnly")));
            data.setIsOpen(!("false".equals(req.getParameter("isOpen"))));

            if (existing == null) {
                restaurantService.registerRestaurant(userId, data);
            } else {
                restaurantService.updateRestaurant(existing.getRestaurantId(), userId, data);
            }

            resp.sendRedirect(req.getContextPath() + "/owner/dashboard?success=1");
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("restaurant", restaurantService.getByOwner(userId));
            req.getRequestDispatcher("/WEB-INF/jsp/owner/restaurant-form.jsp").forward(req, resp);
        }
    }

    private Integer parseInt(String s, int def) {
        try { return s == null ? def : Integer.parseInt(s); }
        catch (NumberFormatException e) { return def; }
    }

    private BigDecimal parseDecimal(String s, String def) {
        try { return s == null ? new BigDecimal(def) : new BigDecimal(s); }
        catch (NumberFormatException e) { return new BigDecimal(def); }
    }
}