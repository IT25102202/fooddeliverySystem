package com.fooddelivery.model;

import jakarta.persistence.*;

/**
 * CardPayment - extends Payment (Inheritance)
 * Component 5: Payment Management (IT25101967)
 */
@Entity
@DiscriminatorValue("CARD")
public class CardPayment extends Payment {

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "card_holder_name")
    private String cardHolderName;

    @Column(name = "card_type")
    private String cardType;

    public CardPayment() { super(); setMethod("CARD"); }

    public CardPayment(String id, String orderId, String customerId, double amount,
                       String cardNumber, String cardHolderName, String cardType) {
        super(id, orderId, customerId, amount, "CARD");
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.cardType = cardType;
    }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public String getCardHolderName() { return cardHolderName; }
    public void setCardHolderName(String cardHolderName) { this.cardHolderName = cardHolderName; }
    public String getCardType() { return cardType; }
    public void setCardType(String cardType) { this.cardType = cardType; }

    // Polymorphism - card-specific payment processing
    @Override
    public String processPayment() {
        setTransactionRef("TXN" + System.currentTimeMillis());
        setStatus("COMPLETED");
        return "Card payment processed via " + cardType + " ending in " + cardNumber;
    }

    @Override
    public String getPaymentSummary() {
        return "💳 Card | " + cardType + " ****" + cardNumber + " | LKR " + getAmount();
    }
}
