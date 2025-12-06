import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public abstract class Service {
    private Date issueAt;

    public boolean addInvoiceItem(Invoice invoice) {
        // Add invoice item logic
        System.out.println("Invoice item added.");
        return true;
    }

    // Getters and Setters
    public Date getIssueAt() {
        return issueAt;
    }

    public void setIssueAt(Date issueAt) {
        this.issueAt = issueAt;
    }
}