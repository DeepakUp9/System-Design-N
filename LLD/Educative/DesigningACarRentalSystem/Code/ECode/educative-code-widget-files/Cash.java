public class Cash extends Payment {
    @Override
    public boolean makePayment() {
        setStatus(PaymentStatus.COMPLETED);
        return true;
    }
}
