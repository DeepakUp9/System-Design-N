package com.librarymanagement.services;

import java.util.Date;

import com.librarymanagement.enums.PaymentMethod;

/**
 * Class diagram: FineTransaction — record of fine and payment method (check, cash, credit card).
 * Uses Decorator pattern for fine calculation (fine keeps adding per days).
 */
public class FineTransaction {
    private final String transactionId;
    private final String memberId;
    private final String bookItemId;
    private final double amount;
    private final int overdueDays;
    private final Date createdAt;
    private PaymentMethod paymentMethod;
    private boolean paid;

    public FineTransaction(String memberId, String bookItemId, int overdueDays) {
        this.transactionId = "FT" + System.currentTimeMillis();
        this.memberId = memberId;
        this.bookItemId = bookItemId;
        this.overdueDays = overdueDays;
        FineCalculation base = new BaseFineCalculation(overdueDays);
        FineCalculation withLate = new LateFeeDecorator(base, overdueDays);
        this.amount = withLate.getAmount();
        this.createdAt = new Date();
        this.paid = false;
    }

    public void pay(PaymentMethod method) {
        this.paymentMethod = method;
        this.paid = true;
    }

    public String getTransactionId() { return transactionId; }
    public String getMemberId() { return memberId; }
    public String getBookItemId() { return bookItemId; }
    public double getAmount() { return amount; }
    public int getOverdueDays() { return overdueDays; }
    public Date getCreatedAt() { return createdAt; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public boolean isPaid() { return paid; }
}
