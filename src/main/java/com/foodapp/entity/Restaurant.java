package com.foodapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "restaurants")
public class Restaurant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long restaurantId;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String address;

    @Column(length = 20)
    private String phone;

    @Column(name = "cuisine_type", length = 100)
    private String cuisineType;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(precision = 2, scale = 1)
    private BigDecimal rating = BigDecimal.ZERO;

    @Column(name = "total_reviews")
    private Integer totalReviews = 0;

    @Column(name = "avg_delivery_time")
    private Integer avgDeliveryTime = 30;

    @Column(name = "distance_km", precision = 4, scale = 1)
    private BigDecimal distanceKm = BigDecimal.ZERO;

    @Column(name = "is_open")
    private Boolean isOpen = true;

    @Column(name = "veg_only")
    private Boolean vegOnly = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    // Getters and Setters
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getCuisineType() { return cuisineType; }
    public void setCuisineType(String cuisineType) { this.cuisineType = cuisineType; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }
    public Integer getTotalReviews() { return totalReviews; }
    public void setTotalReviews(Integer totalReviews) { this.totalReviews = totalReviews; }
    public Integer getAvgDeliveryTime() { return avgDeliveryTime; }
    public void setAvgDeliveryTime(Integer avgDeliveryTime) { this.avgDeliveryTime = avgDeliveryTime; }
    public BigDecimal getDistanceKm() { return distanceKm; }
    public void setDistanceKm(BigDecimal distanceKm) { this.distanceKm = distanceKm; }
    public Boolean getIsOpen() { return isOpen; }
    public void setIsOpen(Boolean isOpen) { this.isOpen = isOpen; }
    public Boolean getVegOnly() { return vegOnly; }
    public void setVegOnly(Boolean vegOnly) { this.vegOnly = vegOnly; }
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
}