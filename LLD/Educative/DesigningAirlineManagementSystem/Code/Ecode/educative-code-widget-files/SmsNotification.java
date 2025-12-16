public class SmsNotification extends Notification {

  public SmsNotification(int notificationId, String content) {
    super(notificationId, content);
  }

  @Override
  public void sendNotification(Account account) {
    System.out.println("SMS to " + account.getUsername() + ": " + getContent());
  }
}
