import java.util.*;

public class Commentary {
  private String text;
  private Date createdAt;
  private Commentator commentator;

  // Getters and Setters
  public String getText() { return text; }
  public void setText(String text) { this.text = text; }
  public Date getCreatedAt() { return createdAt; }
  public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
  public Commentator getCommentator() { return commentator; }
  public void setCommentator(Commentator commentator) { this.commentator = commentator; }
}
