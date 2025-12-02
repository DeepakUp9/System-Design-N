import java.util.*;

public class ATMCard {
    private String cardNumber;
    private String customerName;
    private String cardExpiryDate; // Simplified
    private int pin;

    public ATMCard(String cardNumber, String customerName, String expiry, int pin) {
        this.cardNumber = cardNumber;
        this.customerName = customerName;
        this.cardExpiryDate = expiry;
        this.pin = pin;
    }

    public boolean validatePin(int enteredPin) {
        return this.pin == enteredPin;
    }

    public void setPin(int newPin) {
        this.pin = newPin;
    }

    public String getCardNumber() { return cardNumber; }
    public String getCustomerName() { return customerName; }
}