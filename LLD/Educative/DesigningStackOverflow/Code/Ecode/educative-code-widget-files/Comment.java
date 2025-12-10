import java.util.*;

public class Comment {
    private int id;
    private String content;
    private int flagCount;
    private int upvotes;
    private Date creationDate;
    private User postedBy;
    
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public int getFlagCount() { return flagCount; }
    public void setFlagCount(int flagCount) { this.flagCount = flagCount; }
    
    public int getUpvotes() { return upvotes; }
    public void setUpvotes(int upvotes) { this.upvotes = upvotes; }
    
    public Date getCreationDate() { return creationDate; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }
    
    public User getPostedBy() { return postedBy; }
    public void setPostedBy(User postedBy) { this.postedBy = postedBy; }

}
