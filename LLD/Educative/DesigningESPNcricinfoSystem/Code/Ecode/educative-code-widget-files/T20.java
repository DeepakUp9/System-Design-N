import java.util.*;

public class T20 extends Match {
  public boolean assignStadium(Stadium stadium) {
    this.stadium = stadium;
    return true;
  }

  public boolean assignUmpire(Umpire umpire) {
    umpires.put(umpire, UmpireType.FIELD); // default type
    return true;
  }
}