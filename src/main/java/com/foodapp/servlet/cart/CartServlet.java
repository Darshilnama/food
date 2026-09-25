package com.foodapp.servlet.cart;

import com.foodapp.dto.CartDTO;
import com.foodapp.entity.Cart;
import com.foodapp.service.CartService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    @Inject
    private CartService cartService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long userId = (Long) req.getSession().getAttribute("userId");
        Cart cart = cartService.getCart(userId);
        CartDTO dto = new CartDTO(cart);

        req.setAttribute("cart", dto);
        req.getRequestDispatcher("/WEB-INF/jsp/cart/cart.jsp").forward(req, resp);
    }
}