package com.fooddelivery.repository;

import com.fooddelivery.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * OrderRepository - Database operations for Order
 * Component 4: IT25101831
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    // READ - All orders for a customer (newest first)
    List<Order> findByCustomerIdOrderByOrderDateTimeDesc(String customerId);

    // READ - All orders for a restaurant
    List<Order> findByRestaurantId(String restaurantId);

    // READ - Filter by status
    List<Order> findByStatus(String status);

    // READ - All orders newest first
    List<Order> findAllByOrderByOrderDateTimeDesc();
}