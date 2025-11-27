import java.util.*;

public abstract class Payment {
    public double amount;
    public Date timestamp;
    public PaymentStatus status;

    public abstract boolean makePayment();
}
