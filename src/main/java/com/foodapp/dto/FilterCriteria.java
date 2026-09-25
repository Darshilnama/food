package com.foodapp.dto;

import java.math.BigDecimal;

public class FilterCriteria {
    private BigDecimal minRating;
    private BigDecimal maxDistance;
    private Integer maxDeliveryTime;
    private boolean vegOnly;
    private String cuisine;

    public BigDecimal getMinRating() { return minRating; }
    public void setMinRating(BigDecimal minRating) { this.minRating = minRating; }
    public BigDecimal getMaxDistance() { return maxDistance; }
    public void setMaxDistance(BigDecimal maxDistance) { this.maxDistance = maxDistance; }
    public Integer getMaxDeliveryTime() { return maxDeliveryTime; }
    public void setMaxDeliveryTime(Integer maxDeliveryTime) { this.maxDeliveryTime = maxDeliveryTime; }
    public boolean isVegOnly() { return vegOnly; }
    public void setVegOnly(boolean vegOnly) { this.vegOnly = vegOnly; }
    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }
}