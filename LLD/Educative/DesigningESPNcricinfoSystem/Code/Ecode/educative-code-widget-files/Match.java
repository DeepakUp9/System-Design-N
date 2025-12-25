import java.util.*;

public abstract class Match {
  protected Date startTime;
  protected MatchResult result;
  protected int totalOvers;
  protected List<Playing11> teams = new ArrayList<>();
  protected List<Innings> innings;
  protected Playing11 tossWin;
  protected Map<Umpire, UmpireType> umpires = new HashMap<>();
  protected Stadium stadium;
  protected List<Commentator> commentators = new ArrayList<>();
  protected List<MatchStat> stats;

  public boolean addTeam(Playing11 team) {
    if (teams.size() < 2) {
      teams.add(team);
      return true;
    }
    return false;
  }

  public abstract boolean assignStadium(Stadium stadium);
  public abstract boolean assignUmpire(Umpire umpire);

  public boolean assignCommentator(Commentator commentator) {
    if (!commentators.contains(commentator)) {
      commentators.add(commentator);
      return true;
    }
    return false;
  }
  
  // Getters and Setters
  public Date getStartTime() { return startTime; }
  public void setStartTime(Date startTime) { this.startTime = startTime; }
  public MatchResult getResult() { return result; }
  public void setResult(MatchResult result) { this.result = result; }
  public int getTotalOvers() { return totalOvers; }
  public void setTotalOvers(int totalOvers) { this.totalOvers = totalOvers; }
  public List<Playing11> getTeams() { return teams; }
  public List<Innings> getInnings() { return innings; }
  public void setInnings(List<Innings> innings) { this.innings = innings; }
  public Playing11 getTossWin() { return tossWin; }
  public void setTossWin(Playing11 tossWin) { this.tossWin = tossWin; }
  public Map<Umpire, UmpireType> getUmpires() { return umpires; }
  public Stadium getStadium() { return stadium; }
  public List<Commentator> getCommentators() { return commentators; }
  public List<MatchStat> getStats() { return stats; }
  public void setStats(List<MatchStat> stats) { this.stats = stats; }
}