package Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.notifications;

import java.util.Date;

public abstract class Notification {
    private String notificationId;
    private String content;
    private Date created;
    
    public Notification(String notificationId, String content) {
        this.notificationId = notificationId;
        this.content = content;
        this.created = new Date();
    }
    
    public abstract boolean sendNotification();
    
    // Getters
    public String getNotificationId() { return notificationId; }
    public String getContent() { return content; }
    public Date getCreated() { return created; }
}
