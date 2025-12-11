import java.util.Date;

public class Cash extends Payment {
    private double cashTendered;

    public Cash(int paymentID, Date creationDate, double amount, PaymentStatus status, double cashTendered) {
        super(paymentID, creationDate, amount, status);
        this.cashTendered = cashTendered;
    }

    public double getCashTendered() {
        return cashTendered;
    }

    public void setCashTendered(double cashTendered) {
        this.cashTendered = cashTendered;
    }

    public void processPayment() {
        // Cash payment logic
        System.out.println("Processing cash payment...");
        setStatus(PaymentStatus.Paid);
    }
}
