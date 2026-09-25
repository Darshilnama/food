package com.foodapp.servlet.user;

import com.foodapp.service.PreferenceService;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/preference/toggle-veg")
public class PreferenceServlet extends HttpServlet {

    @Inject
    private PreferenceService preferenceService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long userId = (Long) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Boolean current = (Boolean) req.getSession().getAttribute("vegMode");
        boolean newValue = current == null || !current;

        preferenceService.setVegMode(userId, newValue);
        req.getSession().setAttribute("vegMode", newValue);

        // Redirect back to referrer
        String referer = req.getHeader("Referer");
        resp.sendRedirect(referer != null ? referer : req.getContextPath() + "/restaurants");
    }
}