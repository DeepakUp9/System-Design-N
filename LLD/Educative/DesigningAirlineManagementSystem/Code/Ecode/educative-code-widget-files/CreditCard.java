public class CreditCard extends Payment {
  private String nameOnCard;
  private String cardNumber;

  public CreditCard(int paymentId, double amount, String nameOnCard, String cardNumber) {
    super(paymentId, amount);
    this.nameOnCard = nameOnCard;
    this.cardNumber = cardNumber;
  }

  public String getNameOnCard() { return nameOnCard; }
  public void setNameOnCard(String nameOnCard) { this.nameOnCard = nameOnCard; }

  public String getCardNumber() { return cardNumber; }
  public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

  @Override
  public boolean makePayment(Account account) {
      System.out.println("Charging card " + cardNumber + " for $" + getAmount());
      setStatus(PaymentStatus.COMPLETED);
      notifyUser(account);
      return true;
  }

}
