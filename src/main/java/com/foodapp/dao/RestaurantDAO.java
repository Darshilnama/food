package com.foodapp.dao;

import com.foodapp.entity.Restaurant;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class RestaurantDAO extends GenericDAO<Restaurant> {

    public RestaurantDAO() {
        super(Restaurant.class);
    }

    public List<Restaurant> findByCuisine(String cuisine) {
        return em.createQuery("SELECT r FROM Restaurant r WHERE r.cuisineType = :cuisine", Restaurant.class)
                .setParameter("cuisine", cuisine)
                .getResultList();
    }

    // For "Rating" filter
    public List<Restaurant> findByMinRating(BigDecimal minRating) {
        return em.createQuery("SELECT r FROM Restaurant r WHERE r.rating >= :rating ORDER BY r.rating DESC", Restaurant.class)
                .setParameter("rating", minRating)
                .getResultList();
    }

    // For "Near" filter
    public List<Restaurant> findByMaxDistance(BigDecimal maxDistance) {
        return em.createQuery("SELECT r FROM Restaurant r WHERE r.distanceKm <= :distance ORDER BY r.distanceKm ASC", Restaurant.class)
                .setParameter("distance", maxDistance)
                .getResultList();
    }

    // For "Fast" filter
    public List<Restaurant> findByMaxDeliveryTime(Integer maxTime) {
        return em.createQuery("SELECT r FROM Restaurant r WHERE r.avgDeliveryTime <= :time ORDER BY r.avgDeliveryTime ASC", Restaurant.class)
                .setParameter("time", maxTime)
                .getResultList();
    }

    // For veg-only mode
    public List<Restaurant> findByVegOnly(boolean vegOnly) {
        return em.createQuery("SELECT r FROM Restaurant r WHERE r.vegOnly = :vegOnly", Restaurant.class)
                .setParameter("vegOnly", vegOnly)
                .getResultList();
    }

    /**
     * Find a restaurant by its owner's user ID.
     * This query runs inside the DB, so it does not suffer from
     * lazy-loading issues (unlike iterating over findAll() and filtering in Java).
     */
    public Restaurant findByOwnerId(Long ownerId) {
        List<Restaurant> results = em.createQuery(
                        "SELECT r FROM Restaurant r WHERE r.owner.userId = :ownerId",
                        Restaurant.class)
                .setParameter("ownerId", ownerId)
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
}