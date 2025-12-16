import java.util.List;
import java.util.ArrayList;

public class Customer extends Person {
  private int customerId;

  public Customer(int customerId, String name, Address address, String email, String phone, Account account) {
    super(name, address, email, phone, account);
    this.customerId = customerId;
  }

  public int getCustomerId() { return customerId; }
  public void setCustomerId(int customerId) { this.customerId = customerId; }

  public List<Itinerary> viewItinerary() {
    return new ArrayList<>();
  }

  public boolean createItinerary() {
    System.out.println("Customer is creating itinerary...");
    return true;
  }

  public boolean createReservation() {
    System.out.println("Customer is creating reservation...");
    return true;
  }

  public boolean assignSeat() {
    System.out.println("Customer is assigning seat...");
    return true;
  }

  public boolean makePayment(Payment payment) {
      System.out.println("Customer initiating payment...");
      return payment.makePayment(this.getAccount());
  }

  @Override
  public List<FlightInstance> viewSchedule() {
      return null;
  }

}
