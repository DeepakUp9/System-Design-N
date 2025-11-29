public class CreditCard extends Payment {
    private String nameOnCard;
    private String cardNumber;
    private String billingAddress;
    private int code;

    public void setNameOnCard(String n) { nameOnCard = n; }
    public void setCardNumber(String c) { cardNumber = c; }
    public void setBillingAddress(String b) { billingAddress = b; }
    public void setCode(int c) { code = c; }

    @Override
    public boolean makePayment() {
        setStatus(PaymentStatus.COMPLETED);
        return true;
    }
}
