public class EmailNotification extends Notification {
    @Override
    public void sendNotification(Account account) {
        System.out.println("Email to " + account.getName() + ": " + getContent());
    }
}
