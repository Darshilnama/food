package com.foodapp.dto;

import com.foodapp.entity.Cart;
import com.foodapp.entity.CartItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class CartDTO {
    private Long cartId;
    private Long restaurantId;
    private String restaurantName;
    private List<CartItemDTO> items;
    private BigDecimal subtotal;
    private int totalItems;

    public CartDTO(Cart cart) {
        if (cart == null) {
            this.items = List.of();
            this.subtotal = BigDecimal.ZERO;
            this.totalItems = 0;
            return;
        }
        this.cartId = cart.getCartId();
        if (cart.getRestaurant() != null) {
            this.restaurantId = cart.getRestaurant().getRestaurantId();
            this.restaurantName = cart.getRestaurant().getName();
        }
        this.items = cart.getItems().stream().map(CartItemDTO::new).collect(Collectors.toList());
        this.subtotal = items.stream()
                .map(CartItemDTO::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.totalItems = items.stream().mapToInt(CartItemDTO::getQuantity).sum();
    }

    public Long getCartId() { return cartId; }
    public Long getRestaurantId() { return restaurantId; }
    public String getRestaurantName() { return restaurantName; }
    public List<CartItemDTO> getItems() { return items; }
    public BigDecimal getSubtotal() { return subtotal; }
    public int getTotalItems() { return totalItems; }
}