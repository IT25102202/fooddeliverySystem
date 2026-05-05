package com.fooddelivery.model;

import jakarta.persistence.*;


@Entity
@DiscriminatorValue("VEG")
public class VegRestaurant extends com.fooddelivery.model.Restaurant {

    @Column(name = "is_jain_friendly")
    private boolean isJainFriendly;

    public VegRestaurant() { super(); setType("VEG"); }

    public boolean isJainFriendly() { return isJainFriendly; }
    public void setJainFriendly(boolean jainFriendly) { isJainFriendly = jainFriendly; }

    // Polymorphism - different display for Veg restaurant
    @Override
    public String getDisplayInfo() {
        return "🌱 [VEG] " + getName() + " | " + getCuisine() + " | Rating: " + getRating();
    }

    @Override
    public String getMenuLabel() { return "Pure Veg Menu"; }
}
