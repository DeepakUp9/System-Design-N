import java.util.*;

public abstract class BankAccount {
    protected int accountNumber;
    protected double availableBalance;

    public BankAccount(int accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.availableBalance = balance;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= availableBalance && amount <= getWithdrawLimit()) {
            availableBalance -= amount;
            return true;
        }
        return false;
    }

    public boolean transfer(BankAccount toAccount, double amount) {
        if (amount > 0 && amount <= availableBalance && amount <= getWithdrawLimit()) {
            availableBalance -= amount;
            toAccount.availableBalance += amount;
            return true;
        }
        return false;
    }

    public abstract double getWithdrawLimit();
    public int getAccountNumber() { return accountNumber; }
}