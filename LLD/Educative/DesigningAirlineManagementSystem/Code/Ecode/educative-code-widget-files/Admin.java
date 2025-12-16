import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Admin extends Person {

  private static final HashMap<String, List<FlightInstance>> crewScheduleMap = new HashMap<>();

  public Admin(String name, Address address, String email, String phone, Account account) {
    super(name, address, email, phone, account);
  }

  public boolean addAircraft(Aircraft aircraft) {
    Airline.getInstance().addAircraft(aircraft);
    return true;
  }

  public boolean addFlight(Flight flight) {
    Airline.getInstance().addFlight(flight);
    return true;
  }

  public boolean cancelFlight(Flight flight) {
    flight.setInstances(null);
    return true;
  }

  public boolean assignCrew(Crew crew, FlightInstance instance) {
    String key = crew.getAccount().getUsername();
    crewScheduleMap.computeIfAbsent(key, k -> new ArrayList<>()).add(instance);
    System.out.println("Assigned " + crew.getName() + " to flight " + instance.getFlight().getFlightNo());
    return true;
  }

  public static List<FlightInstance> getScheduleForCrew(String username) {
    return crewScheduleMap.getOrDefault(username, new ArrayList<>());
  }
  
  @Override
  public List<FlightInstance> viewSchedule() {
      return null;
  }
}
