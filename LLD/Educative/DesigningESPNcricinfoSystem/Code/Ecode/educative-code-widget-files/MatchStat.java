import java.util.*;

public class MatchStat extends Stat {
  private double winPercentage;
  private Player topBatsman;
  private Player topBowler;

  @Override
  public boolean updateStats() {
    // Dummy implementation
    return true;
  }

  // Getters and Setters
  public double getWinPercentage() { return winPercentage; }
  public void setWinPercentage(double winPercentage) { this.winPercentage = winPercentage; }
  public Player getTopBatsman() { return topBatsman; }
  public void setTopBatsman(Player topBatsman) { this.topBatsman = topBatsman; }
  public Player getTopBowler() { return topBowler; }
  public void setTopBowler(Player topBowler) { this.topBowler = topBowler; }
}