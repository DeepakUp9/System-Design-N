import java.util.*;

public class CashWithdrawalState extends ATMState {
    @Override
    public void cashWithdrawal(ATM atm, ATMCard card, double amount) {
        BankAccount acc = atm.getActiveUser().getAccount();
        double accLimit = acc.getWithdrawLimit();
        if (amount <= acc.getAvailableBalance() && amount <= accLimit && amount <= atm.getAtmBalance()) {
            boolean dispensed = atm.getCashDispenser().dispenseCash((int) amount, atm);
            if (dispensed) {
                acc.withdraw(amount);
                atm.getPrinter().printReceipt("Withdrawn: $" + amount + " | Remaining balance: $" + acc.getAvailableBalance());
                atm.getScreen().showMessage("Please collect your cash.");
            } else {
                atm.getScreen().showMessage("Unable to dispense the requested amount.");
            }
        } else {
            atm.getScreen().showMessage("Withdrawal denied. Check your balance or limits.");
        }
        // Return to selection state
        atm.setAtmStatus(ATMStatus.SelectionOption);
        atm.setCurrentATMState(new SelectionOptionState());
    }
}