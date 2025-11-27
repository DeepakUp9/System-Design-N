import java.util.*;

public class PhoneNotification extends Notification {
    public void sendNotification(Person person) {
        System.out.println("SMS sent to " + person.phone + ": " + content);
    }
}
