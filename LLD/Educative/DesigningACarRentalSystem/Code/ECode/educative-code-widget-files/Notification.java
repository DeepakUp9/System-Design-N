import java.util.*;

public abstract class Notification {
    private int notificationId;
    private Date createdOn;
    private String content;

    public void setContent(String c) { content = c; }
    public String getContent() { return content; }

    public abstract void sendNotification(Account account);
}
