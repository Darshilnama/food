package com.foodapp.servlet.owner;

import com.foodapp.entity.FoodCategory;
import com.foodapp.entity.MenuItem;
import com.foodapp.entity.Restaurant;
import com.foodapp.enums.FoodType;
import com.foodapp.service.MenuService;
import com.foodapp.service.RestaurantService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/owner/menu-item")
public class MenuItemFormServlet extends HttpServlet {

    @Inject private MenuService menuService;
    @Inject private RestaurantService restaurantService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute("userId");
        Restaurant restaurant = restaurantService.getByOwner(userId);
        if (restaurant == null) {
            resp.sendRedirect(req.getContextPath() + "/owner/dashboard");
            return;
        }

        String idParam = req.getParameter("id");
        if (idParam != null) {
            MenuItem existing = menuService.getById(Long.parseLong(idParam));
            req.setAttribute("item", existing);
        }

        req.setAttribute("restaurant", restaurant);
        req.setAttribute("categories", menuService.getAllFoodCategories());
        req.setAttribute("foodTypes", FoodType.values());
        req.getRequestDispatcher("/WEB-INF/jsp/owner/menu-item-form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute("userId");
        Restaurant restaurant = restaurantService.getByOwner(userId);
        if (restaurant == null) {
            resp.sendRedirect(req.getContextPath() + "/owner/dashboard");
            return;
        }

        try {
            MenuItem data = new MenuItem();
            data.setName(req.getParameter("name"));
            data.setDescription(req.getParameter("description"));
            data.setPrice(new BigDecimal(req.getParameter("price")));
            data.setImageUrl(req.getParameter("imageUrl"));
            data.setFoodType(FoodType.valueOf(req.getParameter("foodType")));
            data.setIsAvailable(!"false".equals(req.getParameter("isAvailable")));

            String categoryId = req.getParameter("foodCategoryId");
            if (categoryId != null && !categoryId.isBlank()) {
                FoodCategory cat = menuService.getCategoryById(Long.parseLong(categoryId));
                data.setFoodCategory(cat);
            }

            String idParam = req.getParameter("itemId");
            if (idParam != null && !idParam.isBlank()) {
                menuService.updateMenuItem(Long.parseLong(idParam), userId, data);
            } else {
                menuService.addMenuItem(restaurant.getRestaurantId(), userId, data);
            }

            resp.sendRedirect(req.getContextPath() + "/owner/dashboard?success=1");
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("restaurant", restaurant);
            req.setAttribute("categories", menuService.getAllFoodCategories());
            req.setAttribute("foodTypes", FoodType.values());
            req.getRequestDispatcher("/WEB-INF/jsp/owner/menu-item-form.jsp").forward(req, resp);
        }
    }
}