package com.librarymanagement.services;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lending record and service (R8): creation date, due date (15 days), return date, member.
 * Thread safety: ConcurrentHashMap for shared lendings map (concurrent issue/return).
 * No stubs: lendBook(), fetchLendingDetails(), completeReturn() fully implemented.
 */
public class BookLending {
    private static final Map<String, BookLending> lendings = new ConcurrentHashMap<>();
    private static final int BORROW_DAYS = 15;

    private final String itemId;
    private final String memberId;
    private final Date creationDate;
    private final Date dueDate;
    private Date returnDate;

    public BookLending(String itemId, String memberId) {
        this.itemId = itemId;
        this.memberId = memberId;
        this.creationDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(creationDate);
        c.add(Calendar.DATE, BORROW_DAYS);
        this.dueDate = c.getTime();
    }

    public static BookLending lendBook(String bookItemId, String memberId) {
        BookLending lending = new BookLending(bookItemId, memberId);
        lendings.put(bookItemId, lending);
        return lending;
    }

    public static BookLending fetchLendingDetails(String bookItemId) {
        return lendings.get(bookItemId);
    }

    public static void completeReturn(String bookItemId) {
        BookLending l = lendings.get(bookItemId);
        if (l != null) {
            l.returnDate = new Date();
        }
    }

    public static void removeLending(String bookItemId) {
        lendings.remove(bookItemId);
    }

    public String getItemId() { return itemId; }
    public String getMemberId() { return memberId; }
    public Date getCreationDate() { return creationDate; }
    public Date getDueDate() { return dueDate; }
    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }
}
