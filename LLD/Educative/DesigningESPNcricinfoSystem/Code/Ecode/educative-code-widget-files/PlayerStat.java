import java.util.*;
public class PlayerStat extends Stat {
  private int ranking;
  private int bestScore;
  private int bestWicketCount;
  private int totalMatchesPlayed;
  private int total100s;
  private int totalHattricks;

  @Override
  public boolean updateStats() {
    // Dummy implementation
    return true;
  }
  
  // Getters and Setters
  public int getRanking() { return ranking; }
  public void setRanking(int ranking) { this.ranking = ranking; }
  public int getBestScore() { return bestScore; }
  public void setBestScore(int bestScore) { this.bestScore = bestScore; }
  public int getBestWicketCount() { return bestWicketCount; }
  public void setBestWicketCount(int bestWicketCount) { this.bestWicketCount = bestWicketCount; }
  public int getTotalMatchesPlayed() { return totalMatchesPlayed; }
  public void setTotalMatchesPlayed(int totalMatchesPlayed) { this.totalMatchesPlayed = totalMatchesPlayed; }
  public int getTotal100s() { return total100s; }
  public void setTotal100s(int total100s) { this.total100s = total100s; }
  public int getTotalHattricks() { return totalHattricks; }
  public void setTotalHattricks(int totalHattricks) { this.totalHattricks = totalHattricks; }
  
}