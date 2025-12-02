import java.util.*;

public class SavingAccount extends BankAccount {
    public SavingAccount(int accountNumber, double balance) {
        super(accountNumber, balance);
    }

    @Override
    public double getWithdrawLimit() {
        return 1000.0;
    }
}