import java.util.*;

public class Wicket {
  private WicketType type;
  private Player playerOut;
  private Player balledBy;
  private Player caughtBy;
  private Player runoutBy;
  private Player stumpedBy;

  // Getters and Setters
  public WicketType getType() { return type; }
  public void setType(WicketType type) { this.type = type; }
  public Player getPlayerOut() { return playerOut; }
  public void setPlayerOut(Player playerOut) { this.playerOut = playerOut; }
  public Player getBalledBy() { return balledBy; }
  public void setBalledBy(Player balledBy) { this.balledBy = balledBy; }
  public Player getCaughtBy() { return caughtBy; }
  public void setCaughtBy(Player caughtBy) { this.caughtBy = caughtBy; }
  public Player getRunoutBy() { return runoutBy; }
  public void setRunoutBy(Player runoutBy) { this.runoutBy = runoutBy; }
  public Player getStumpedBy() { return stumpedBy; }
  public void setStumpedBy(Player stumpedBy) { this.stumpedBy = stumpedBy; }
}