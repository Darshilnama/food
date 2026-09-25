package com.foodapp.dto;

import com.foodapp.entity.CartItem;

import java.math.BigDecimal;

public class CartItemDTO {
    private Long cartItemId;
    private Long itemId;
    private String name;
    private BigDecimal unitPrice;
    private int quantity;
    private BigDecimal lineTotal;
    private String foodType;
    private String imageUrl;

    public CartItemDTO(CartItem ci) {
        this.cartItemId = ci.getCartItemId();
        this.itemId = ci.getMenuItem().getItemId();
        this.name = ci.getMenuItem().getName();
        this.unitPrice = ci.getMenuItem().getPrice();
        this.quantity = ci.getQuantity();
        this.lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        this.foodType = ci.getMenuItem().getFoodType().name();
        this.imageUrl = ci.getMenuItem().getImageUrl();
    }

    public Long getCartItemId() { return cartItemId; }
    public Long getItemId() { return itemId; }
    public String getName() { return name; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }
    public BigDecimal getLineTotal() { return lineTotal; }
    public String getFoodType() { return foodType; }
    public String getImageUrl() { return imageUrl; }
}