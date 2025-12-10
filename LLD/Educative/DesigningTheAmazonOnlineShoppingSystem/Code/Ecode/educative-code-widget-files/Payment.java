import java.util.Date;

public abstract class Payment {
  protected double amount;
  protected Date timestamp;
  private PaymentStatus status;

  public abstract PaymentStatus makePayment();
}
