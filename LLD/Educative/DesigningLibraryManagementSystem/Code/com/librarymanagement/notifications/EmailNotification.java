package Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.notifications;

public class EmailNotification extends Notification {
    private String email;
    
    public EmailNotification(String notificationId, String content, String email) {
        super(notificationId, content);
        this.email = email;
    }
    
    @Override
    public boolean sendNotification() {
        System.out.println("Email notification to " + email + ": " + getContent());
        return true;
    }
}
