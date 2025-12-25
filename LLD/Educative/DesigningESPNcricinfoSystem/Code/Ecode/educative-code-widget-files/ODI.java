import java.util.*;

public class ODI extends Match {
  public boolean assignStadium(Stadium stadium) {
    this.stadium = stadium;
    return true;
  }

  public boolean assignUmpire(Umpire umpire) {
    umpires.put(umpire, UmpireType.FIELD);
    return true;
  }
}