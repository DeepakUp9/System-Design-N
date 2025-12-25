import java.util.*;

public class Ball {
  private Player balledBy;
  private Player playedBy;
  private BallType type;
  private List<Run> runs;
  private Wicket wicket;

  public boolean addCommentary(Commentary commentary) {
    return true; // Dummy implementation
  }

  // Getters and Setters
  public Player getBalledBy() { return balledBy; }
  public void setBalledBy(Player balledBy) { this.balledBy = balledBy; }
  public Player getPlayedBy() { return playedBy; }
  public void setPlayedBy(Player playedBy) { this.playedBy = playedBy; }
  public BallType getType() { return type; }
  public void setType(BallType type) { this.type = type; }
  public List<Run> getRuns() { return runs; }
  public void setRuns(List<Run> runs) { this.runs = runs; }
  public Wicket getWicket() { return wicket; }
  public void setWicket(Wicket wicket) { this.wicket = wicket; }
}