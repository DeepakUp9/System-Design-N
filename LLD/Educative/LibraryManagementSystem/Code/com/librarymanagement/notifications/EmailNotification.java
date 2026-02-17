package com.librarymanagement.notifications;

/**
 * Email notification (R12): sends to member email.
 * Design pattern: Template Method — sendNotification() implements channel (email).
 * SOLID: LSP — substitutable for Notification.
 */
public class EmailNotification extends Notification {
    private final String email;

    public EmailNotification(String notificationId, String content, String email) {
        super(notificationId, content);
        this.email = email;
    }

    @Override
    public boolean sendNotification() {
        System.out.println("[Email to " + email + "] " + getContent());
        return true;
    }
}
