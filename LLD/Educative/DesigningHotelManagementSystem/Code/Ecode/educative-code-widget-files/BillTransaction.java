import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public abstract class BillTransaction {
    private Date creationDate;
    private double amount;
    private PaymentStatus status;

    public abstract void initiateTransaction();

    // Getters and Setters
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}