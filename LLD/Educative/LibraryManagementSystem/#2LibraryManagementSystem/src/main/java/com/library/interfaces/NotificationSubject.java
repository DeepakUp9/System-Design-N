package com.library.interfaces;

/**
 * Subject interface for notification system.
 * 
 * DESIGN PATTERN: Observer Pattern
 * - Defines the interface for subjects that maintain a list of observers
 * - Provides methods to attach, detach, and notify observers
 */
public interface NotificationSubject {
    /**
     * Attach an observer to receive notifications.
     * 
     * @param observer The observer to attach
     */
    void attach(NotificationObserver observer);
    
    /**
     * Detach an observer from receiving notifications.
     * 
     * @param observer The observer to detach
     */
    void detach(NotificationObserver observer);
    
    /**
     * Notify all attached observers.
     * 
     * @param member The member to be notified
     * @param subject The notification subject
     * @param message The notification message
     */
    void notifyObservers(com.library.models.Member member, String subject, String message);
}
