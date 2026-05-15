package com.fooddelivery.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Payment base class - Encapsulation + JPA Entity
 * Component 5: Payment Management (IT25101967)
 */
@Entity
@Table(name = "payments")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "method", discriminatorType = DiscriminatorType.STRING)
public class Payment {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "order_id", length = 50)
    private String orderId;

    @Column(name = "customer_id", length = 50)
    private String customerId;

    @Column(name = "amount")
    private double amount;

    @Column(name = "method", insertable = false, updatable = false)
    private String method;

    @Column(name = "status")
    private String status;

    @Column(name = "payment_date_time")
    private String paymentDateTime;

    @Column(name = "transaction_ref")
    private String transactionRef;

    public Payment() {
        this.paymentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.status = "PENDING";
    }

    public Payment(String id, String orderId, String customerId, double amount, String method) {
        this();
        this.id = id; this.orderId = orderId; this.customerId = customerId;
        this.amount = amount; this.method = method;
    }

    // Encapsulation - Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPaymentDateTime() { return paymentDateTime; }
    public void setPaymentDateTime(String paymentDateTime) { this.paymentDateTime = paymentDateTime; }
    public String getTransactionRef() { return transactionRef; }
    public void setTransactionRef(String transactionRef) { this.transactionRef = transactionRef; }

    // Polymorphism - overridden in CardPayment and CashPayment
    public String processPayment() {
        this.status = "COMPLETED";
        return "Payment processed";
    }

    public String getPaymentSummary() {
        return "Payment #" + id + " | LKR " + amount + " | " + method + " | " + status;
    }
}
