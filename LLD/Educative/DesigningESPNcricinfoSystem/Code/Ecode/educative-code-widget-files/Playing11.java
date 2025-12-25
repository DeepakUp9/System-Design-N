import java.util.*;

public class Playing11 {
  private List<Player> players = new ArrayList<>();

  public boolean addPlayer(Player player) {
    if (players.size() < 11) {
      players.add(player);
      return true;
    }
    return false;
  }
  
  // Getter
  public List<Player> getPlayers() { return players; }
}