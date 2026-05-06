package com.fooddelivery.model;

import jakarta.persistence.*;

/**
 * Customer - extends User (Inheritance)
 * Component 1: User Management (IT251022022)
 */
@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    public Customer() {
        super();
        setRole("CUSTOMER");
    }

    public Customer(String id, String username, String email, String password,
                    String phone, String address, String city) {
        super(id, username, email, password, phone, "CUSTOMER");
        this.address = address;
        this.city = city;
    }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    // Polymorphism - overrides parent getDisplayInfo()
    @Override
    public String getDisplayInfo() {
        return "Customer: " + getUsername() + " | City: " + city;
    }

    // Polymorphism - customer-specific login validation
    @Override
    public boolean validateLogin(String inputPassword) {
        return getPassword().equals(inputPassword) && "CUSTOMER".equals(getRole());
    }
}
