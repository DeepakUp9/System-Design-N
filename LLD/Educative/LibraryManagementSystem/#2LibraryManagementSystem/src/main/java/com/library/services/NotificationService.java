package com.library.services;

import com.library.interfaces.NotificationObserver;
import com.library.interfaces.NotificationSubject;
import com.library.models.BookItem;
import com.library.models.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing and sending notifications.
 * Requirement R12: Notify members for overdue books and reservation availability.
 * 
 * DESIGN PATTERN: Observer Pattern - Concrete Subject
 * - Implements NotificationSubject interface
 * - Manages list of observers and notifies them of events
 * - Decouples notification logic from business logic
 * 
 * SOLID PRINCIPLE: Dependency Inversion Principle
 * - Depends on NotificationObserver abstraction, not concrete implementations
 * - Easy to add new notification channels without modifying this class
 */
public class NotificationService implements NotificationSubject {
    private final List<NotificationObserver> observers;
    
    public NotificationService() {
        this.observers = new ArrayList<>();
    }
    
    @Override
    public void attach(NotificationObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("✅ Notification channel '" + observer.getChannelType() + "' attached");
        }
    }
    
    @Override
    public void detach(NotificationObserver observer) {
        if (observers.remove(observer)) {
            System.out.println("❌ Notification channel '" + observer.getChannelType() + "' detached");
        }
    }
    
    @Override
    public void notifyObservers(Member member, String subject, String message) {
        for (NotificationObserver observer : observers) {
            observer.update(member, subject, message);
        }
    }
    
    /**
     * Send overdue notification to member.
     * Requirement R12: Notify if book is not returned by due date.
     * 
     * @param member The member with overdue book
     * @param bookItem The overdue book item
     */
    public void sendOverdueNotification(Member member, BookItem bookItem) {
        String subject = "Overdue Book Alert";
        String message = String.format(
            "Dear %s,\n\n" +
            "The book \"%s\" (ID: %s) was due on %s.\n" +
            "It is now %d day(s) overdue.\n\n" +
            "Please return it as soon as possible to avoid additional fines.\n" +
            "Current fine: $%.2f\n\n" +
            "Thank you,\nLibrary Management System",
            member.getName(),
            bookItem.getBook().getTitle(),
            bookItem.getBookItemId(),
            bookItem.getDueDate(),
            bookItem.getOverdueDays(),
            bookItem.getOverdueDays() * 1.0
        );
        
        notifyObservers(member, subject, message);
    }
    
    /**
     * Send reservation available notification.
     * Requirement R12: Notify when reserved book becomes available.
     * 
     * @param member The member whose reservation is available
     * @param bookItem The available book item
     */
    public void sendReservationAvailableNotification(Member member, BookItem bookItem) {
        String subject = "Reserved Book Available";
        String message = String.format(
            "Good news, %s!\n\n" +
            "The book \"%s\" you reserved is now available.\n\n" +
            "Book Details:\n" +
            "- Title: %s\n" +
            "- Author(s): %s\n" +
            "- Location: %s\n\n" +
            "Please collect it within 24 hours, or your reservation will expire.\n\n" +
            "Thank you,\nLibrary Management System",
            member.getName(),
            bookItem.getBook().getTitle(),
            bookItem.getBook().getTitle(),
            bookItem.getBook().getAuthorNames(),
            bookItem.getFullLocation()
        );
        
        notifyObservers(member, subject, message);
    }
    
    /**
     * Send reservation cancelled notification.
     * 
     * @param member The member whose reservation was cancelled
     * @param bookItem The book item
     */
    public void sendReservationCancelledNotification(Member member, BookItem bookItem) {
        String subject = "Reservation Cancelled";
        String message = String.format(
            "Dear %s,\n\n" +
            "Your reservation for \"%s\" has been cancelled.\n\n" +
            "If you did not cancel this reservation, please contact the library.\n\n" +
            "Thank you,\nLibrary Management System",
            member.getName(),
            bookItem.getBook().getTitle()
        );
        
        notifyObservers(member, subject, message);
    }
    
    /**
     * Send due date reminder notification.
     * 
     * @param member The member to remind
     * @param bookItem The book item due soon
     * @param daysUntilDue Days until the book is due
     */
    public void sendDueDateReminder(Member member, BookItem bookItem, long daysUntilDue) {
        String subject = "Book Due Date Reminder";
        String message = String.format(
            "Dear %s,\n\n" +
            "This is a friendly reminder that the book \"%s\" is due in %d day(s).\n\n" +
            "Due Date: %s\n" +
            "Location: %s\n\n" +
            "Please return it on time to avoid fines.\n\n" +
            "Thank you,\nLibrary Management System",
            member.getName(),
            bookItem.getBook().getTitle(),
            daysUntilDue,
            bookItem.getDueDate(),
            bookItem.getFullLocation()
        );
        
        notifyObservers(member, subject, message);
    }
    
    /**
     * Send fine payment notification.
     * 
     * @param member The member who paid the fine
     * @param amount The amount paid
     */
    public void sendFinePaymentNotification(Member member, double amount) {
        String subject = "Fine Payment Received";
        String message = String.format(
            "Dear %s,\n\n" +
            "We have received your fine payment of $%.2f.\n\n" +
            "Remaining balance: $%.2f\n\n" +
            "Thank you,\nLibrary Management System",
            member.getName(),
            amount,
            member.getOutstandingFines()
        );
        
        notifyObservers(member, subject, message);
    }
    
    public int getObserverCount() {
        return observers.size();
    }
}
