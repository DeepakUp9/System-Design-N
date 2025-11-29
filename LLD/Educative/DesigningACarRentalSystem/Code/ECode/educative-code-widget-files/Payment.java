import java.util.*;

public abstract class Payment {
    private double amount;
    private Date timestamp;
    private PaymentStatus status;

    public void setAmount(double a) { amount = a; }
    public double getAmount() { return amount; }
    public void setTimestamp(Date d) { timestamp = d; }
    public void setStatus(PaymentStatus s) { status = s; }
    public PaymentStatus getStatus() { return status; }

    public abstract boolean makePayment();
}
