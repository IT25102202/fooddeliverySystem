package com.fooddelivery.service;

import com.fooddelivery.model.*;
import com.fooddelivery.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * PaymentService - Component 5: IT25101967
 */
@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    // CREATE - Record payment (Polymorphism: Card vs Cash)
    public Payment recordPayment(String orderId, String customerId, double amount,
                                 String method, Map<String, String> details) {
        Payment payment;
        if ("CARD".equalsIgnoreCase(method)) {
            CardPayment card = new CardPayment();
            card.setId("PAY" + System.currentTimeMillis());
            card.setOrderId(orderId); card.setCustomerId(customerId); card.setAmount(amount);
            card.setCardNumber(details.getOrDefault("cardLast4", "0000"));
            card.setCardHolderName(details.getOrDefault("cardHolder", "Unknown"));
            card.setCardType(details.getOrDefault("cardType", "VISA"));
            card.processPayment(); // Polymorphism - CardPayment's version runs
            payment = card;
        } else {
            CashPayment cash = new CashPayment();
            cash.setId("PAY" + System.currentTimeMillis());
            cash.setOrderId(orderId); cash.setCustomerId(customerId); cash.setAmount(amount);
            double tendered = Double.parseDouble(details.getOrDefault("amountTendered", String.valueOf(amount)));
            cash.setAmountTendered(tendered);
            cash.setChangeAmount(tendered - amount);
            cash.processPayment(); // Polymorphism - CashPayment's version runs
            payment = cash;
        }
        return paymentRepository.save(payment);
    }

    // READ - Customer payment history
    public List<Payment> getPaymentsByCustomer(String customerId) {
        return paymentRepository.findByCustomerIdOrderByPaymentDateTimeDesc(customerId);
    }

    // READ - All payments (admin)
    public List<Payment> getAllPayments() {
        return paymentRepository.findAllByOrderByPaymentDateTimeDesc();
    }

    // READ - By order ID
    public Payment findByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId).orElse(null);
    }

    // READ - By payment ID
    public Payment findById(String id) {
        return paymentRepository.findById(id).orElse(null);
    }

    // UPDATE - Update payment status
    public boolean updatePaymentStatus(String paymentId, String newStatus) {
        Payment p = paymentRepository.findById(paymentId).orElse(null);
        if (p == null) return false;
        p.setStatus(newStatus);
        paymentRepository.save(p);
        return true;
    }

    // DELETE - Remove payment record (admin)
    public boolean deletePayment(String id) {
        if (!paymentRepository.existsById(id)) return false;
        paymentRepository.deleteById(id);
        return true;
    }

    public int countPayments() { return (int) paymentRepository.count(); }

    public double getTotalRevenue() {
        Double total = paymentRepository.getTotalRevenue();
        return total != null ? total : 0.0;
    }

    public void initSampleData() {
        if (paymentRepository.count() == 0) {
            Payment p1 = new Payment("PAY001","ORD001","USR001",1625.0,"CARD");
            p1.setStatus("COMPLETED"); p1.setTransactionRef("TXN1705000000001");
            p1.setPaymentDateTime("2025-01-10 14:35:00");
            paymentRepository.save(p1);
        }
    }
}
