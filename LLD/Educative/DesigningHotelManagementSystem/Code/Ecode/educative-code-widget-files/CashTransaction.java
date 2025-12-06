import java.util.Date;
import java.util.List;
import java.util.ArrayList;

class CashTransaction extends BillTransaction {
    private double cashTendered;

    @Override
    public void initiateTransaction() {
        // Cash transaction logic
        setStatus(PaymentStatus.COMPLETED);
        System.out.println("Cash transaction initiated.");
    }

    // Getters and Setters
    public double getCashTendered() {
        return cashTendered;
    }

    public void setCashTendered(double cashTendered) {
        this.cashTendered = cashTendered;
    }
}