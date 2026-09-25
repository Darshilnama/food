package com.foodapp.service;

import com.foodapp.dao.RestaurantDAO;
import com.foodapp.dao.ReviewDAO;
import com.foodapp.dao.UserDAO;
import com.foodapp.entity.Restaurant;
import com.foodapp.entity.Review;
import com.foodapp.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@ApplicationScoped
public class RatingService {

    @Inject private ReviewDAO reviewDAO;
    @Inject private RestaurantDAO restaurantDAO;
    @Inject private UserDAO userDAO;

    @Transactional
    public Review addReview(Long userId, Long restaurantId, int rating, String comment) {
        User user = userDAO.findById(userId);
        Restaurant restaurant = restaurantDAO.findById(restaurantId);
        if (user == null || restaurant == null) throw new IllegalArgumentException("Invalid ids");

        Review review = new Review();
        review.setUser(user);
        review.setRestaurant(restaurant);
        review.setRating(rating);
        review.setComment(comment);
        reviewDAO.save(review);

        // Recalculate restaurant average rating
        Double avg = reviewDAO.getAverageRating(restaurantId);
        if (avg != null) {
            restaurant.setRating(BigDecimal.valueOf(avg).setScale(1, RoundingMode.HALF_UP));
        }
        long total = reviewDAO.findByRestaurantId(restaurantId).size();
        restaurant.setTotalReviews((int) total);
        restaurantDAO.update(restaurant);

        return review;
    }

    public List<Review> getReviewsForRestaurant(Long restaurantId) {
        return reviewDAO.findByRestaurantId(restaurantId);
    }
}