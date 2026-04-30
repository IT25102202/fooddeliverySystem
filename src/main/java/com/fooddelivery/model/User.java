package com.fooddelivery.model;

import jakarta.persistence.*;

/**
 * Base User class - Encapsulation + JPA Entity
 * Component 1: User Management (IT251022022)
 */
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
public class User {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone")
    private String phone;

    @Column(name = "role", insertable = false, updatable = false)
    private String role;

    public User() {}

    public User(String id, String username, String email, String password, String phone, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
    }

    // Encapsulation - Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // Polymorphism - overridden in subclasses
    public String getDisplayInfo() {
        return "User: " + username + " | Email: " + email;
    }

    // Polymorphism - different validation per subclass
    public boolean validateLogin(String inputPassword) {
        return this.password.equals(inputPassword);
    }
}
