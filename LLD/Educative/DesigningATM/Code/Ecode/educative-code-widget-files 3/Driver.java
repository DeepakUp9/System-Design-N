import java.util.*;

public class Driver {
    public static void main(String[] args) {
        // Set up bank, accounts, users, cards
        Bank bank = new Bank("Sample Bank", "SB001");

        BankAccount acc1 = new SavingAccount(1001, 1200.0);
        BankAccount acc2 = new CurrentAccount(1002, 8000.0);
        ATMCard card1 = new ATMCard("123456", "Alice", "12/27", 1111);
        ATMCard card2 = new ATMCard("654321", "Bob", "08/26", 2222);

        User user1 = new User(card1, acc1);
        User user2 = new User(card2, acc2);

        // Set up ATM
        ATM atm = ATM.getInstance();
        atm.initializeATM(20000, 100, 40, 50); // $20,000, 100x$100, 40x$50, 50x$10

        System.out.println("=== Scenario 1: Alice - Balance Inquiry ===");
        atm.setActiveUser(user1);
        atm.getCurrentATMState().insertCard(atm, user1.getCard());
        atm.getCurrentATMState().authenticatePin(atm, user1.getCard(), 1111);
        atm.getCurrentATMState().selectOperation(atm, TransactionType.BalanceInquiry);
        atm.getCurrentATMState().displayBalance(atm, user1.getCard());
        atm.getCurrentATMState().returnCard(atm);

        System.out.println("\n=== Scenario 2: Alice - Withdraw $500 ===");
        atm.setActiveUser(user1);
        atm.getCurrentATMState().insertCard(atm, user1.getCard());
        atm.getCurrentATMState().authenticatePin(atm, user1.getCard(), 1111);
        atm.getCurrentATMState().selectOperation(atm, TransactionType.CashWithdrawal);
        atm.getCurrentATMState().cashWithdrawal(atm, user1.getCard(), 500.0);
        atm.getCurrentATMState().returnCard(atm);

        System.out.println("\n=== Scenario 3: Bob - Transfer $1000 to Alice ===");
        atm.setActiveUser(user2);
        atm.getCurrentATMState().insertCard(atm, user2.getCard());
        atm.getCurrentATMState().authenticatePin(atm, user2.getCard(), 2222);
        atm.getCurrentATMState().selectOperation(atm, TransactionType.FundsTransfer);
        atm.getCurrentATMState().transferMoney(atm, user2.getCard(), acc1, 1000.0);
        atm.getCurrentATMState().returnCard(atm);

        System.out.println("\n=== Scenario 4: Bob - Change PIN ===");
        atm.setActiveUser(user2);
        atm.getCurrentATMState().insertCard(atm, user2.getCard());
        atm.getCurrentATMState().authenticatePin(atm, user2.getCard(), 2222);
        atm.getCurrentATMState().selectOperation(atm, TransactionType.ChangePIN);
        atm.getCurrentATMState().changePin(atm, user2.getCard(), 9999); // Bob sets new PIN to 9999
        atm.getCurrentATMState().returnCard(atm);

        System.out.println("\n=== Scenario 5: Alice - Cancel transaction after PIN ===");
        atm.setActiveUser(user1);
        atm.getCurrentATMState().insertCard(atm, user1.getCard());
        atm.getCurrentATMState().authenticatePin(atm, user1.getCard(), 1111);
        atm.getCurrentATMState().selectOperation(atm, TransactionType.Cancel);
        // Card returned, session ends
    }
}