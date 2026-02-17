package com.librarymanagement.users;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.librarymanagement.models.BookItem;
import com.librarymanagement.models.LibraryCard;
import com.librarymanagement.models.Person;

/**
 * Member (R5, R7): can borrow max 10 books, reserve, renew, return, pay fines.
 * SOLID: SRP — member state and borrowing limits; LSP — substitutable for User.
 * R7: max 10 books enforced in canBorrowMore() and Library.issueBook().
 */
public class Member extends User {
    private static final int MAX_BOOKS_ALLOWED = 10;

    private final Date dateOfMembership;
    private final List<BookItem> booksBorrowed;
    private double finesDue;

    public Member(String id, String password, Person person, LibraryCard card) {
        super(id, password, person, card);
        this.dateOfMembership = new Date();
        this.booksBorrowed = new ArrayList<>();
        this.finesDue = 0.0;
    }

    public int getTotalBooksCheckedOut() {
        return booksBorrowed.size();
    }

    public boolean canBorrowMore() {
        return getTotalBooksCheckedOut() < MAX_BOOKS_ALLOWED && getFinesDue() == 0
                && getStatus() == com.librarymanagement.enums.AccountStatus.ACTIVE;
    }

    public void addBorrowedBook(BookItem item) {
        if (!booksBorrowed.contains(item)) {
            booksBorrowed.add(item);
        }
    }

    public void removeBorrowedBook(BookItem item) {
        booksBorrowed.remove(item);
    }

    public void addFine(double amount) {
        this.finesDue += amount;
    }

    public void payFine(double amount) {
        if (amount >= finesDue) {
            finesDue = 0;
        } else {
            finesDue -= amount;
        }
    }

    public Date getDateOfMembership() { return dateOfMembership; }
    public List<BookItem> getBooksBorrowed() { return Collections.unmodifiableList(booksBorrowed); }
    public double getFinesDue() { return finesDue; }
}
