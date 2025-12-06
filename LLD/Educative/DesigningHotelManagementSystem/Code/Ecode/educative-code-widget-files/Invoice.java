import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Invoice {
    private double amount;

    public boolean createBill() {
        // Bill creation logic
        System.out.println("Bill created.");
        return true;
    }

    // Getters and Setters
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
