import java.util.*;

public class SelectionOptionState extends ATMState {
    @Override
    public void selectOperation(ATM atm, TransactionType tType) {
        switch (tType) {
            case BalanceInquiry:
                atm.setAtmStatus(ATMStatus.BalanceInquiry);
                atm.setCurrentATMState(new BalanceInquiryState());
                break;
            case CashWithdrawal:
                atm.setAtmStatus(ATMStatus.Withdraw);
                atm.setCurrentATMState(new CashWithdrawalState());
                break;
            case FundsTransfer:
                atm.setAtmStatus(ATMStatus.TransferMoney);
                atm.setCurrentATMState(new TransferMoneyState());
                break;
            case ChangePIN:
                atm.setAtmStatus(ATMStatus.ChangePin);
                atm.setCurrentATMState(new ChangePinState());
                break;
            case Cancel:
                atm.getCurrentATMState().returnCard(atm);
                break;
        }
    }
}