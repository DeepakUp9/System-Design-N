import java.util.*;

public class Team {
  private String name;
  private List<Player> players;
  private Coach coach;
  private List<News> news;
  private TeamStat stats;

  public boolean addSquad(TournamentSquad squad) {
    return true; // Dummy placeholder
  }

  public boolean addPlayer(Player player) {
    return players.add(player);
  }

  public boolean addNews(News newsItem) {
    return news.add(newsItem);
  }

  // Getters and Setters
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public List<Player> getPlayers() { return players; }
  public void setPlayers(List<Player> players) { this.players = players; }
  public Coach getCoach() { return coach; }
  public void setCoach(Coach coach) { this.coach = coach; }
  public List<News> getNews() { return news; }
  public void setNews(List<News> news) { this.news = news; }
  public TeamStat getStats() { return stats; }
  public void setStats(TeamStat stats) { this.stats = stats; }
}
