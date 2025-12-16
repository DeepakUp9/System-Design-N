import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class SearchCatalog implements Search {
  private HashMap<String, List<FlightInstance>> flights;

  public SearchCatalog() {
    this.flights = new HashMap<>();
  }

  private String generateKey(Airport source, Airport dest, Date arrival, Date departure) {
    return source.getCode() + "-" + dest.getCode() + "-" + arrival.getTime() + "-" + departure.getTime();
  }

  public void addFlight(Airport source, Airport dest, Date arrival, Date departure, FlightInstance instance) {
    String key = generateKey(source, dest, arrival, departure);
    flights.putIfAbsent(key, new ArrayList<>());
    flights.get(key).add(instance);
  }

  @Override
  public List<FlightInstance> searchFlight(Airport source, Airport dest, Date arrival, Date departure) {
    String key = generateKey(source, dest, arrival, departure);
    return flights.getOrDefault(key, new ArrayList<>());
  }
}
