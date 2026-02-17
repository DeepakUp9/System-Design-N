package com.librarymanagement.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.librarymanagement.enums.BookStatus;
import com.librarymanagement.enums.TransactionType;
import com.librarymanagement.models.Address;
import com.librarymanagement.models.BookItem;
import com.librarymanagement.models.TransactionLog;
import com.librarymanagement.notifications.EmailNotification;
import com.librarymanagement.observer.EmailReservationObserver;
import com.librarymanagement.observer.ReservationObserver;
import com.librarymanagement.search.Catalog;
import com.librarymanagement.services.BookLending;
import com.librarymanagement.services.BookReservation;
import com.librarymanagement.services.FineTransaction;
import com.librarymanagement.users.Librarian;
import com.librarymanagement.users.Member;

/**
 * Library: Singleton (thread-safe). Central coordinator for issue/return/reserve/renew (R1, R10).
 * Design pattern: Singleton with double-check locking for thread-safe shared resource.
 */
public class Library {
    private static volatile Library instance;
    private static final Object LOCK = new Object();

    private final String name;
    private final Address address;
    private final Catalog catalog;
    /** Class diagram: Composition — Library is composed of BookItem (1 -- 1..*). */
    private final List<BookItem> bookItems;
    private final List<TransactionLog> transactionHistory;
    /** Class diagram: Observer pattern — observers notified when reserved book becomes available. */
    private final List<ReservationObserver> reservationObservers;

    private Library(String name, Address address) {
        this.name = name;
        this.address = address;
        this.catalog = new Catalog();
        this.bookItems = Collections.synchronizedList(new ArrayList<>());
        this.transactionHistory = Collections.synchronizedList(new ArrayList<>());
        this.reservationObservers = Collections.synchronizedList(new ArrayList<>());
        reservationObservers.add(new EmailReservationObserver());
    }

    /** Class diagram: Composition — add BookItem to library (1 -- 1..*). Also adds Book to Catalog (aggregation). */
    public void addBookItem(BookItem bookItem) {
        if (bookItem != null) {
            bookItems.add(bookItem);
            catalog.addBookIfAbsent(bookItem.getBook());
        }
    }

    public BookItem getBookItemById(String id) {
        for (BookItem bi : bookItems) {
            if (bi.getId().equals(id)) return bi;
        }
        return null;
    }

    /** Register an observer for reservation-available events (Observer pattern). */
    public void addReservationObserver(ReservationObserver observer) {
        if (observer != null) reservationObservers.add(observer);
    }

    /** Thread-safe Singleton (double-check locking). */
    public static Library getInstance(String name, Address address) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new Library(name, address);
                }
            }
        }
        return instance;
    }

    /** For tests/Driver when instance already set. */
    public static Library getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Library not initialized. Call getInstance(name, address) first.");
        }
        return instance;
    }

    private void recordTransaction(TransactionType type, Member member, BookItem bookItem, Librarian processedBy) {
        transactionHistory.add(new TransactionLog(type, member, bookItem, processedBy));
    }

    /** Issue book to member (R7: max 10, R8: 15 days). Returns message for member. */
    public String issueBook(Librarian librarian, Member member, BookItem bookItem) {
        if (member.getTotalBooksCheckedOut() >= 10) {
            return "Quota reached. You cannot borrow more than 10 books.";
        }
        if (member.getFinesDue() > 0) {
            return "Please clear your outstanding fines before borrowing.";
        }
        if (member.getStatus() != com.librarymanagement.enums.AccountStatus.ACTIVE) {
            return "Your account is not active.";
        }
        if (bookItem.getStatus() != BookStatus.AVAILABLE && !(bookItem.getStatus() == BookStatus.RESERVED && bookItem.getReservedBy() == member)) {
            return "Book not available.";
        }
        if (!member.getBooksBorrowed().contains(bookItem) && bookItem.getStatus() == BookStatus.RESERVED && bookItem.getReservedBy() != member) {
            return "Book is reserved by another member.";
        }
        if (!bookItem.checkout(member)) {
            return "Checkout failed.";
        }
        BookLending.lendBook(bookItem.getId(), member.getId());
        member.addBorrowedBook(bookItem);
        recordTransaction(TransactionType.BORROW, member, bookItem, librarian);
        return "Book issued successfully. Due date: " + bookItem.getDueDate();
    }

    /** Return book. Calculates fine if overdue; notifies reserver if any (R12). */
    public String returnBook(Librarian librarian, Member member, BookItem bookItem) {
        if (!member.getBooksBorrowed().contains(bookItem)) {
            return "This book was not checked out by you.";
        }
        int overdueDays = bookItem.getOverdueDays();
        if (overdueDays > 0) {
            com.librarymanagement.services.FineTransaction ft = new com.librarymanagement.services.FineTransaction(member.getId(), bookItem.getId(), overdueDays);
            member.addFine(ft.getAmount());
        }
        Member reserver = bookItem.getReservedBy();
        bookItem.returnBook();
        BookLending.completeReturn(bookItem.getId());
        BookLending.removeLending(bookItem.getId());
        member.removeBorrowedBook(bookItem);
        recordTransaction(TransactionType.RETURN, member, bookItem, librarian);
        if (reserver != null) {
            for (ReservationObserver o : reservationObservers) {
                o.onReservationAvailable(bookItem, reserver);
            }
        }
        if (overdueDays > 0) {
            FineTransaction ft = new FineTransaction(member.getId(), bookItem.getId(), overdueDays);
            return "Book returned. Fine applied: $" + String.format("%.2f", ft.getAmount()) + " for " + overdueDays + " day(s) overdue.";
        }
        return "Book returned successfully.";
    }

    /** Reserve book when unavailable (R9, R13). */
    public String reserveBook(Librarian librarian, Member member, BookItem bookItem) {
        if (bookItem.getStatus() != BookStatus.LOANED) {
            return "Book is available; you can issue it directly.";
        }
        if (bookItem.getReservedBy() != null) {
            return "Book is already reserved by another member.";
        }
        if (!bookItem.reserve(member)) {
            return "Reservation failed.";
        }
        BookReservation.createReservation(bookItem.getId(), member.getId());
        recordTransaction(TransactionType.RESERVE, member, bookItem, librarian);
        return "Book reserved. You will be notified when it is available.";
    }

    /** Renew book (R11: max 2 renewals, not reserved, not overdue, no fines). */
    public String renewBook(Librarian librarian, Member member, BookItem bookItem) {
        if (!member.getBooksBorrowed().contains(bookItem)) {
            return "You have not checked out this book.";
        }
        if (bookItem.getReservedBy() != null) {
            return "Cannot renew: book is reserved by another member.";
        }
        if (bookItem.getRenewalCount() >= 2) {
            return "Maximum renewals (2) reached.";
        }
        if (bookItem.isOverdue()) {
            return "Cannot renew an overdue book. Please return it and pay the fine.";
        }
        if (member.getFinesDue() > 0) {
            return "Clear your outstanding fines before renewing.";
        }
        if (!bookItem.renew()) {
            return "Renewal failed.";
        }
        recordTransaction(TransactionType.RENEW, member, bookItem, librarian);
        return "Book renewed. New due date: " + bookItem.getDueDate();
    }

    public void sendOverdueNotification(Member member, BookItem bookItem) {
        int days = bookItem.getOverdueDays();
        FineTransaction ft = new FineTransaction(member.getId(), bookItem.getId(), days);
        String msg = "Your book \"" + bookItem.getBook().getTitle() + "\" was due on " + bookItem.getDueDate() + ". Please return it. Fine: $" + String.format("%.2f", ft.getAmount());
        new EmailNotification("N_OV_" + System.currentTimeMillis(), msg, member.getPerson().getEmail()).sendNotification();
    }

    public String getName() { return name; }
    public Address getAddress() { return address; }
    public Catalog getCatalog() { return catalog; }
    public List<TransactionLog> getTransactionHistory() { return new ArrayList<>(transactionHistory); }
}
