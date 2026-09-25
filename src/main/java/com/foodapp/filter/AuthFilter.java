package com.foodapp.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Set;

@WebFilter("/*")
public class AuthFilter implements Filter {

    // Paths that don't require login
    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/",
            "/index.jsp",
            "/login",
            "/register",
            "/css",
            "/js",
            "/images"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());

        // Allow public paths and static assets
        if (isPublic(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("user") != null);

        if (!loggedIn) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // ⭐ Role-based access control
        Object roleAttr = session.getAttribute("role");
        String roleName = (roleAttr != null) ? roleAttr.toString() : "";

        // /owner/* → only RESTAURANT_OWNER
        if (path.startsWith("/owner") && !"RESTAURANT_OWNER".equals(roleName)) {
            resp.sendRedirect(req.getContextPath() + "/restaurants");
            return;
        }

        // /admin/* → only ADMIN
        if (path.startsWith("/admin") && !"ADMIN".equals(roleName)) {
            resp.sendRedirect(req.getContextPath() + "/restaurants");
            return;
        }

        chain.doFilter(request, response);
    }


    /**
     * Determines whether a path is publicly accessible.
     */
    private boolean isPublic(String path) {

        if (path.isEmpty() || path.equals("/")) {
            return true;
        }

        for (String prefix : PUBLIC_PATHS) {

            if (path.equals(prefix)
                    || path.startsWith(prefix + "/")) {

                return true;
            }
        }

        return false;
    }
}