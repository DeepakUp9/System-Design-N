import java.util.*;

public class Umpire {
  private String name;
  private int age;
  private int country;

  public boolean assignMatch(Match match) {
    return match.assignUmpire(this);
  }
  
  // Getters and Setters
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public int getAge() { return age; }
  public void setAge(int age) { this.age = age; }
  public int getCountry() { return country; }
  public void setCountry(int country) { this.country = country; }
}