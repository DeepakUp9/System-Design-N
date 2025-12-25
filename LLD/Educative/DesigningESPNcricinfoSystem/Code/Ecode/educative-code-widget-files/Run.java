import java.util.*;

public class Run {
  private int totalRuns;
  private RunType type;
  private Player scoredBy;

  // Getters and Setters
  public int getTotalRuns() { return totalRuns; }
  public void setTotalRuns(int totalRuns) { this.totalRuns = totalRuns; }
  public RunType getType() { return type; }
  public void setType(RunType type) { this.type = type; }
  public Player getScoredBy() { return scoredBy; }
  public void setScoredBy(Player scoredBy) { this.scoredBy = scoredBy; }
}