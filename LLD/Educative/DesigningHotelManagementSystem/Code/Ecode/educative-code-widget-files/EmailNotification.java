import java.util.Date;
import java.util.List;
import java.util.ArrayList;

class EmailNotification extends Notification {

    @Override
    public void sendNotification(Person person) {
        // Email sending logic
        System.out.println("Sending email to: " + person.getEmail());
    }
}