package com.foodapp.service;

import com.foodapp.dao.CartDAO;
import com.foodapp.dao.MenuItemDAO;
import com.foodapp.dao.UserDAO;
import com.foodapp.entity.Cart;
import com.foodapp.entity.CartItem;
import com.foodapp.entity.MenuItem;
import com.foodapp.entity.User;
import com.foodapp.websocket.EventBroadcaster;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Optional;

@ApplicationScoped
public class CartService {

    @Inject
    private CartDAO cartDAO;

    @Inject
    private MenuItemDAO menuItemDAO;

    @Inject
    private UserDAO userDAO;
    
    @Inject
    private EventBroadcaster eventBroadcaster;

    @Transactional
    public Cart getOrCreateCart(Long userId) {
        Cart cart = cartDAO.findByUserId(userId);
        if (cart == null) {
            User user = userDAO.findById(userId);
            cart = new Cart();
            cart.setUser(user);
            cart = cartDAO.save(cart);
        }
        return cart;
    }

    @Transactional
    public Cart addItem(Long userId, Long menuItemId, int quantity) {
        Cart cart = getOrCreateCart(userId);
        MenuItem menuItem = menuItemDAO.findById(menuItemId);
        if (menuItem == null) throw new IllegalArgumentException("Item not found");

        // Rule: cart can only contain items from one restaurant
        if (cart.getRestaurant() == null) {
            cart.setRestaurant(menuItem.getRestaurant());
        } else if (!cart.getRestaurant().getRestaurantId()
                .equals(menuItem.getRestaurant().getRestaurantId())) {
            throw new IllegalStateException(
                    "Your cart contains items from another restaurant. Please clear the cart first.");
        }

        Optional<CartItem> existing = cart.getItems().stream()
                .filter(i -> i.getMenuItem().getItemId().equals(menuItemId))
                .findFirst();

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setMenuItem(menuItem);
            newItem.setQuantity(quantity);
            cart.addItem(newItem);
        }
        Cart saved = cartDAO.update(cart);
        if (eventBroadcaster != null) {
            eventBroadcaster.broadcastToUser(userId, "{\"type\": \"CART_UPDATED\"}");
        }
        return saved;
    }

    @Transactional
    public Cart updateQuantity(Long userId, Long cartItemId, int quantity) {
        Cart cart = cartDAO.findByUserId(userId);
        if (cart == null) throw new IllegalStateException("Cart not found");

        cart.getItems().stream()
                .filter(i -> i.getCartItemId().equals(cartItemId))
                .findFirst()
                .ifPresent(item -> {
                    if (quantity <= 0) {
                        cart.removeItem(item);
                    } else {
                        item.setQuantity(quantity);
                    }
                });

        // ⭐ NEW: If cart becomes empty, release the restaurant lock
        if (cart.getItems().isEmpty()) {
            cart.setRestaurant(null);
        }

        Cart saved = cartDAO.update(cart);
        if (eventBroadcaster != null) {
            eventBroadcaster.broadcastToUser(userId, "{\"type\": \"CART_UPDATED\"}");
        }
        return saved;
    }
    @Transactional
    public Cart removeItem(Long userId, Long cartItemId) {
        Cart cart = cartDAO.findByUserId(userId);
        if (cart == null) throw new IllegalStateException("Cart not found");

        CartItem toRemove = cart.getItems().stream()
                .filter(i -> i.getCartItemId().equals(cartItemId))
                .findFirst()
                .orElse(null);

        if (toRemove != null) {
            cart.removeItem(toRemove);
        }

        if (cart.getItems().isEmpty()) {
            cart.setRestaurant(null);
        }

        Cart saved = cartDAO.update(cart);
        if (eventBroadcaster != null) {
            eventBroadcaster.broadcastToUser(userId, "{\"type\": \"CART_UPDATED\"}");
        }
        return saved;
    }
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartDAO.findByUserId(userId);
        if (cart != null) {
            cart.getItems().clear();
            cart.setRestaurant(null);
            cartDAO.update(cart);
            if (eventBroadcaster != null) {
                eventBroadcaster.broadcastToUser(userId, "{\"type\": \"CART_UPDATED\"}");
            }
        }
    }

    public Cart getCart(Long userId) {
        return cartDAO.findByUserId(userId);
    }
}