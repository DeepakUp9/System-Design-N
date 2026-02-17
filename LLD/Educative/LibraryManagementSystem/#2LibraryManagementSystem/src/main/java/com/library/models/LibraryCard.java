package com.library.models;

import com.library.enums.CardStatus;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity representing a library card issued to users.
 * Requirement R6: Every user must have a library card with unique card number.
 */
public class LibraryCard {
    private final String cardNumber;
    private final LocalDate issuedAt;
    private LocalDate expiryDate;
    private CardStatus status;
    private String barcode;

    public LibraryCard(LocalDate issuedAt) {
        this.cardNumber = generateCardNumber();
        this.issuedAt = issuedAt;
        this.expiryDate = issuedAt.plusYears(1);
        this.status = CardStatus.ACTIVE;
        this.barcode = generateBarcode();
    }

    private String generateCardNumber() {
        return "LC-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateBarcode() {
        return "BAR-" + cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public LocalDate getIssuedAt() {
        return issuedAt;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public CardStatus getStatus() {
        return status;
    }

    public void setStatus(CardStatus status) {
        this.status = status;
    }

    public String getBarcode() {
        return barcode;
    }

    public boolean isValid() {
        return status == CardStatus.ACTIVE && LocalDate.now().isBefore(expiryDate);
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    public void renew() {
        if (!isExpired()) {
            this.expiryDate = LocalDate.now().plusYears(1);
        }
    }

    public void activate() {
        this.status = CardStatus.ACTIVE;
    }

    public void suspend() {
        this.status = CardStatus.SUSPENDED;
    }

    public void block() {
        this.status = CardStatus.BLOCKED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LibraryCard that = (LibraryCard) o;
        return Objects.equals(cardNumber, that.cardNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber);
    }

    @Override
    public String toString() {
        return "LibraryCard{" +
                "cardNumber='" + cardNumber + '\'' +
                ", status=" + status +
                ", expiryDate=" + expiryDate +
                '}';
    }
}
