import java.util.*;

public class TicketAgent extends Person {
    public boolean createBooking(Booking booking) {
        System.out.println("Booking created by ticket agent: " + name);
        return true;
    }
}
