package com.foodapp.servlet.auth;

import com.foodapp.entity.User;
import com.foodapp.service.AuthService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Inject
    private AuthService authService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String phone = req.getParameter("phone");

        // Get role from registration form
        String role = req.getParameter("role");

        // Default role = CUSTOMER
        if (role == null || role.isBlank()) {
            role = "CUSTOMER";
        }

        // Basic validation
        if (username == null || username.isBlank()
                || email == null || email.isBlank()
                || password == null || password.isBlank()) {

            forwardWithError(
                    req,
                    resp,
                    "All fields are required.",
                    username,
                    email,
                    phone
            );
            return;
        }

        // Confirm password
        if (!password.equals(confirmPassword)) {

            forwardWithError(
                    req,
                    resp,
                    "Passwords do not match.",
                    username,
                    email,
                    phone
            );
            return;
        }

        // Password length
        if (password.length() < 6) {

            forwardWithError(
                    req,
                    resp,
                    "Password must be at least 6 characters.",
                    username,
                    email,
                    phone
            );
            return;
        }

        try {

            // Register user with selected role
            User user = authService.register(
                    username,
                    email,
                    password,
                    phone,
                    role
            );

            // Auto-login after registration
            HttpSession session = req.getSession(true);

            session.setAttribute("user", user);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());
            session.setAttribute("vegMode", false);
            session.setAttribute("cartCount", 0);

            // Redirect based on role
            if (user.getRole() == com.foodapp.enums.Role.RESTAURANT_OWNER) {

                resp.sendRedirect(
                        req.getContextPath() + "/owner/dashboard"
                );

            } else {

                resp.sendRedirect(
                        req.getContextPath() + "/restaurants"
                );
            }

        } catch (IllegalArgumentException e) {

            forwardWithError(
                    req,
                    resp,
                    e.getMessage(),
                    username,
                    email,
                    phone
            );
        }
    }

    private void forwardWithError(
            HttpServletRequest req,
            HttpServletResponse resp,
            String error,
            String username,
            String email,
            String phone
    ) throws ServletException, IOException {

        req.setAttribute("error", error);
        req.setAttribute("username", username);
        req.setAttribute("email", email);
        req.setAttribute("phone", phone);

        req.getRequestDispatcher(
                "/WEB-INF/jsp/auth/register.jsp"
        ).forward(req, resp);
    }
}