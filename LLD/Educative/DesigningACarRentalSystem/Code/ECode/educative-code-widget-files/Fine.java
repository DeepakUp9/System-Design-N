public class Fine {
    private double amount;
    private String reason;

    public void setAmount(double a) { amount = a; }
    public double getAmount() { return amount; }
    public void setReason(String r) { reason = r; }
    public String getReason() { return reason; }

    public double calculateFine() {
        return amount;
    }
}
