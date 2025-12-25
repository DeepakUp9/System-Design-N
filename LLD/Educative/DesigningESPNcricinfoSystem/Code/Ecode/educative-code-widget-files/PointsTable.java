import java.util.*;

public class PointsTable {
  private HashMap<String, Float> teamPoints;
  private HashMap<Team, MatchResult> matchResults;
  private Tournament tournament;
  private Date lastUpdated;

  // Getters and Setters
  public HashMap<String, Float> getTeamPoints() { return teamPoints; }
  public void setTeamPoints(HashMap<String, Float> teamPoints) { this.teamPoints = teamPoints; }
  public HashMap<Team, MatchResult> getMatchResults() { return matchResults; }
  public void setMatchResults(HashMap<Team, MatchResult> matchResults) { this.matchResults = matchResults; }
  public Tournament getTournament() { return tournament; }
  public void setTournament(Tournament tournament) { this.tournament = tournament; }
  public Date getLastUpdated() { return lastUpdated; }
  public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }
}