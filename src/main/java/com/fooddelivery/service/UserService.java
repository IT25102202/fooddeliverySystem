package com.fooddelivery.service;

import com.fooddelivery.model.*;
import com.fooddelivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * UserService - Component 1: IT251022022
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // CREATE - Register new user
    public boolean register(Customer customer) {
        if (userRepository.existsByEmail(customer.getEmail())) return false;
        if (customer.getId() == null || customer.getId().isEmpty())
            customer.setId("USR" + System.currentTimeMillis());
        userRepository.save(customer);
        return true;
    }

    // READ - Find by email (login)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // READ - Find by ID
    public User findById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    // READ - Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // READ - Search users
    public List<User> searchUsers(String term) {
        return userRepository.searchUsers(term);
    }

    // UPDATE - Update user profile
    public boolean updateUser(Customer updated) {
        Optional<User> existing = userRepository.findById(updated.getId());
        if (existing.isEmpty()) return false;
        if (updated.getPassword() == null || updated.getPassword().isEmpty())
            updated.setPassword(existing.get().getPassword());
        updated.setEmail(existing.get().getEmail());
        updated.setRole("CUSTOMER");
        userRepository.save(updated);
        return true;
    }

    // DELETE - Delete user account
    public boolean deleteUser(String id) {
        if (!userRepository.existsById(id)) return false;
        userRepository.deleteById(id);
        return true;
    }

    // LOGIN - Polymorphism: validateLogin called on actual subclass object
    public User login(String email, String password) {
        User user = findByEmail(email);
        if (user != null && user.validateLogin(password)) return user;
        return null;
    }

    public int countUsers() { return (int) userRepository.count(); }

    // Seed admin + sample customers if DB is empty
    public void initSampleData() {
        if (userRepository.count() == 0) {
            Admin admin = new Admin("ADM001", "admin", "admin@foodhub.lk", "admin123", "0112345678", "Management");
            userRepository.save(admin);
            userRepository.save(new Customer("USR001","Kasun Perera","kasun@gmail.com","password123","0771234567","45 Galle Road","Colombo"));
            userRepository.save(new Customer("USR002","Nimasha Silva","nimasha@gmail.com","password123","0762345678","12 Kandy Road","Kandy"));
            userRepository.save(new Customer("USR003","Amal Fernando","amal@gmail.com","password123","0753456789","78 Beach Road","Galle"));
        }
    }
}
