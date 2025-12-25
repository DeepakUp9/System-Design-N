import java.util.*;

public class News {
  private Date date;
  private String text;
  private List<Byte> image;
  private Team team;

  // Getters and Setters
  public Date getDate() { return date; }
  public void setDate(Date date) { this.date = date; }
  public String getText() { return text; }
  public void setText(String text) { this.text = text; }
  public List<Byte> getImage() { return image; }
  public void setImage(List<Byte> image) { this.image = image; }
  public Team getTeam() { return team; }
  public void setTeam(Team team) { this.team = team; }
}