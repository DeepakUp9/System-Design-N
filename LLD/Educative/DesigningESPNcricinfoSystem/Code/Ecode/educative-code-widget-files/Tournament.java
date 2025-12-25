import java.util.*;

public class Tournament {
  private Date startDate;
  private List<TournamentSquad> teams;
  private List<Match> matches;
  private PointsTable points;

  public boolean addTeam(TournamentSquad team) {
    return teams.add(team);
  }

  public boolean addMatch(Match match) {
    return matches.add(match);
  }

  // Getters and Setters
  public Date getStartDate() { return startDate; }
  public void setStartDate(Date startDate) { this.startDate = startDate; }
  public List<TournamentSquad> getTeams() { return teams; }
  public void setTeams(List<TournamentSquad> teams) { this.teams = teams; }
  public List<Match> getMatches() { return matches; }
  public void setMatches(List<Match> matches) { this.matches = matches; }
  public PointsTable getPoints() { return points; }
  public void setPoints(PointsTable points) { this.points = points; }
}
