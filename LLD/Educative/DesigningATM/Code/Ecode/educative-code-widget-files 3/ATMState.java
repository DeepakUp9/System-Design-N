import java.util.*;

public abstract class ATMState {
    public void insertCard(ATM atm, ATMCard card) {}
    public void authenticatePin(ATM atm, ATMCard card, int pin) {}
    public void selectOperation(ATM atm, TransactionType tType) {}
    public void cashWithdrawal(ATM atm, ATMCard card, double amount) {}
    public void displayBalance(ATM atm, ATMCard card) {}
    public void transferMoney(ATM atm, ATMCard card, BankAccount toAccount, double amount) {}
    public void changePin(ATM atm, ATMCard card, int newPin) {}
    public void cancelTransaction(ATM atm) {}
    public void returnCard(ATM atm) {}
    public void exit(ATM atm) {}
}