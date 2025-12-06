import java.util.Date;
import java.util.List;
import java.util.ArrayList;

class CreditCardTransaction extends BillTransaction {
    private String nameOnCard;
    private int zipcode;

    @Override
    public void initiateTransaction() {
        // Credit Card transaction logic
        setStatus(PaymentStatus.COMPLETED);
        System.out.println("Credit Card transaction initiated.");
    }

    // Getters and Setters
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
}