import java.util.*;

public class User {
    private int reputationPoints;
    private String name;
    private UserStatus status;
    private List<Badge> badges;
    private Question currentDraft;

    public boolean addAnswer(Question question, Answer answer) {
        question.getAnswers().add(answer);
        return true;
    }

    public boolean createComment(Comment comment) {
        System.out.println(this.name + " added a comment.");
        return true;
    }

    public boolean createTag(Tag tag) {
        System.out.println(this.name + " created a tag.");
        return true;
    }

    public void flagQuestion(Question question) {
        System.out.println(this.name + " flagged question: " + question.getTitle());
    }

    public void flagAnswer(Answer answer) {
        System.out.println(this.name + " flagged answer ID: " + answer.getId());
    }

    public void upvote(int id) {
        System.out.println(this.name + " upvoted post ID: " + id);
    }

    public void downvote(int id) {
        System.out.println(this.name + " downvoted post ID: " + id);
    }

    public void vote(String action, String remark) {
        System.out.println(this.name + " voted to " + action + " with remark: " + remark);
    }
    
    public Question create(String title, String body, List<Tag> tags) {
        currentDraft = new Question(/*id=*/new Random().nextInt(10000), title, body, this);
        currentDraft.setTags(tags);
        System.out.println(this.name + " is drafting a new question.");
        return currentDraft;
    }

    public boolean publishQuestion() {
        if (currentDraft != null) {
            currentDraft.setCreationDate(new Date());
            System.out.println(this.name + " published question: " + currentDraft.getTitle());
            currentDraft = null;
            return true;
        }
        return false;
    }

    public void addBounty(int value) {
        if (currentDraft != null) {
            currentDraft.createBounty(value);
            System.out.println("Bounty of " + value + " points added to draft question.");
        } else {
            System.out.println("No draft question available to add bounty.");
        }
    }

    public void acceptAnswer(Answer answer) {
        answer.setAccepted(true);
    }
    
    public int getReputationPoints() { return reputationPoints; }
    public void setReputationPoints(int reputationPoints) { this.reputationPoints = reputationPoints; }
    
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    
    public List<Badge> getBadges() { return badges; }
    public void setBadges(List<Badge> badges) { this.badges = badges; }
    
    public Question getCurrentDraft() { return currentDraft; }
    public void setCurrentDraft(Question currentDraft) { this.currentDraft = currentDraft; }
    
    public UserStatus getStatus() { return this.status; }
    public void setStatus(UserStatus status) { this.status = status; }
    

}
