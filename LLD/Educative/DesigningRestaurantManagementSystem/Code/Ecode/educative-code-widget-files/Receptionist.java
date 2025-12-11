import java.util.Date;

public class Receptionist extends Person {
    public Receptionist(String name, String email, String password, Address address, String phone) {
        super(name, email, password, address, phone);
    }

    public boolean confirmAndSaveReservation(Reservation reservationDetails) {
        // Custom logic to confirm and store reservation
        return true;
    }
}
