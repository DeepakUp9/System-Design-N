import java.util.Date;
import java.util.List;
import java.util.ArrayList;

class SMSNotification extends Notification {

    @Override
    public void sendNotification(Person person) {
        // SMS sending logic
        System.out.println("Sending SMS to: " + person.getPhone());
    }
}