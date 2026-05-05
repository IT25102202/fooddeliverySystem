package com.fooddelivery.model;

import jakarta.persistence.*;

/**
 * MainCourse - extends FoodItem (Inheritance)
 * Component 3: Food Menu Management (IT25100897)
 */
@Entity
@DiscriminatorValue("MAINCOURSE")
public class MainCourse extends FoodItem {

    @Column(name = "is_spicy")
    private boolean isSpicy;

    @Column(name = "portion_size")
    private String portionSize;

    public MainCourse() { super(); setCategory("MAINCOURSE"); }

    public MainCourse(String id, String name, String description, double price,
                      String restaurantId, boolean isAvailable, boolean isVeg,
                      String imageUrl, int prepTime, boolean isSpicy, String portionSize) {
        super(id, name, description, price, restaurantId, "MAINCOURSE", isAvailable, isVeg, imageUrl, prepTime);
        this.isSpicy = isSpicy;
        this.portionSize = portionSize;
    }

    public boolean isSpicy() { return isSpicy; }
    public void setSpicy(boolean spicy) { isSpicy = spicy; }
    public String getPortionSize() { return portionSize; }
    public void setPortionSize(String portionSize) { this.portionSize = portionSize; }

    // Polymorphism
    @Override
    public String getDisplayInfo() {
        return "🍽️ " + getName() + " | LKR " + getPrice() + (isSpicy ? " | 🌶️ Spicy" : "");
    }

    @Override
    public String getSpecialTag() { return isSpicy ? "Spicy" : "Mild"; }
}
