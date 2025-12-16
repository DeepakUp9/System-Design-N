public class Cash extends Payment {

  public Cash(int paymentId, double amount) {
    super(paymentId, amount);
  }

  @Override
  public boolean makePayment(Account account) {
      System.out.println("Cash payment of $" + getAmount() + " received.");
      setStatus(PaymentStatus.COMPLETED);
      notifyUser(account);
      return true;
  }

}
