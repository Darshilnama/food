package com.foodapp.dao;

import com.foodapp.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;   // <-- ADD THIS LINE

@ApplicationScoped
public class UserDAO extends GenericDAO<User> {

    public UserDAO() {
        super(User.class);
    }

    public User findByUsername(String username) {
        List<User> results = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    public User findByEmail(String email) {
        List<User> results = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    public boolean existsByUsernameOrEmail(String username, String email) {
        Long count = em.createQuery(
                        "SELECT COUNT(u) FROM User u WHERE u.username = :username OR u.email = :email", Long.class)
                .setParameter("username", username)
                .setParameter("email", email)
                .getSingleResult();
        return count > 0;
    }
}