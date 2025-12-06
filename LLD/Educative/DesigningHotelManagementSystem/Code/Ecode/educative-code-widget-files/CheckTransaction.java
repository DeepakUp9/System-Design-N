import java.util.Date;
import java.util.List;
import java.util.ArrayList;

class CheckTransaction extends BillTransaction {
    private String bankName;
    private String checkNumber;

    @Override
    public void initiateTransaction() {
        // Check transaction logic
        setStatus(PaymentStatus.COMPLETED);
        System.out.println("Check transaction initiated.");
    }

    // Getters and Setters
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }
}