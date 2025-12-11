import java.util.Date;

public class CreditCard extends Payment {
    private String nameOnCard;
    private int zipcode;

    public CreditCard(int paymentID, Date creationDate, double amount, PaymentStatus status,
                      String nameOnCard, int zipcode) {
        super(paymentID, creationDate, amount, status);
        this.nameOnCard = nameOnCard;
        this.zipcode = zipcode;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public void processPayment() {
        // Credit card payment logic
        System.out.println("Processing credit card payment...");
        setStatus(PaymentStatus.Paid);
    }
}
