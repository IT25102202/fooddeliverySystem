package com.fooddelivery.repository;

import com.fooddelivery.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * PaymentRepository - Database operations for Payment
 * Component 5: IT25101967
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    // READ - All payments by customer
    List<Payment> findByCustomerIdOrderByPaymentDateTimeDesc(String customerId);

    // READ - Payment for a specific order
    Optional<Payment> findByOrderId(String orderId);

    // READ - All payments newest first
    List<Payment> findAllByOrderByPaymentDateTimeDesc();

    // READ - Total revenue (completed payments only)
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'COMPLETED'")
    Double getTotalRevenue();
}
