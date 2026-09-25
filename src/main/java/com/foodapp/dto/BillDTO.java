package com.foodapp.dto;

import com.foodapp.entity.Order;
import com.foodapp.entity.OrderItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

public class BillDTO {

    private Long orderId;
    private String restaurantName;
    private String customerName;
    private String deliveryAddress;
    private List<OrderItem> items;
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal gst;
    private BigDecimal deliveryFee;
    private BigDecimal grandTotal;
    private LocalDateTime orderDate;

    public BillDTO(Order order) {
        this.orderId = order.getOrderId();
        this.restaurantName = (order.getRestaurant() != null)
                ? order.getRestaurant().getName() : "Restaurant";
        this.customerName = (order.getUser() != null)
                ? order.getUser().getUsername() : "Customer";

        if (order.getAddress() != null) {
            StringBuilder sb = new StringBuilder();
            if (order.getAddress().getStreet() != null) sb.append(order.getAddress().getStreet());
            if (order.getAddress().getCity() != null) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(order.getAddress().getCity());
            }
            this.deliveryAddress = sb.length() > 0 ? sb.toString() : "—";
        } else {
            this.deliveryAddress = "—";
        }

        this.items = order.getItems() != null ? order.getItems() : List.of();

        this.subtotal = order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO;
        this.discount = order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO;

        BigDecimal finalAmount = order.getFinalAmount() != null ? order.getFinalAmount() : BigDecimal.ZERO;
        this.gst = finalAmount.multiply(BigDecimal.valueOf(0.05)).setScale(2, RoundingMode.HALF_UP);
        this.deliveryFee = BigDecimal.valueOf(30);
        this.grandTotal = finalAmount.add(gst).add(deliveryFee);
        this.orderDate = order.getCreatedAt();
    }

    public Long getOrderId() { return orderId; }
    public String getRestaurantName() { return restaurantName; }
    public String getCustomerName() { return customerName; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public List<OrderItem> getItems() { return items; }
    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getDiscount() { return discount; }
    public BigDecimal getGst() { return gst; }
    public BigDecimal getDeliveryFee() { return deliveryFee; }
    public BigDecimal getGrandTotal() { return grandTotal; }
    public LocalDateTime getOrderDate() { return orderDate; }
}