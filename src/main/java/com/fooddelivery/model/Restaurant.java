package com.fooddelivery.model;

import jakarta.persistence.*;


@Entity
@Table(name = "restaurants")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class Restaurant {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "phone")
    private String phone;

    @Column(name = "cuisine")
    private String cuisine;

    @Column(name = "type", insertable = false, updatable = false)
    private String type;

    @Column(name = "rating")
    private double rating;

    @Column(name = "is_open")
    private boolean isOpen;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "open_time")
    private String openTime;

    @Column(name = "close_time")
    private String closeTime;

    @Column(name = "delivery_fee")
    private double deliveryFee;

    @Column(name = "discount_percent")
    private Integer discountPercent = 0;   // 0 = no discount, e.g. 10 = 10% off

    @Column(name = "discount_label", length = 100)
    private String discountLabel;  // e.g. "Online Special", "Weekend Deal"

    public Restaurant() {}

    public Restaurant(String id, String name, String address, String city, String phone,
                      String cuisine, String type, double rating, boolean isOpen,
                      String imageUrl, String description, String openTime,
                      String closeTime, double deliveryFee) {
        this.id = id; this.name = name; this.address = address; this.city = city;
        this.phone = phone; this.cuisine = cuisine; this.type = type;
        this.rating = rating; this.isOpen = isOpen; this.imageUrl = imageUrl;
        this.description = description; this.openTime = openTime;
        this.closeTime = closeTime; this.deliveryFee = deliveryFee;
    }

    // Encapsulation - Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public boolean isOpen() { return isOpen; }
    public void setOpen(boolean open) { isOpen = open; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getOpenTime() { return openTime; }
    public void setOpenTime(String openTime) { this.openTime = openTime; }
    public String getCloseTime() { return closeTime; }
    public void setCloseTime(String closeTime) { this.closeTime = closeTime; }
    public double getDeliveryFee() { return deliveryFee; }
    public void setDeliveryFee(double deliveryFee) { this.deliveryFee = deliveryFee; }
    public int getDiscountPercent() { return discountPercent != null ? discountPercent : 0; }
    public void setDiscountPercent(int discountPercent) { this.discountPercent = discountPercent; }
    public void setDiscountPercent(Integer discountPercent) { this.discountPercent = discountPercent != null ? discountPercent : 0; }
    public String getDiscountLabel() { return discountLabel; }
    public void setDiscountLabel(String discountLabel) { this.discountLabel = discountLabel; }
    public boolean hasDiscount() { return discountPercent != null && discountPercent > 0; }

    // Polymorphism - overridden in subclasses
    public String getDisplayInfo() {
        return name + " | " + cuisine + " | Rating: " + rating;
    }

    public String getMenuLabel() { return "Menu"; }
}
