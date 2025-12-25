import java.util.*;

public class Admin {

  private List<Player> players = new ArrayList<>();
  private List<Team> teams = new ArrayList<>();
  private List<Match> matches = new ArrayList<>();
  private List<Tournament> tournaments = new ArrayList<>();
  private List<Stat> stats = new ArrayList<>();
  private List<News> newsList = new ArrayList<>();

  public Match createMatch(MatchType type) {
    switch (type) {
      case T20:
        return new T20();
      case ODI:
        return new ODI();
      case TEST:
        return new Test();
      default:
        return null;
    }
  }

  public boolean addPlayer(Player player) {
    return players.add(player);
  }

  public boolean addTeam(Team team) {
    return teams.add(team);
  }

  public boolean addMatch(Match match) {
    return matches.add(match);
  }

  public boolean addTournament(Tournament tournament) {
    return tournaments.add(tournament);
  }

  public boolean addStats(Stat stat) {
    return stats.add(stat);
  }

  public boolean addNews(News news) {
    return newsList.add(news);
  }

  public boolean assignStadiumToMatch(Stadium stadium, Match match) {
    return stadium.assignMatch(match);
  }

  public boolean assignUmpireToMatch(Umpire umpire, Match match) {
    return umpire.assignMatch(match);
  }

  public boolean assignCommentatorToMatch(Commentator commentator, Match match) {
    return commentator.assignMatch(match);
  }

  // Getters for internal lists if needed
  public List<Player> getPlayers() { return players; }
  public List<Team> getTeams() { return teams; }
  public List<Match> getMatches() { return matches; }
  public List<Tournament> getTournaments() { return tournaments; }
  public List<Stat> getStats() { return stats; }
  public List<News> getNewsList() { return newsList; }
}
