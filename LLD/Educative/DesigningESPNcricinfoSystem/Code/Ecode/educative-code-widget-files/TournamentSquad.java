import java.util.*;

public class TournamentSquad {
  private List<Player> players;
  private Tournament tournament;
  private List<TeamStat> stats;

  public boolean addPlayer(Player player) {
    return players.add(player);
  }

  // Getters and Setters
  public List<Player> getPlayers() { return players; }
  public void setPlayers(List<Player> players) { this.players = players; }
  public Tournament getTournament() { return tournament; }
  public void setTournament(Tournament tournament) { this.tournament = tournament; }
  public List<TeamStat> getStats() { return stats; }
  public void setStats(List<TeamStat> stats) { this.stats = stats; }
}