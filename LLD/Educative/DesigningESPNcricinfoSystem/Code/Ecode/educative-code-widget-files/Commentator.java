import java.util.*;

public class Commentator {
  private String name;

  public boolean assignMatch(Match match) {
    return match.assignCommentator(this);
  }
  
  // Getter and Setter
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
}