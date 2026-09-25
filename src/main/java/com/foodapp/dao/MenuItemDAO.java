package com.foodapp.dao;

import com.foodapp.entity.MenuItem;
import com.foodapp.enums.FoodType;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class MenuItemDAO extends GenericDAO<MenuItem> {

    public MenuItemDAO() {
        super(MenuItem.class);
    }

    public List<MenuItem> findByRestaurant(Long restaurantId) {
        return em.createQuery("SELECT m FROM MenuItem m WHERE m.restaurant.restaurantId = :rid AND m.isAvailable = true", MenuItem.class)
                .setParameter("rid", restaurantId)
                .getResultList();
    }

    // For Veg/Non-Veg mode
    public List<MenuItem> findByRestaurantAndFoodType(Long restaurantId, FoodType foodType) {
        return em.createQuery("SELECT m FROM MenuItem m WHERE m.restaurant.restaurantId = :rid AND m.foodType = :type AND m.isAvailable = true", MenuItem.class)
                .setParameter("rid", restaurantId)
                .setParameter("type", foodType)
                .getResultList();
    }

    public List<MenuItem> findByRestaurantAndCategory(Long restaurantId, Long categoryId) {
        return em.createQuery("SELECT m FROM MenuItem m WHERE m.restaurant.restaurantId = :rid AND m.foodCategory.categoryId = :cid AND m.isAvailable = true", MenuItem.class)
                .setParameter("rid", restaurantId)
                .setParameter("cid", categoryId)
                .getResultList();
    }
}