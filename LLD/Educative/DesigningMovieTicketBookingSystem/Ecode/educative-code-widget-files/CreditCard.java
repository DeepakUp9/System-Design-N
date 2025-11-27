import java.util.*;

public class CreditCard extends Payment {
    public String nameOnCard;
    public String cardNumber;
    public String billingAddress;
    public int code;

    public boolean makePayment() {
        System.out.println("Credit card payment processed for " + nameOnCard);
        status = PaymentStatus.CONFIRMED;
        return true;
    }
}
