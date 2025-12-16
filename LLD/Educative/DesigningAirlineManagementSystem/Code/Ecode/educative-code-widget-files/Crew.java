import java.util.List;
import java.util.ArrayList;

public class Crew extends Person {

  public Crew(String name, Address address, String email, String phone, Account account) {
    super(name, address, email, phone, account);
  }

  @Override
  public List<FlightInstance> viewSchedule() {
      return Admin.getScheduleForCrew(this.getAccount().getUsername());
  }

}
