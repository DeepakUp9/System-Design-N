import java.util.*;

public abstract class Notification {
    public int notificationId;
    public Date createdOn;
    public String content;

    public abstract void sendNotification(Person person);
}
