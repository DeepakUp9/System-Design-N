class ChangePinState extends ATMState {
    @Override
    public void changePin(ATM atm, ATMCard card, int newPin) {
        card.setPin(newPin);
        atm.getPrinter().printReceipt("PIN changed successfully.");
        atm.getScreen().showMessage("PIN changed.");
        // Return to selection state
        atm.setAtmStatus(ATMStatus.SelectionOption);
        atm.setCurrentATMState(new SelectionOptionState());
    }
}