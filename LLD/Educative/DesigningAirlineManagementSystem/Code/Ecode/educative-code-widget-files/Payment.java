import java.util.Date;

public abstract class Payment {
  private int paymentId;
  private double amount;
  private PaymentStatus status;
  private Date timestamp;

  public Payment(int paymentId, double amount) {
    this.paymentId = paymentId;
    this.amount = amount;
    this.status = PaymentStatus.PENDING;
    this.timestamp = new Date();
  }

  public int getPaymentId() { return paymentId; }
  public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

  public double getAmount() { return amount; }
  public void setAmount(double amount) { this.amount = amount; }

  public PaymentStatus getStatus() { return status; }
  public void setStatus(PaymentStatus status) { this.status = status; }

  public Date getTimestamp() { return timestamp; }

  public boolean notifyUser(Account account) {
    Notification n = new EmailNotification(paymentId, "Your payment of $" + amount + " is " + status);
    n.sendNotification(account);
    return true;
  }

  public abstract boolean makePayment(Account account);

}
