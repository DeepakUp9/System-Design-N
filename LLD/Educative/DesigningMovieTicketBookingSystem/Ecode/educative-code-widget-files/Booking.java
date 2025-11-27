import java.util.*;

public class Booking {
    public int bookingId;
    public int amount;
    public int totalSeats;
    public Date createdOn;
    public BookingStatus status;
    public Payment payment;
    public List<MovieTicket> tickets;
    public List<Seat> seats;

    public Booking(int bookingId, int amount, int totalSeats, Date createdOn, BookingStatus status, Payment payment, List<MovieTicket> tickets, List<Seat> seats) {
        this.bookingId = bookingId;
        this.amount = amount;
        this.totalSeats = totalSeats;
        this.createdOn = createdOn;
        this.status = status;
        this.payment = payment;
        this.tickets = tickets;
        this.seats = seats;
    }
}
