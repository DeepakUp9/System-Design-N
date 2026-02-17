package com.library.models;

import com.library.enums.AccountStatus;

import java.util.Objects;
import java.util.UUID;

/**
 * Abstract base class for all users in the library system.
 * Requirement R5: Support two types of users - Librarian and Member.
 * Requirement R6: Every user has a library card.
 * 
 * DESIGN PATTERN: Template Method Pattern
 * - Defines common structure for all users
 * - Subclasses implement specific behaviors
 * 
 * SOLID PRINCIPLE: Open/Closed Principle
 * - Open for extension (can create new user types)
 * - Closed for modification (base functionality is stable)
 */
public abstract class User {
    protected final String userId;
    protected String name;
    protected String email;
    protected String phone;
    protected Address address;
    protected AccountStatus accountStatus;
    protected LibraryCard libraryCard; // Composition: User has LibraryCard

    public User(String name, String email, String phone, Address address) {
        this.userId = generateUserId();
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.accountStatus = AccountStatus.ACTIVE;
        this.libraryCard = new LibraryCard(java.time.LocalDate.now());
    }

    private String generateUserId() {
        return "U-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Abstract methods to be implemented by subclasses
    public abstract boolean canBorrowBooks();
    public abstract boolean canAddBooks();
    public abstract boolean canDeleteBooks();
    public abstract String getUserType();

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public LibraryCard getLibraryCard() {
        return libraryCard;
    }

    public boolean isActive() {
        return accountStatus == AccountStatus.ACTIVE && libraryCard.isValid();
    }

    public void activate() {
        this.accountStatus = AccountStatus.ACTIVE;
        this.libraryCard.activate();
    }

    public void suspend() {
        this.accountStatus = AccountStatus.SUSPENDED;
        this.libraryCard.suspend();
    }

    public void close() {
        this.accountStatus = AccountStatus.CLOSED;
    }

    public void blacklist() {
        this.accountStatus = AccountStatus.BLACKLISTED;
        this.libraryCard.block();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", accountStatus=" + accountStatus +
                ", userType=" + getUserType() +
                '}';
    }
}
