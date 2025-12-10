import java.util.*;

public class Bounty {
    private int reputationPoints;
    private Date expiryDate;
    
    public Bounty(int reputationPoints, Date expiryDate) {
        this.reputationPoints = reputationPoints;
        this.expiryDate = expiryDate;
    }


    public boolean updateReputationPoints(int reputation) {
        this.reputationPoints = reputation;
        return true;
    }
    
    public int getReputationPoints() { return reputationPoints; }
    public void setReputationPoints(int reputationPoints) { this.reputationPoints = reputationPoints; }
    
    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }

}
