import java.util.*;

public class Answer {
    private int id;
    private String content;
    private int flagCount;
    private int voteCount;
    private int upvotes;
    private int downvotes;
    private boolean isAccepted;
    private Date creationTime;
    private User postedBy;
    private List<Comment> comments;

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public int getFlagCount() { return flagCount; }
    public void setFlagCount(int flagCount) { this.flagCount = flagCount; }
    
    public int getVoteCount() { return voteCount; }
    public void setVoteCount(int voteCount) { this.voteCount = voteCount; }
    
    public int getUpvotes() { return upvotes; }
    public void setUpvotes(int upvotes) { this.upvotes = upvotes; }
    
    public int getDownvotes() { return downvotes; }
    public void setDownvotes(int downvotes) { this.downvotes = downvotes; }
    
    public boolean isAccepted() { return isAccepted; }
    public void setAccepted(boolean accepted) { isAccepted = accepted; }
    
    public Date getCreationTime() { return creationTime; }
    public void setCreationTime(Date creationTime) { this.creationTime = creationTime; }
    
    public User getPostedBy() { return postedBy; }
    public void setPostedBy(User postedBy) { this.postedBy = postedBy; }
    
    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }
    
}
