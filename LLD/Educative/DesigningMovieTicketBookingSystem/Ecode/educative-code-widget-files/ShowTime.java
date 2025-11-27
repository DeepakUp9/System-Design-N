import java.util.*;

public class ShowTime {
    public int showId;
    public Date startTime;
    public Date date;
    public int duration;
    public List<Seat> seats = new ArrayList<>();

    public ShowTime(int showId, Date startTime, Date date, int duration, List<Seat> seats) {
        this.showId = showId;
        this.startTime = startTime;
        this.date = date;
        this.duration = duration;
        this.seats = seats;
    }

    public void showAvailableSeats() {
        System.out.println("Available seats:");
        for (Seat s : seats) {
            if (s.isAvailable()) {
                System.out.println("Seat: " + s.seatNo);
            }
        }
    }
}