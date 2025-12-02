import java.util.*;

public class TransferMoneyState extends ATMState {
    @Override
    public void transferMoney(ATM atm, ATMCard card, BankAccount toAccount, double amount) {
        BankAccount fromAcc = atm.getActiveUser().getAccount();
        double accLimit = fromAcc.getWithdrawLimit();
        if (amount <= fromAcc.getAvailableBalance() && amount <= accLimit) {
            boolean transferred = fromAcc.transfer(toAccount, amount);
            if (transferred) {
                atm.getPrinter().printReceipt("Transferred: $" + amount +
                    " from Acc#"+fromAcc.getAccountNumber() + " to Acc#"+toAccount.getAccountNumber() +
                    " | Your balance: $" + fromAcc.getAvailableBalance());
                atm.getScreen().showMessage("Transfer successful.");
            } else {
                atm.getScreen().showMessage("Transfer failed.");
            }
        } else {
            atm.getScreen().showMessage("Transfer denied. Check your balance or limits.");
        }
        // Return to selection state
        atm.setAtmStatus(ATMStatus.SelectionOption);
        atm.setCurrentATMState(new SelectionOptionState());
    }
}
