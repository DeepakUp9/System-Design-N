import java.util.*;

public class Coach {
  private String name;
  private int age;
  private int country;
  private List<Team> teams;

  // Getters and Setters
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public int getAge() { return age; }
  public void setAge(int age) { this.age = age; }
  public int getCountry() { return country; }
  public void setCountry(int country) { this.country = country; }
  public List<Team> getTeams() { return teams; }
  public void setTeams(List<Team> teams) { this.teams = teams; }
}
