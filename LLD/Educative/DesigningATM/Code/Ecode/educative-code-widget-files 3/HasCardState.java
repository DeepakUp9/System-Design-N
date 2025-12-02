import java.util.*;

public class HasCardState extends ATMState {
    @Override
    public void authenticatePin(ATM atm, ATMCard card, int pin) {
        if (card.validatePin(pin)) {
            atm.setAuthenticated(true);
            atm.setAtmStatus(ATMStatus.SelectionOption);
            atm.setCurrentATMState(new SelectionOptionState());
            atm.getScreen().showMessage("PIN verified. Please select a transaction.");
        } else {
            atm.getScreen().showMessage("Incorrect PIN. Please try again or cancel.");
        }
    }

    @Override
    public void returnCard(ATM atm) {
        atm.setInsertedCard(null);
        atm.setAuthenticated(false);
        atm.setAtmStatus(ATMStatus.Idle);
        atm.setCurrentATMState(new IdleState());
        atm.getScreen().showMessage("Card returned. Thank you.");
    }
}