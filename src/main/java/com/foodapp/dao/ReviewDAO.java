package com.foodapp.dao;

import com.foodapp.entity.Review;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ReviewDAO extends GenericDAO<Review> {

    public ReviewDAO() {
        super(Review.class);
    }

    public List<Review> findByRestaurantId(Long restaurantId) {
        return em.createQuery("SELECT r FROM Review r WHERE r.restaurant.restaurantId = :rid ORDER BY r.createdAt DESC", Review.class)
                .setParameter("rid", restaurantId)
                .getResultList();
    }

    public Double getAverageRating(Long restaurantId) {
        return em.createQuery("SELECT AVG(r.rating) FROM Review r WHERE r.restaurant.restaurantId = :rid", Double.class)
                .setParameter("rid", restaurantId)
                .getSingleResult();
    }
}