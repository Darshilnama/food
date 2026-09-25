package com.foodapp.dao;

import com.foodapp.entity.Cart;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class CartDAO extends GenericDAO<Cart> {

    public CartDAO() {
        super(Cart.class);
    }

    public Cart findByUserId(Long userId) {
        List<Cart> results = em.createQuery("SELECT c FROM Cart c WHERE c.user.userId = :uid", Cart.class)
                .setParameter("uid", userId)
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
}