import java.util.Date;

public abstract class Payment {
    private int paymentID;
    private Date creationDate;
    private double amount;
    private PaymentStatus status;

    public Payment(int paymentID, Date creationDate, double amount, PaymentStatus status) {
        this.paymentID = paymentID;
        this.creationDate = creationDate;
        this.amount = amount;
        this.status = status;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

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

    public void updateTableStatus(Table table) {
        table.setStatus(TableStatus.Free);
    }
    
    public abstract void processPayment();
}
