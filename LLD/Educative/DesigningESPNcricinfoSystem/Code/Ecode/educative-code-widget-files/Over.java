import java.util.*;

public class Over {
  private int number;
  private Player bowler;
  private int totalScore;
  private List<Ball> balls;

  public boolean addBall(Ball ball) {
    return balls.add(ball);
  }

  // Getters and Setters
  public int getNumber() { return number; }
  public void setNumber(int number) { this.number = number; }
  public Player getBowler() { return bowler; }
  public void setBowler(Player bowler) { this.bowler = bowler; }
  public int getTotalScore() { return totalScore; }
  public void setTotalScore(int totalScore) { this.totalScore = totalScore; }
  public List<Ball> getBalls() { return balls; }
  public void setBalls(List<Ball> balls) { this.balls = balls; }
}
