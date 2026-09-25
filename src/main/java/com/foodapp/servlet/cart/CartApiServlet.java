package com.foodapp.servlet.cart;

import com.foodapp.dto.CartDTO;
import com.foodapp.entity.Cart;
import com.foodapp.service.CartService;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet("/api/cart/*")
public class CartApiServlet extends HttpServlet {

    @Inject
    private CartService cartService;

    private final Jsonb jsonb = JsonbBuilder.create();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Long userId = (Long) req.getSession().getAttribute("userId");
        if (userId == null) {
            writeError(resp, 401, "Please log in first.");
            return;
        }

        String action = req.getPathInfo(); // /add, /update, /remove, /clear
        if (action == null) {
            writeError(resp, 400, "Missing action");
            return;
        }

        try {
            switch (action) {
                case "/add"    -> handleAdd(req, resp, userId);
                case "/update" -> handleUpdate(req, resp, userId);
                case "/remove" -> handleRemove(req, resp, userId);
                case "/clear"  -> handleClear(req, resp, userId);
                default        -> writeError(resp, 404, "Unknown action: " + action);
            }
        } catch (IllegalStateException e) {
            writeError(resp, 409, e.getMessage());
        } catch (IllegalArgumentException e) {
            writeError(resp, 400, e.getMessage());
        } catch (Exception e) {
            writeError(resp, 500, "Server error: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Long userId = (Long) req.getSession().getAttribute("userId");
        if (userId == null) {
            writeError(resp, 401, "Please log in first.");
            return;
        }

        Cart cart = cartService.getCart(userId);
        CartDTO dto = new CartDTO(cart);

        // Also update session count so the navbar can show it
        req.getSession().setAttribute("cartCount", dto.getTotalItems());

        resp.getWriter().write(jsonb.toJson(dto));
    }

    private void handleAdd(HttpServletRequest req, HttpServletResponse resp, Long userId) throws IOException {
        JsonObject body = readJson(req);
        Long itemId = body.getJsonNumber("itemId").longValue();
        int quantity = body.containsKey("quantity") ? body.getInt("quantity") : 1;

        Cart cart = cartService.addItem(userId, itemId, quantity);
        CartDTO dto = new CartDTO(cart);
        req.getSession().setAttribute("cartCount", dto.getTotalItems());
        resp.getWriter().write(jsonb.toJson(Map.of("success", true, "cart", dto)));
    }

    private void handleUpdate(HttpServletRequest req, HttpServletResponse resp, Long userId) throws IOException {
        JsonObject body = readJson(req);
        Long cartItemId = body.getJsonNumber("cartItemId").longValue();
        int quantity = body.getInt("quantity");

        Cart cart = cartService.updateQuantity(userId, cartItemId, quantity);
        CartDTO dto = new CartDTO(cart);
        req.getSession().setAttribute("cartCount", dto.getTotalItems());
        resp.getWriter().write(jsonb.toJson(Map.of("success", true, "cart", dto)));
    }

    private void handleRemove(HttpServletRequest req, HttpServletResponse resp, Long userId) throws IOException {
        JsonObject body = readJson(req);
        Long cartItemId = body.getJsonNumber("cartItemId").longValue();

        Cart cart = cartService.removeItem(userId, cartItemId);
        CartDTO dto = new CartDTO(cart);
        req.getSession().setAttribute("cartCount", dto.getTotalItems());
        resp.getWriter().write(jsonb.toJson(Map.of("success", true, "cart", dto)));
    }

    private void handleClear(HttpServletRequest req, HttpServletResponse resp, Long userId) throws IOException {
        cartService.clearCart(userId);
        req.getSession().setAttribute("cartCount", 0);
        resp.getWriter().write("{\"success\":true}");
    }

    private JsonObject readJson(HttpServletRequest req) throws IOException {
        try (var reader = Json.createReader(req.getInputStream())) {
            return reader.readObject();
        }
    }

    private void writeError(HttpServletResponse resp, int status, String message) throws IOException {
        resp.setStatus(status);
        resp.getWriter().write(Json.createObjectBuilder()
                .add("success", false)
                .add("error", message)
                .build().toString());
    }
}