import java.util.Date;

public class Cash extends Payment {
  private String billingAddress;

  public Cash(double amount, String billingAddress) {
    this.amount = amount;
    this.billingAddress = billingAddress;
    this.timestamp = new Date();
  }

  @Override
  public PaymentStatus makePayment() {
    System.out.println("Processing cash payment of $" + amount);
    return PaymentStatus.CONFIRMED;
  }

  public String getBillingAddress() {
    return billingAddress;
  }
}