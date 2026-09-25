package com.foodapp.servlet.owner;

import com.foodapp.service.MenuService;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/owner/menu-item/delete")
public class MenuItemDeleteServlet extends HttpServlet {

    @Inject private MenuService menuService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long userId = (Long) req.getSession().getAttribute("userId");
        try {
            Long itemId = Long.parseLong(req.getParameter("itemId"));
            menuService.deleteMenuItem(itemId, userId);
        } catch (Exception ignored) {}
        resp.sendRedirect(req.getContextPath() + "/owner/dashboard");
    }
}