package com.librarymanagement.notifications;

import com.librarymanagement.models.Address;

/**
 * Postal notification (R12): sends to member address.
 * Design pattern: Template Method — sendNotification() implements channel (postal).
 * SOLID: LSP — substitutable for Notification.
 */
public class PostalNotification extends Notification {
    private final Address address;

    public PostalNotification(String notificationId, String content, Address address) {
        super(notificationId, content);
        this.address = address;
    }

    @Override
    public boolean sendNotification() {
        System.out.println("[Postal to " + address + "] " + getContent());
        return true;
    }
}
