package com.foodapp.service;

import com.foodapp.dao.RestaurantDAO;
import com.foodapp.dao.UserDAO;
import com.foodapp.dto.FilterCriteria;
import com.foodapp.entity.Restaurant;
import com.foodapp.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class RestaurantService {

    @Inject
    private RestaurantDAO restaurantDAO;

    @Inject
    private UserDAO userDAO;

    public List<Restaurant> getAllRestaurants() {
        return restaurantDAO.findAll();
    }

    public Restaurant getById(Long id) {
        return restaurantDAO.findById(id);
    }

    /**
     * Returns the restaurant owned by the given user, or null if none exists.
     * Uses a DB-level query to avoid lazy-loading issues on detached entities.
     */
    public Restaurant getByOwner(Long ownerId) {
        return restaurantDAO.findByOwnerId(ownerId);
    }

    // Applies filters (rating, near & fast, veg-only)
    public List<Restaurant> filterRestaurants(FilterCriteria criteria) {
        List<Restaurant> results = restaurantDAO.findAll();

        if (criteria.getMinRating() != null) {
            results.removeIf(r -> r.getRating().compareTo(criteria.getMinRating()) < 0);
        }
        if (criteria.getMaxDistance() != null) {
            results.removeIf(r -> r.getDistanceKm().compareTo(criteria.getMaxDistance()) > 0);
        }
        if (criteria.getMaxDeliveryTime() != null) {
            results.removeIf(r -> r.getAvgDeliveryTime() > criteria.getMaxDeliveryTime());
        }
        if (criteria.isVegOnly()) {
            results.removeIf(r -> !r.getVegOnly());
        }
        if (criteria.getCuisine() != null && !criteria.getCuisine().isEmpty()) {
            results.removeIf(r -> !criteria.getCuisine().equalsIgnoreCase(r.getCuisineType()));
        }

        return results;
    }

    /**
     * Registers a new restaurant for the given owner.
     * Throws if the owner already has a restaurant.
     */
    @Transactional
    public Restaurant registerRestaurant(Long ownerId, Restaurant data) {
        if (getByOwner(ownerId) != null) {
            throw new IllegalStateException("You already have a registered restaurant.");
        }

        User owner = userDAO.findById(ownerId);
        if (owner == null) {
            throw new IllegalArgumentException("Owner user not found");
        }

        data.setOwner(owner);
        data.setRating(BigDecimal.ZERO);
        data.setTotalReviews(0);
        data.setIsOpen(true);

        return restaurantDAO.save(data);
    }

    /**
     * Updates an existing restaurant, verifying that the caller is the owner.
     */
    @Transactional
    public Restaurant updateRestaurant(Long restaurantId, Long ownerId, Restaurant data) {
        Restaurant r = restaurantDAO.findById(restaurantId);
        if (r == null) {
            throw new IllegalArgumentException("Restaurant not found");
        }
        if (r.getOwner() == null || !r.getOwner().getUserId().equals(ownerId)) {
            throw new IllegalStateException("You don't own this restaurant.");
        }

        r.setName(data.getName());
        r.setDescription(data.getDescription());
        r.setAddress(data.getAddress());
        r.setPhone(data.getPhone());
        r.setCuisineType(data.getCuisineType());
        r.setImageUrl(data.getImageUrl());
        r.setAvgDeliveryTime(data.getAvgDeliveryTime());
        r.setDistanceKm(data.getDistanceKm());
        r.setVegOnly(data.getVegOnly());
        r.setIsOpen(data.getIsOpen());

        return restaurantDAO.update(r);
    }
}