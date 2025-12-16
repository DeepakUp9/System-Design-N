import java.util.Date;
import java.util.List;

public class FlightInstance {
  private Flight flight;
  private Date departureTime;
  private String gate;
  private FlightStatus status;
  private Aircraft aircraft;
  private List<FlightSeat> seats;

  public FlightInstance(Flight flight, Date departureTime, String gate, FlightStatus status, Aircraft aircraft, List<FlightSeat> seats) {
    this.flight = flight;
    this.departureTime = departureTime;
    this.gate = gate;
    this.status = status;
    this.aircraft = aircraft;
    this.seats = seats;
  }

  public Flight getFlight() { return flight; }
  public void setFlight(Flight flight) { this.flight = flight; }

  public Date getDepartureTime() { return departureTime; }
  public void setDepartureTime(Date departureTime) { this.departureTime = departureTime; }

  public String getGate() { return gate; }
  public void setGate(String gate) { this.gate = gate; }

  public FlightStatus getStatus() { return status; }
  public void setStatus(FlightStatus status) { this.status = status; }

  public Aircraft getAircraft() { return aircraft; }
  public void setAircraft(Aircraft aircraft) { this.aircraft = aircraft; }

  public List<FlightSeat> getSeats() { return seats; }
  public void setSeats(List<FlightSeat> seats) { this.seats = seats; }
}
