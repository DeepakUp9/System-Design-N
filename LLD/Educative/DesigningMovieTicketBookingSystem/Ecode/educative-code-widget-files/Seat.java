import java.util.*;

public abstract class Seat {
    public String seatNo;
    public SeatStatus status;

    public boolean isAvailable() {
        return status == SeatStatus.AVAILABLE;
    }

    public abstract void setSeat();
    public abstract void setRate();
}
