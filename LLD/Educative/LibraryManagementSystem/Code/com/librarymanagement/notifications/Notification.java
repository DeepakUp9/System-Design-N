package com.librarymanagement.notifications;

import java.util.Date;

/**
 * Abstract Notification (R12): overdue reminder, reservation available.
 * Template Method pattern: sendNotification() implemented by Email/Postal subclasses.
 */
public abstract class Notification {
    private final String notificationId;
    private final String content;
    private final Date created;

    public Notification(String notificationId, String content) {
        this.notificationId = notificationId;
        this.content = content;
        this.created = new Date();
    }

    public abstract boolean sendNotification();

    public String getNotificationId() { return notificationId; }
    public String getContent() { return content; }
    public Date getCreated() { return created; }
}
