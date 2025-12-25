import java.util.*;

public class Stadium {
  private String name;
  private Address location;
  private int maxCapacity;

  public boolean assignMatch(Match match) {
    return match.assignStadium(this);
  }
  
  // Getters and Setters
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public Address getLocation() { return location; }
  public void setLocation(Address location) { this.location = location; }
  public int getMaxCapacity() { return maxCapacity; }
  public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }
}
