import java.util.*;

public class Cash extends Payment {
    public boolean makePayment() {
        System.out.println("Cash payment received.");
        status = PaymentStatus.CONFIRMED;
        return true;
    }
}
