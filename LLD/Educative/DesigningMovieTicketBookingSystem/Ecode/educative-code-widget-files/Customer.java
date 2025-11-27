import java.util.*;

public class Customer extends Person {
    public List<Booking> bookings = new ArrayList<>();

    public boolean createBooking(Booking booking) {
        bookings.add(booking);
        System.out.println("Booking created by customer: " + name);
        return true;
    }
    public boolean updateBooking(Booking booking) {
        System.out.println("Booking updated by customer: " + name);
        return true;
    }
    public boolean deleteBooking(Booking booking) {
        bookings.remove(booking);
        System.out.println("Booking deleted by customer: " + name);
        return true;
    }
}
