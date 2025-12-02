import java.util.*;

public class CurrentAccount extends BankAccount {
    public CurrentAccount(int accountNumber, double balance) {
        super(accountNumber, balance);
    }

    @Override
    public double getWithdrawLimit() {
        return 5000.0;
    }
}