import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class FlightReservation {
  private String reservationNumber;
  private FlightInstance flight;
  private HashMap<Passenger, FlightSeat> seatMap;
  private ReservationStatus status;
  private Date creationDate;

  public FlightReservation(String reservationNumber, FlightInstance flight,
                           HashMap<Passenger, FlightSeat> seatMap, ReservationStatus status, Date creationDate) {
    this.reservationNumber = reservationNumber;
    this.flight = flight;
    this.seatMap = seatMap;
    this.status = status;
    this.creationDate = creationDate;
  }

  public String getReservationNumber() { return reservationNumber; }
  public void setReservationNumber(String reservationNumber) { this.reservationNumber = reservationNumber; }

  public FlightInstance getFlight() { return flight; }
  public void setFlight(FlightInstance flight) { this.flight = flight; }

  public HashMap<Passenger, FlightSeat> getSeatMap() { return seatMap; }
  public void setSeatMap(HashMap<Passenger, FlightSeat> seatMap) { this.seatMap = seatMap; }

  public ReservationStatus getStatus() { return status; }
  public void setStatus(ReservationStatus status) { this.status = status; }

  public Date getCreationDate() { return creationDate; }
  public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }

  public static FlightReservation fetchReservationDetails(String reservationNumber) {
    System.out.println("Fetching reservation for: " + reservationNumber);
    return null; // In real systems, this would query a DB or repository
  }

  public List<Passenger> getPassengers() {
    return new ArrayList<>(seatMap.keySet());
  }
}
