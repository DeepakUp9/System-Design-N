import java.util.Date;

public  class Notification {
  private int notificationId;
  private Date createdOn;
  private String content;

  public Notification(int notificationId, String content) {
    this.notificationId = notificationId;
    this.content = content;
    this.createdOn = new Date();
  }

  public boolean sendNotification(Account account){
    System.out.println("Sending Notification to: " + account.getEmail());
    return true;
  }
}