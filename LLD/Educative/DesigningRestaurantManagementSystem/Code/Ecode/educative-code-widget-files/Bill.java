public class Bill {
    private int billId;
    private float amount;
    private float tax;
    private boolean isPaid;

    public Bill(int billId, float amount, float tax, boolean isPaid) {
        this.billId = billId;
        this.amount = amount;
        this.tax = tax;
        this.isPaid = isPaid;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

}
