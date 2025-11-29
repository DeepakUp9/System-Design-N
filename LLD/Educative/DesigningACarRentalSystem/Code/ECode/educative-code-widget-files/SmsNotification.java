public class SmsNotification extends Notification {
    @Override
    public void sendNotification(Account account) {
        System.out.println("SMS to " + account.getName() + ": " + getContent());
    }
}
