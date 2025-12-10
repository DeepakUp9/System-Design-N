import java.util.Date;

public class CreditCard extends Payment {
  private String nameOnCard;
  private String cardNumber;
  private String billingAddress;
  private int code;

  public CreditCard(double amount, String nameOnCard, String cardNumber, String billingAddress, int code) {
    this.amount = amount;
    this.nameOnCard = nameOnCard;
    this.cardNumber = cardNumber;
    this.billingAddress = billingAddress;
    this.code = code;
    this.timestamp = new Date();
  }

  @Override
  public PaymentStatus makePayment() {
    System.out.println("Processing credit card payment for " + amount);
    return PaymentStatus.CONFIRMED;
  }
}