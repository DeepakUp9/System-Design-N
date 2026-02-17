package com.librarymanagement.observer;

import com.librarymanagement.models.BookItem;
import com.librarymanagement.users.Member;

/**
 * Class diagram design pattern: Observer pattern.
 * When a reserved book becomes available, observers are notified.
 * Reference: Class Diagram for the Library Management System.md — "Observer pattern: Members who reserve
 * can be registered as observers; when BookItem status changes to available, notify observers."
 */
@FunctionalInterface
public interface ReservationObserver {
    /**
     * Called when a book that was reserved becomes available for the given member.
     */
    void onReservationAvailable(BookItem bookItem, Member forMember);
}
