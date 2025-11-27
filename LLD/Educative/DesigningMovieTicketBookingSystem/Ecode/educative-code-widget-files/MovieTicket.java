import java.util.*;

public class MovieTicket {
    public int ticketId;
    public Seat seat;
    public Movie movie;
    public ShowTime show;

    public MovieTicket(int ticketId, Seat seat, Movie movie, ShowTime show) {
        this.ticketId = ticketId;
        this.seat = seat;
        this.movie = movie;
        this.show = show;
    }
}
