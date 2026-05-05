package com.fooddelivery.model;

import jakarta.persistence.*;

/**
 * Beverage - extends FoodItem (Inheritance)
 * Component 3: Food Menu Management (IT25100897)
 */
@Entity
@DiscriminatorValue("BEVERAGE")
public class Beverage extends FoodItem {

    @Column(name = "size")
    private String size;

    @Column(name = "is_cold")
    private boolean isCold;

    @Column(name = "is_alcoholic")
    private boolean isAlcoholic;

    public Beverage() { super(); setCategory("BEVERAGE"); setVeg(true); }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public boolean isCold() { return isCold; }
    public void setCold(boolean cold) { isCold = cold; }
    public boolean isAlcoholic() { return isAlcoholic; }
    public void setAlcoholic(boolean alcoholic) { isAlcoholic = alcoholic; }

    // Polymorphism
    @Override
    public String getDisplayInfo() {
        return "🥤 " + getName() + " | LKR " + getPrice() + " | " + (isCold ? "Cold" : "Hot");
    }

    @Override
    public String getSpecialTag() { return isCold ? "Cold" : "Hot"; }
}
