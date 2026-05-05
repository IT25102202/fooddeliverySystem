package com.fooddelivery.model;

import jakarta.persistence.*;

/**
 * Dessert - extends FoodItem (Inheritance)
 * Component 3: Food Menu Management (IT25100897)
 */
@Entity
@DiscriminatorValue("DESSERT")
public class Dessert extends FoodItem {

    @Column(name = "contains_nuts")
    private boolean containsNuts;

    @Column(name = "is_gluten_free")
    private boolean isGlutenFree;

    public Dessert() { super(); setCategory("DESSERT"); setVeg(true); }

    public boolean isContainsNuts() { return containsNuts; }
    public void setContainsNuts(boolean containsNuts) { this.containsNuts = containsNuts; }
    public boolean isGlutenFree() { return isGlutenFree; }
    public void setGlutenFree(boolean glutenFree) { isGlutenFree = glutenFree; }

    // Polymorphism
    @Override
    public String getDisplayInfo() {
        return "🍰 " + getName() + " | LKR " + getPrice() + (isGlutenFree ? " | Gluten-Free" : "");
    }

    @Override
    public String getSpecialTag() { return isGlutenFree ? "Gluten-Free" : "Classic"; }
}
