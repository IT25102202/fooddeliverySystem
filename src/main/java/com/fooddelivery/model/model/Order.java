package com.fooddelivery.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Order class - Encapsulation + Abstraction + JPA Entity
 * Component 4: Order Management (IT25101831)
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "customer_id", length = 50)
    private String customerId;

    @Column(name = "restaurant_id", length = 50)
    private String restaurantId;

    @Column(name = "restaurant_name")
    private String restaurantName;

    // Store item lists as comma-separated strings in DB
    @Column(name = "item_ids", length = 1000)
    private String itemIdsStr;

    @Column(name = "item_names", length = 1000)
    private String itemNamesStr;

    @Column(name = "quantities", length = 500)
    private String quantitiesStr;

    @Column(name = "item_prices", length = 500)
    private String itemPricesStr;

    @Column(name = "total_amount")
    private double totalAmount;

    @Column(name = "status")
    private String status;

    @Column(name = "delivery_address", length = 500)
    private String deliveryAddress;

    @Column(name = "order_date_time")
    private String orderDateTime;

    @Column(name = "estimated_delivery")
    private String estimatedDelivery;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "delivery_fee")
    private double deliveryFee;

    @Column(name = "discount_amount")
    private Double discountAmount = 0.0;

    @Column(name = "special_instructions", length = 500)
    private String specialInstructions;

    // Transient lists - not stored directly in DB, derived from string columns
    @Transient
    private List<String> itemIds = new ArrayList<>();
    @Transient
    private List<String> itemNames = new ArrayList<>();
    @Transient
    private List<Integer> quantities = new ArrayList<>();
    @Transient
    private List<Double> itemPrices = new ArrayList<>();

    public Order() {
        this.orderDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.status = "PENDING";
        this.paymentStatus = "PENDING";
    }

    // Convert lists to/from strings for DB storage
    @PostLoad
    public void loadLists() {
        if (itemIdsStr != null && !itemIdsStr.isEmpty())
            for (String s : itemIdsStr.split(",")) itemIds.add(s);
        if (itemNamesStr != null && !itemNamesStr.isEmpty())
            for (String s : itemNamesStr.split("~")) itemNames.add(s);
        if (quantitiesStr != null && !quantitiesStr.isEmpty())
            for (String s : quantitiesStr.split(",")) quantities.add(Integer.parseInt(s));
        if (itemPricesStr != null && !itemPricesStr.isEmpty())
            for (String s : itemPricesStr.split(",")) itemPrices.add(Double.parseDouble(s));
    }

    @PrePersist @PreUpdate
    public void saveLists() {
        itemIdsStr    = String.join(",", itemIds);
        itemNamesStr  = String.join("~", itemNames);
        quantitiesStr = quantities.stream().map(String::valueOf).reduce("", (a,b) -> a.isEmpty() ? b : a+","+b);
        itemPricesStr = itemPrices.stream().map(String::valueOf).reduce("", (a,b) -> a.isEmpty() ? b : a+","+b);
    }

    // Getters and Setters - Encapsulation
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getRestaurantId() { return restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }
    public String getRestaurantName() { return restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }
    public List<String> getItemIds() { return itemIds; }
    public void setItemIds(List<String> itemIds) { this.itemIds = itemIds; }
    public List<String> getItemNames() { return itemNames; }
    public void setItemNames(List<String> itemNames) { this.itemNames = itemNames; }
    public List<Integer> getQuantities() { return quantities; }
    public void setQuantities(List<Integer> quantities) { this.quantities = quantities; }
    public List<Double> getItemPrices() { return itemPrices; }
    public void setItemPrices(List<Double> itemPrices) { this.itemPrices = itemPrices; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public String getOrderDateTime() { return orderDateTime; }
    public void setOrderDateTime(String orderDateTime) { this.orderDateTime = orderDateTime; }
    public String getEstimatedDelivery() { return estimatedDelivery; }
    public void setEstimatedDelivery(String estimatedDelivery) { this.estimatedDelivery = estimatedDelivery; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public double getDeliveryFee() { return deliveryFee; }
    public void setDeliveryFee(double deliveryFee) { this.deliveryFee = deliveryFee; }
    public double getDiscountAmount() { return discountAmount != null ? discountAmount : 0.0; }
    public void setDiscountAmount(double discountAmount) { this.discountAmount = discountAmount; }
    public void setDiscountAmount(Double discountAmount) { this.discountAmount = discountAmount != null ? discountAmount : 0.0; }
    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }

    // Abstraction - status display logic hidden from caller
    public String getStatusDisplay() {
        return switch (status) {
            case "PENDING"          -> "⏳ Pending";
            case "CONFIRMED"        -> "✅ Confirmed";
            case "PREPARING"        -> "👨‍🍳 Preparing";
            case "OUT_FOR_DELIVERY" -> "🚗 Out for Delivery";
            case "DELIVERED"        -> "🎉 Delivered";
            case "CANCELLED"        -> "❌ Cancelled";
            default -> status;
        };
    }

    public boolean canCancel() {
        return "PENDING".equals(status) || "CONFIRMED".equals(status);
    }

    public boolean isCompleted() { return "DELIVERED".equals(status); }
}
