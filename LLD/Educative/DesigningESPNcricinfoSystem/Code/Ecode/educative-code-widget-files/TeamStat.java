import java.util.*;

public class TeamStat extends Stat {
  private int totalSixes;
  private int totalFours;
  private int totalReviews;

  @Override
  public boolean updateStats() {
    return true;
  }

  // Getters and Setters
  public int getTotalSixes() { return totalSixes; }
  public void setTotalSixes(int totalSixes) { this.totalSixes = totalSixes; }
  public int getTotalFours() { return totalFours; }
  public void setTotalFours(int totalFours) { this.totalFours = totalFours; }
  public int getTotalReviews() { return totalReviews; }
  public void setTotalReviews(int totalReviews) { this.totalReviews = totalReviews; }
}