package com.foodapp.dto;

import com.foodapp.entity.Restaurant;

import java.math.BigDecimal;

public class RestaurantCardDTO {
    private Long id;
    private String name;
    private String cuisineType;
    private String imageUrl;
    private BigDecimal rating;
    private Integer totalReviews;
    private Integer avgDeliveryTime;
    private BigDecimal distanceKm;
    private boolean vegOnly;
    private boolean isOpen;

    public RestaurantCardDTO(Restaurant r) {
        this.id = r.getRestaurantId();
        this.name = r.getName();
        this.cuisineType = r.getCuisineType();
        this.imageUrl = r.getImageUrl();
        this.rating = r.getRating();
        this.totalReviews = r.getTotalReviews();
        this.avgDeliveryTime = r.getAvgDeliveryTime();
        this.distanceKm = r.getDistanceKm();
        this.vegOnly = r.getVegOnly() != null && r.getVegOnly();
        this.isOpen = r.getIsOpen() != null && r.getIsOpen();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCuisineType() { return cuisineType; }
    public String getImageUrl() { return imageUrl; }
    public BigDecimal getRating() { return rating; }
    public Integer getTotalReviews() { return totalReviews; }
    public Integer getAvgDeliveryTime() { return avgDeliveryTime; }
    public BigDecimal getDistanceKm() { return distanceKm; }
    public boolean isVegOnly() { return vegOnly; }
    public boolean isOpen() { return isOpen; }
}