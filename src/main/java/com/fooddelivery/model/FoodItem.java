package com.fooddelivery.model;

import jakarta.persistence.*;

/**
 * FoodItem base class - Encapsulation + JPA Entity
 * Component 3: Food Menu Management (IT25100897)
 */
@Entity
@Table(name = "food_items")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "category", discriminatorType = DiscriminatorType.STRING)
public class FoodItem {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "restaurant_id", length = 50)
    private String restaurantId;

    @Column(name = "category", insertable = false, updatable = false)
    private String category;

    @Column(name = "is_available")
    private boolean isAvailable;

    @Column(name = "is_veg")
    private boolean isVeg;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "prep_time")
    private int prepTime;

    public FoodItem() {}

    public FoodItem(String id, String name, String description, double price,
                    String restaurantId, String category, boolean isAvailable,
                    boolean isVeg, String imageUrl, int prepTime) {
        this.id = id; this.name = name; this.description = description;
        this.price = price; this.restaurantId = restaurantId;
        this.category = category; this.isAvailable = isAvailable;
        this.isVeg = isVeg; this.imageUrl = imageUrl; this.prepTime = prepTime;
    }

    // Encapsulation - Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getRestaurantId() { return restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    public boolean isVeg() { return isVeg; }
    public void setVeg(boolean veg) { isVeg = veg; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public int getPrepTime() { return prepTime; }
    public void setPrepTime(int prepTime) { this.prepTime = prepTime; }

    // Polymorphism - overridden in subclasses
    public String getDisplayInfo() {
        return name + " | LKR " + price + " | " + (isVeg ? "Veg" : "Non-Veg");
    }

    public String getSpecialTag() { return ""; }
}
