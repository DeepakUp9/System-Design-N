package com.library.models;

import com.library.exceptions.BorrowLimitExceededException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entity representing a library member who can borrow books.
 * Requirement R7: Members can borrow maximum 10 books at a time.
 * Requirement R8: Maximum borrowing period is 15 days.
 * 
 * SOLID PRINCIPLE: Single Responsibility Principle
 * - Member class manages member-specific data and borrowing rules
 * - Business logic for borrowing is delegated to services
 */
public class Member extends User {
    private final String membershipId;
    private final LocalDate membershipDate;
    private int borrowedBooksCount;
    private double outstandingFines;
    private final List<BookItem> currentlyBorrowedBooks;
    
    // Constants from requirements
    private static final int MAX_BOOKS_ALLOWED = 10; // Requirement R7
    private static final int BORROWING_PERIOD_DAYS = 15; // Requirement R8

    public Member(String name, String email, String phone, Address address) {
        super(name, email, phone, address);
        this.membershipId = generateMembershipId();
        this.membershipDate = LocalDate.now();
        this.borrowedBooksCount = 0;
        this.outstandingFines = 0.0;
        this.currentlyBorrowedBooks = new ArrayList<>();
    }

    private String generateMembershipId() {
        return "MEM-" + System.currentTimeMillis();
    }

    @Override
    public boolean canBorrowBooks() {
        return isActive() && 
               borrowedBooksCount < MAX_BOOKS_ALLOWED && 
               outstandingFines == 0;
    }

    @Override
    public boolean canAddBooks() {
        return false; // Members cannot add books
    }

    @Override
    public boolean canDeleteBooks() {
        return false; // Members cannot delete books
    }

    @Override
    public String getUserType() {
        return "MEMBER";
    }

    // Getters
    public String getMembershipId() {
        return membershipId;
    }

    public LocalDate getMembershipDate() {
        return membershipDate;
    }

    public int getBorrowedBooksCount() {
        return borrowedBooksCount;
    }

    public double getOutstandingFines() {
        return outstandingFines;
    }

    public List<BookItem> getCurrentlyBorrowedBooks() {
        return Collections.unmodifiableList(currentlyBorrowedBooks);
    }

    public static int getMaxBooksAllowed() {
        return MAX_BOOKS_ALLOWED;
    }

    public static int getBorrowingPeriodDays() {
        return BORROWING_PERIOD_DAYS;
    }

    // Business logic methods
    public boolean canBorrowMoreBooks() {
        return borrowedBooksCount < MAX_BOOKS_ALLOWED;
    }

    public int getAvailableBorrowSlots() {
        return MAX_BOOKS_ALLOWED - borrowedBooksCount;
    }

    public void incrementBorrowedCount() {
        if (borrowedBooksCount >= MAX_BOOKS_ALLOWED) {
            throw new BorrowLimitExceededException(
                "Cannot borrow more than " + MAX_BOOKS_ALLOWED + " books"
            );
        }
        borrowedBooksCount++;
    }

    public void decrementBorrowedCount() {
        if (borrowedBooksCount > 0) {
            borrowedBooksCount--;
        }
    }

    public void addBorrowedBook(BookItem bookItem) {
        if (!currentlyBorrowedBooks.contains(bookItem)) {
            currentlyBorrowedBooks.add(bookItem);
            incrementBorrowedCount();
        }
    }

    public void removeBorrowedBook(BookItem bookItem) {
        if (currentlyBorrowedBooks.remove(bookItem)) {
            decrementBorrowedCount();
        }
    }

    public void addFine(double amount) {
        if (amount > 0) {
            this.outstandingFines += amount;
        }
    }

    public void payFine(double amount) {
        if (amount > outstandingFines) {
            throw new IllegalArgumentException("Payment amount exceeds outstanding fines");
        }
        this.outstandingFines -= amount;
    }

    public void clearFines() {
        this.outstandingFines = 0.0;
    }

    public boolean hasFines() {
        return outstandingFines > 0;
    }

    public boolean hasOverdueBooks() {
        return currentlyBorrowedBooks.stream().anyMatch(BookItem::isOverdue);
    }

    @Override
    public String toString() {
        return "Member{" +
                "membershipId='" + membershipId + '\'' +
                ", name='" + name + '\'' +
                ", borrowedBooks=" + borrowedBooksCount +
                ", outstandingFines=" + outstandingFines +
                ", accountStatus=" + accountStatus +
                '}';
    }
}
