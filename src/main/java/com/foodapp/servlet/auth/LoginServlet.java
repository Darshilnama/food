package com.foodapp.servlet.auth;

import com.foodapp.entity.User;
import com.foodapp.enums.Role;
import com.foodapp.service.AuthService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Inject
    private AuthService authService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // If already logged in, redirect away
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String usernameOrEmail = req.getParameter("usernameOrEmail");
        String password = req.getParameter("password");

        User user = authService.login(usernameOrEmail, password);

        if (user == null) {
            req.setAttribute("error", "Invalid username/email or password.");
            req.setAttribute("usernameOrEmail", usernameOrEmail);
            req.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(req, resp);
            return;
        }


        // Success: create session
        HttpSession session = req.getSession(true);
        session.setAttribute("user", user);
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("role", user.getRole());
        session.setAttribute("vegMode", user.getVegMode() != null && user.getVegMode());
        session.setAttribute("cartCount", 0); // will be populated by /api/cart call
        session.setMaxInactiveInterval(30 * 60); // 30 minutes

        // Redirect based on role
        if (user.getRole() == Role.ADMIN) {
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
        } else if (user.getRole() == Role.RESTAURANT_OWNER) {
            resp.sendRedirect(req.getContextPath() + "/owner/dashboard");
        } else {
            resp.sendRedirect(req.getContextPath() + "/restaurants");
        }
    }
}