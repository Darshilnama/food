package com.foodapp.service;

import com.foodapp.dao.FoodCategoryDAO;
import com.foodapp.dao.MenuItemDAO;
import com.foodapp.dao.RestaurantDAO;
import com.foodapp.entity.FoodCategory;
import com.foodapp.entity.MenuItem;
import com.foodapp.entity.Restaurant;
import com.foodapp.enums.FoodType;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class MenuService {

    @Inject
    private MenuItemDAO menuItemDAO;

    @Inject
    private FoodCategoryDAO foodCategoryDAO;

    @Inject
    private RestaurantDAO restaurantDAO;


    // =========================================================
    // CUSTOMER METHODS
    // =========================================================

    /**
     * Get complete menu for a restaurant.
     */
    public List<MenuItem> getMenuForRestaurant(Long restaurantId) {
        return menuItemDAO.findByRestaurant(restaurantId);
    }


    /**
     * Get only vegetarian menu items.
     */
    public List<MenuItem> getVegMenuForRestaurant(Long restaurantId) {
        return menuItemDAO.findByRestaurantAndFoodType(
                restaurantId,
                FoodType.VEG
        );
    }


    /**
     * Get menu items by category.
     */
    public List<MenuItem> getMenuByCategory(
            Long restaurantId,
            Long categoryId
    ) {
        return menuItemDAO.findByRestaurantAndCategory(
                restaurantId,
                categoryId
        );
    }


    /**
     * Get all food categories.
     */
    public List<FoodCategory> getAllFoodCategories() {
        return foodCategoryDAO.findAll();
    }


    /**
     * Get menu item by ID.
     */
    public MenuItem getById(Long itemId) {
        return menuItemDAO.findById(itemId);
    }


    /**
     * Get food category by ID.
     */
    public FoodCategory getCategoryById(Long id) {
        return foodCategoryDAO.findById(id);
    }


    // =========================================================
    // RESTAURANT OWNER METHODS
    // =========================================================

    /**
     * Add a new menu item.
     *
     * The owner must own the restaurant.
     */
    @Transactional
    public MenuItem addMenuItem(
            Long restaurantId,
            Long ownerId,
            MenuItem data
    ) {

        // Find restaurant
        Restaurant restaurant = restaurantDAO.findById(restaurantId);

        // Verify ownership
        if (restaurant == null
                || restaurant.getOwner() == null
                || !restaurant.getOwner()
                .getUserId()
                .equals(ownerId)) {

            throw new IllegalStateException(
                    "You don't own this restaurant."
            );
        }

        // Associate item with restaurant
        data.setRestaurant(restaurant);

        // New items are available by default
        data.setIsAvailable(true);

        return menuItemDAO.save(data);
    }


    /**
     * Update an existing menu item.
     *
     * Only the restaurant owner can update it.
     */
    @Transactional
    public MenuItem updateMenuItem(
            Long itemId,
            Long ownerId,
            MenuItem data
    ) {

        // Find menu item
        MenuItem item = menuItemDAO.findById(itemId);

        if (item == null) {
            throw new IllegalArgumentException(
                    "Item not found"
            );
        }

        // Check restaurant
        Restaurant restaurant = item.getRestaurant();

        if (restaurant == null
                || restaurant.getOwner() == null
                || !restaurant.getOwner()
                .getUserId()
                .equals(ownerId)) {

            throw new IllegalStateException(
                    "You don't own this item."
            );
        }

        // Update fields
        item.setName(data.getName());
        item.setDescription(data.getDescription());
        item.setPrice(data.getPrice());
        item.setImageUrl(data.getImageUrl());
        item.setFoodType(data.getFoodType());
        item.setFoodCategory(data.getFoodCategory());
        item.setIsAvailable(data.getIsAvailable());

        return menuItemDAO.update(item);
    }


    /**
     * Delete a menu item.
     *
     * Only the restaurant owner can delete it.
     */
    @Transactional
    public void deleteMenuItem(
            Long itemId,
            Long ownerId
    ) {

        // Find menu item
        MenuItem item = menuItemDAO.findById(itemId);

        // If item doesn't exist, nothing to delete
        if (item == null) {
            return;
        }

        // Check restaurant ownership
        Restaurant restaurant = item.getRestaurant();

        if (restaurant == null
                || restaurant.getOwner() == null
                || !restaurant.getOwner()
                .getUserId()
                .equals(ownerId)) {

            throw new IllegalStateException(
                    "You don't own this item."
            );
        }

        menuItemDAO.delete(item);
    }
}