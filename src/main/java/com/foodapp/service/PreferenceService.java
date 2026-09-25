package com.foodapp.service;

import com.foodapp.dao.UserDAO;
import com.foodapp.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PreferenceService {

    @Inject
    private UserDAO userDAO;

    @Transactional
    public void setVegMode(Long userId, boolean vegMode) {
        User user = userDAO.findById(userId);
        if (user != null) {
            user.setVegMode(vegMode);
            userDAO.update(user);
        }
    }
}