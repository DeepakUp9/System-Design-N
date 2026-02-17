package com.librarymanagement.observer;

import com.librarymanagement.models.BookItem;
import com.librarymanagement.notifications.EmailNotification;
import com.librarymanagement.users.Member;

/**
 * Class diagram design pattern: Observer pattern — concrete observer.
 * Sends email when a reserved book becomes available (R12).
 */
public class EmailReservationObserver implements ReservationObserver {
    @Override
    public void onReservationAvailable(BookItem bookItem, Member forMember) {
        String msg = "Your reserved book \"" + bookItem.getBook().getTitle() + "\" (ID: " + bookItem.getId() + ") is now available. Please collect within 24 hours.";
        new EmailNotification("N_RES_" + System.currentTimeMillis(), msg, forMember.getPerson().getEmail()).sendNotification();
    }
}
