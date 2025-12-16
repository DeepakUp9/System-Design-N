import java.util.List;
import java.util.ArrayList;

public class FrontDeskOfficer extends Person {

  public FrontDeskOfficer(String name, Address address, String email, String phone, Account account) {
    super(name, address, email, phone, account);
  }

  public List<Itinerary> viewItinerary() {
    return new ArrayList<>(); // placeholder
  }

  public boolean createItinerary() {
    System.out.println("Creating itinerary...");
    return true;
  }

  public boolean createReservation() {
    System.out.println("Creating reservation...");
    return true;
  }

  public boolean assignSeat() {
    System.out.println("Assigning seat...");
    return true;
  }

  public boolean makePayment() {
    System.out.println("Processing payment...");
    return true;
  }
  
  @Override
  public List<FlightInstance> viewSchedule() {
      return null;
  }
}
