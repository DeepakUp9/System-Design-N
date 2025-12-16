import java.util.ArrayList;
import java.util.List;

public class Airline {
  private String name;
  private String code;
  private List<Flight> flights;
  private List<Aircraft> aircrafts;
  private List<Crew> crew;

  private static Airline airline = null;

  private Airline() {
    this.name = "Default Airline";
    this.code = "DEF";
    this.flights = new ArrayList<>();
    this.aircrafts = new ArrayList<>();
    this.crew = new ArrayList<>();
  }

  public static Airline getInstance() {
    if (airline == null) {
      airline = new Airline();
    }
    return airline;
  }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getCode() { return code; }
  public void setCode(String code) { this.code = code; }

  public List<Flight> getFlights() { return flights; }
  public List<Aircraft> getAircrafts() { return aircrafts; }
  public List<Crew> getCrew() { return crew; }

  public void addFlight(Flight flight) {
    flights.add(flight);
  }

  public void addAircraft(Aircraft aircraft) {
    aircrafts.add(aircraft);
  }

  public void addCrew(Crew crewMember) {
    crew.add(crewMember);
  }
}
