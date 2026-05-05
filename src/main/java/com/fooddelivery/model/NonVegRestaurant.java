package com.fooddelivery.model;

import jakarta.persistence.*;


@Entity
@DiscriminatorValue("NONVEG")
public class NonVegRestaurant extends com.fooddelivery.model.Restaurant {

    @Column(name = "has_seafood")
    private boolean hasSeafood;

    @Column(name = "has_beef")
    private boolean hasBeef;

    public NonVegRestaurant() { super(); setType("NONVEG"); }

    public boolean isHasSeafood() { return hasSeafood; }
    public void setHasSeafood(boolean hasSeafood) { this.hasSeafood = hasSeafood; }
    public boolean isHasBeef() { return hasBeef; }
    public void setHasBeef(boolean hasBeef) { this.hasBeef = hasBeef; }

    // Polymorphism - different display for NonVeg restaurant
    @Override
    public String getDisplayInfo() {
        return "🍖 [NON-VEG] " + getName() + " | " + getCuisine() + " | Rating: " + getRating();
    }

    @Override
    public String getMenuLabel() { return "Non-Veg Menu"; }
}
