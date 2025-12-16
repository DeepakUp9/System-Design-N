import java.util.Date;

public abstract class Notification {
  private int notificationId;
  private Date createdOn;
  private String content;

  public Notification(int notificationId, String content) {
    this.notificationId = notificationId;
    this.content = content;
    this.createdOn = new Date();
  }

  public int getNotificationId() { return notificationId; }
  public Date getCreatedOn() { return createdOn; }
  public String getContent() { return content; }

  public abstract void sendNotification(Account account);
}
