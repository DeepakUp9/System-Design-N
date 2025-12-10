import java.util.Date;

public class ElectronicBankTransfer extends Payment {
  private String bankName;
  private String routingNumber;
  private String accountNumber;
  private String billingAddress;

  public ElectronicBankTransfer(double amount, String bankName, String routingNumber, String accountNumber, String billingAddress) {
    this.amount = amount;
    this.bankName = bankName;
    this.routingNumber = routingNumber;
    this.accountNumber = accountNumber;
    this.billingAddress = billingAddress;
    this.timestamp = new Date();
  }

  @Override
  public PaymentStatus makePayment() {
    System.out.println("Processing bank transfer for " + amount);
    return PaymentStatus.CONFIRMED;
  }
}