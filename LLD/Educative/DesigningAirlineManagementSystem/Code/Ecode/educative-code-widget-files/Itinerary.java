import java.util.Date;
import java.util.List;

public class Itinerary {
  private Airport startingAirport;
  private Airport finalAirport;
  private Date creationDate;
  private List<FlightReservation> reservations;
  private List<Passenger> passengers;

  public Itinerary(Airport startingAirport, Airport finalAirport, Date creationDate,
                   List<FlightReservation> reservations, List<Passenger> passengers) {
    this.startingAirport = startingAirport;
    this.finalAirport = finalAirport;
    this.creationDate = creationDate;
    this.reservations = reservations;
    this.passengers = passengers;
  }

  public Airport getStartingAirport() { return startingAirport; }
  public void setStartingAirport(Airport startingAirport) { this.startingAirport = startingAirport; }

  public Airport getFinalAirport() { return finalAirport; }
  public void setFinalAirport(Airport finalAirport) { this.finalAirport = finalAirport; }

  public Date getCreationDate() { return creationDate; }
  public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }

  public List<FlightReservation> getReservations() { return reservations; }
  public void setReservations(List<FlightReservation> reservations) { this.reservations = reservations; }

  public List<Passenger> getPassengers() { return passengers; }
  public void setPassengers(List<Passenger> passengers) { this.passengers = passengers; }

  public boolean makeReservation() {
    System.out.println("Making reservations for passengers...");
    return true;
  }

  public boolean makePayment() {
    System.out.println("Processing payment for itinerary...");
    return true;
  }
}
