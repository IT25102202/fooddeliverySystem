package com.fooddelivery.model;

import jakarta.persistence.*;

/**
 * CashPayment - extends Payment (Inheritance)
 * Component 5: Payment Management (IT25101967)
 */
@Entity
@DiscriminatorValue("CASH")
public class CashPayment extends Payment {

    @Column(name = "amount_tendered")
    private double amountTendered;

    @Column(name = "change_amount")
    private double changeAmount;

    public CashPayment() { super(); setMethod("CASH"); }

    public CashPayment(String id, String orderId, String customerId,
                       double amount, double amountTendered) {
        super(id, orderId, customerId, amount, "CASH");
        this.amountTendered = amountTendered;
        this.changeAmount = amountTendered - amount;
    }

    public double getAmountTendered() { return amountTendered; }
    public void setAmountTendered(double amountTendered) { this.amountTendered = amountTendered; }
    public double getChangeAmount() { return changeAmount; }
    public void setChangeAmount(double changeAmount) { this.changeAmount = changeAmount; }

    // Polymorphism - cash-specific payment processing
    @Override
    public String processPayment() {
        if (amountTendered >= getAmount()) {
            changeAmount = amountTendered - getAmount();
            setStatus("COMPLETED");
            setTransactionRef("CASH" + System.currentTimeMillis());
            return "Cash payment completed. Change: LKR " + changeAmount;
        } else {
            setStatus("FAILED");
            return "Insufficient cash amount";
        }
    }

    @Override
    public String getPaymentSummary() {
        return "💵 Cash | LKR " + getAmount() + " | Change: LKR " + changeAmount;
    }
}
