import java.util.*;

public class Innings {
  private Playing11 bowling;
  private Playing11 batting;
  private Date startTime;
  private Date endTime;
  private int totalScores;
  private int totalWickets;
  private List<Over> overs;

  public boolean addOver(Over over) {
    return overs.add(over);
  }

  // Getters and Setters
  public Playing11 getBowling() { return bowling; }
  public void setBowling(Playing11 bowling) { this.bowling = bowling; }
  public Playing11 getBatting() { return batting; }
  public void setBatting(Playing11 batting) { this.batting = batting; }
  public Date getStartTime() { return startTime; }
  public void setStartTime(Date startTime) { this.startTime = startTime; }
  public Date getEndTime() { return endTime; }
  public void setEndTime(Date endTime) { this.endTime = endTime; }
  public int getTotalScores() { return totalScores; }
  public void setTotalScores(int totalScores) { this.totalScores = totalScores; }
  public int getTotalWickets() { return totalWickets; }
  public void setTotalWickets(int totalWickets) { this.totalWickets = totalWickets; }
  public List<Over> getOvers() { return overs; }
  public void setOvers(List<Over> overs) { this.overs = overs; }
}
