package com.fooddelivery.model;

import jakarta.persistence.*;

/**
 * Admin - extends User (Inheritance)
 * Component 1: User Management (IT251022022)
 */
@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {

    @Column(name = "department")
    private String department;

    public Admin() {
        super();
        setRole("ADMIN");
    }

    public Admin(String id, String username, String email, String password,
                 String phone, String department) {
        super(id, username, email, password, phone, "ADMIN");
        this.department = department;
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    // Polymorphism - admin-specific login validation
    @Override
    public boolean validateLogin(String inputPassword) {
        return getPassword().equals(inputPassword) && "ADMIN".equals(getRole());
    }

    @Override
    public String getDisplayInfo() {
        return "Admin: " + getUsername() + " | Dept: " + department;
    }
}
