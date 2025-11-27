import java.util.*;

public class EmailNotification extends Notification {
    public void sendNotification(Person person) {
        System.out.println("Email sent to " + person.email + ": " + content);
    }
}
