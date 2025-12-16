public class EmailNotification extends Notification {

  public EmailNotification(int notificationId, String content) {
    super(notificationId, content);
  }

  @Override
  public void sendNotification(Account account) {
    System.out.println("Email to " + account.getUsername() + ": " + getContent());
  }
}
