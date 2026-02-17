package com.library.interfaces;

import com.library.models.Member;

/**
 * Observer interface for notification system.
 * Requirement R12: Notify members for overdue books and reservation availability.
 * 
 * DESIGN PATTERN: Observer Pattern
 * - Defines the interface for observers who want to be notified of events
 * - Enables loose coupling between notification sources and receivers
 * - Supports multiple notification channels (Email, SMS, etc.)
 */
public interface NotificationObserver {
    /**
     * Receive and process a notification.
     * 
     * @param member The member to be notified
     * @param subject The notification subject
     * @param message The notification message
     */
    void update(Member member, String subject, String message);
    
    /**
     * Get the notification channel type.
     * 
     * @return The channel identifier (e.g., "EMAIL", "SMS")
     */
    String getChannelType();
}
