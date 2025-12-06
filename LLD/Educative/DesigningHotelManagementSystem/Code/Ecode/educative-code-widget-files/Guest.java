import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Guest extends Person {
    private int totalRoomsCheckedIn;

    public List<RoomBooking> getBookings() {
        return new ArrayList<>(); // Return guest's bookings list
    }
    
    public boolean createBooking() {
        // Booking creation logic
        System.out.println("Booking created");
        return true;
    }

    // Getters and Setters
}