import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Receptionist extends Person {
    public List<Person> searchMember(String name) {
        return new ArrayList<>(); // Search and return member list
    }

    public boolean createBooking() {
        // Booking creation logic
        System.out.println("Booking created");
        return true;
    }
}