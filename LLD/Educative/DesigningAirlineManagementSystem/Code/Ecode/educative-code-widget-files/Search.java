import java.util.Date;
import java.util.List;

public interface Search {
  public List<FlightInstance> searchFlight(Airport source, Airport dest, Date arrival, Date departure);
}
