import java.util.*;

public class BalanceInquiryState extends ATMState {
    @Override
    public void displayBalance(ATM atm, ATMCard card) {
        atm.getScreen().showMessage("Your current balance is: $" +
            atm.getActiveUser().getAccount().getAvailableBalance());
        atm.getPrinter().printReceipt("Balance: $" +
            atm.getActiveUser().getAccount().getAvailableBalance());
        // Return to selection state
        atm.setAtmStatus(ATMStatus.SelectionOption);
        atm.setCurrentATMState(new SelectionOptionState());
    }
}