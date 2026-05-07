package com.fooddelivery.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Review class - Encapsulation + JPA Entity
 * Component 6: Feedback & Review Management (IT25100159)
 */
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "customer_id", length = 50)
    private String customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "restaurant_id", length = 50)
    private String restaurantId;

    @Column(name = "restaurant_name")
    private String restaurantName;

    @Column(name = "order_id", length = 50)
    private String orderId;

    @Column(name = "rating")
    private int rating;

    @Column(name = "comment", length = 1000)
    private String comment;

    @Column(name = "review_date_time")
    private String reviewDateTime;

    @Column(name = "status")
    private String status;

    @Column(name = "type")
    private String type;

    public Review() {
        this.reviewDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.status = "ACTIVE";
        this.type = "CUSTOMER";
    }

    public Review(String id, String customerId, String customerName, String restaurantId,
                  String restaurantName, String orderId, int rating, String comment) {
        this();
        this.id = id; this.customerId = customerId; this.customerName = customerName;
        this.restaurantId = restaurantId; this.restaurantName = restaurantName;
        this.orderId = orderId; this.rating = rating; this.comment = comment;
    }

    // Encapsulation - Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getRestaurantId() { return restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }
    public String getRestaurantName() { return restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public String getReviewDateTime() { return reviewDateTime; }
    public void setReviewDateTime(String reviewDateTime) { this.reviewDateTime = reviewDateTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    // Polymorphism - display methods
    public String getStarDisplay() {
        return "★".repeat(rating) + "☆".repeat(5 - rating);
    }

    public String getDisplayInfo() {
        return customerName + " | " + getStarDisplay() + " | " + comment;
    }
}


