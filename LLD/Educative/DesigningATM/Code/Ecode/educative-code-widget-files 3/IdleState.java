import java.util.*;

public class IdleState extends ATMState {
    @Override
    public void insertCard(ATM atm, ATMCard card) {
        if (atm.getCardReader().readCard(card)) {
            atm.setInsertedCard(card);
            atm.setAtmStatus(ATMStatus.HasCard);
            atm.setCurrentATMState(new HasCardState());
            atm.getScreen().showMessage("Please enter your PIN:");
        } else {
            atm.getScreen().showMessage("Card reading failed.");
        }
    }
}