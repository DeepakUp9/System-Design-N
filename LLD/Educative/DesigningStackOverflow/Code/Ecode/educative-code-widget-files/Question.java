import java.util.*;

public class Question {
    private int id;
    private String title;
    private String content;
    private User createdBy;
    private int upvotes;
    private int downvotes;
    private int voteCount;
    private Date creationDate;
    private Date modificationDate;
    private QuestionStatus status;
    private Bounty bounty;
    private List<Tag> tags;
    private List<Comment> comments;
    private List<Answer> answers;
    private List<User> followers;
    
    public Question(int id, String title, String content, User createdBy) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdBy = createdBy;
        this.creationDate = new Date();
        this.modificationDate = new Date();
        this.status = QuestionStatus.ACTIVE;
        this.tags = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.answers = new ArrayList<>();
        this.followers = new ArrayList<>();
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void addBounty(Bounty bounty) {
        this.bounty = bounty;
    }

    public void notify(String closedQuestion) {
        System.out.println("Notification: " + closedQuestion);
    }

    public void createBounty(int value) {
        Bounty bounty = new Bounty(value, new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000)); // 7 days
        this.addBounty(bounty);
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
    
    public int getUpvotes() { return upvotes; }
    public void setUpvotes(int upvotes) { this.upvotes = upvotes; }
    
    public int getDownvotes() { return downvotes; }
    public void setDownvotes(int downvotes) { this.downvotes = downvotes; }
    
    public int getVoteCount() { return voteCount; }
    public void setVoteCount(int voteCount) { this.voteCount = voteCount; }
    
    public Date getCreationDate() { return creationDate; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }
    
    public Date getModificationDate() { return modificationDate; }
    public void setModificationDate(Date modificationDate) { this.modificationDate = modificationDate; }
    
    public QuestionStatus getStatus() { return status; }
    public void setStatus(QuestionStatus status) { this.status = status; }
    
    public Bounty getBounty() { return bounty; }
    public void setBounty(Bounty bounty) { this.bounty = bounty; }
    
    public List<Tag> getTags() { return tags; }
    public void setTags(List<Tag> tags) { this.tags = tags; }
    
    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }
    
    public List<Answer> getAnswers() { return answers; }
    public void setAnswers(List<Answer> answers) { this.answers = answers; }
    
}
