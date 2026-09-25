package com.foodapp.dao;

import com.foodapp.entity.Order;
import com.foodapp.enums.OrderStatus;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class OrderDAO extends GenericDAO<Order> {

    public OrderDAO() {
        super(Order.class);
    }

    public List<Order> findByUserId(Long userId) {
        return em.createQuery(
                        "SELECT o FROM Order o WHERE o.user.userId = :uid ORDER BY o.createdAt DESC",
                        Order.class)
                .setParameter("uid", userId)
                .getResultList();
    }

    public List<Order> findByStatus(OrderStatus status) {
        return em.createQuery(
                        "SELECT o FROM Order o WHERE o.status = :status ORDER BY o.createdAt DESC",
                        Order.class)
                .setParameter("status", status)
                .getResultList();
    }

    public List<Order> findByRestaurantId(Long restaurantId) {
        return em.createQuery(
                        "SELECT o FROM Order o WHERE o.restaurant.restaurantId = :rid ORDER BY o.createdAt DESC",
                        Order.class)
                .setParameter("rid", restaurantId)
                .getResultList();
    }

    /**
     * Loads an order only if it belongs to the given user.
     */
    public Order findByIdForUser(Long orderId, Long userId) {
        List<Order> results = em.createQuery(
                        "SELECT o FROM Order o WHERE o.orderId = :oid AND o.user.userId = :uid",
                        Order.class)
                .setParameter("oid", orderId)
                .setParameter("uid", userId)
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
    /**
     * Loads an order with all lazy associations eagerly fetched (user, restaurant, address, items).
     * Use this for bill generation and any read that needs the full object graph.
     */
    public Order findByIdWithDetails(Long orderId) {
        List<Order> results = em.createQuery(
                        "SELECT DISTINCT o FROM Order o " +
                                "LEFT JOIN FETCH o.user " +
                                "LEFT JOIN FETCH o.restaurant " +
                                "LEFT JOIN FETCH o.address " +
                                "LEFT JOIN FETCH o.items " +
                                "WHERE o.orderId = :oid",
                        Order.class)
                .setParameter("oid", orderId)
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
}