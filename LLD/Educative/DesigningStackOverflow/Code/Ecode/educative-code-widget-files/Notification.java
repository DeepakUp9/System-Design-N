import java.util.*;

public class Notification {
    private int notificationId;
    private Date createdOn;
    private String content;

    public boolean sendNotification(User user) {
        System.out.println("Notification sent to " + user.getName() + ": " + content);
        return true;
    }
    
    public int getNotificationId() { return notificationId; }
    public void setNotificationId(int notificationId) { this.notificationId = notificationId; }
    
    public Date getCreatedOn() { return createdOn; }
    public void setCreatedOn(Date createdOn) { this.createdOn = createdOn; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

}
