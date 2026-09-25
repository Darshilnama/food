package com.foodapp.service;

import com.foodapp.dao.UserDAO;
import com.foodapp.entity.User;
import com.foodapp.util.PasswordUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AuthService {

    @Inject
    private UserDAO userDAO;

    @Transactional
    public User register(String username, String email, String password, String phone, String role) {
        if (userDAO.existsByUsernameOrEmail(username, email)) {
            throw new IllegalArgumentException("Username or email already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(PasswordUtil.hashPassword(password));
        user.setPhone(phone);
        user.setRole("RESTAURANT_OWNER".equals(role)
                ? com.foodapp.enums.Role.RESTAURANT_OWNER
                : com.foodapp.enums.Role.CUSTOMER);
        return userDAO.save(user);
    }

    public User login(String usernameOrEmail, String password) {
        User user = userDAO.findByUsername(usernameOrEmail);
        if (user == null) {
            user = userDAO.findByEmail(usernameOrEmail);
        }
        if (user == null || !PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            return null;
        }
        return user;
    }
}