import java.util.*;

public class ATM {
    private static ATM atmObject = new ATM();
    private ATMState currentATMState;
    private ATMStatus atmStatus;
    private int atmBalance;
    private int noOfHundredDollarBills;
    private int noOfFiftyDollarBills;
    private int noOfTenDollarBills;

    private CardReader cardReader;
    private CashDispenser cashDispenser;
    private Keypad keypad;
    private Screen screen;
    private Printer printer;

    // Session variables
    private User activeUser;
    private ATMCard insertedCard;
    private boolean authenticated;

    private ATM() {
        this.currentATMState = new IdleState();
        this.atmStatus = ATMStatus.Idle;
        this.cardReader = new CardReader();
        this.cashDispenser = new CashDispenser();
        this.keypad = new Keypad();
        this.screen = new Screen();
        this.printer = new Printer();
    }

    public static ATM getInstance() {
        return atmObject;
    }

    public void displayCurrentState() {
        System.out.println("[ATM] Status: " + atmStatus);
    }

    public void initializeATM(int atmBalance, int noOfHundred, int noOfFifty, int noOfTen) {
        this.atmBalance = atmBalance;
        this.noOfHundredDollarBills = noOfHundred;
        this.noOfFiftyDollarBills = noOfFifty;
        this.noOfTenDollarBills = noOfTen;
        this.atmStatus = ATMStatus.Idle;
        this.currentATMState = new IdleState();
        this.activeUser = null;
        this.insertedCard = null;
        this.authenticated = false;
    }

    // Getters and setters
    public ATMState getCurrentATMState() { return currentATMState; }
    public void setCurrentATMState(ATMState s) { this.currentATMState = s; }
    public ATMStatus getAtmStatus() { return atmStatus; }
    public void setAtmStatus(ATMStatus status) { this.atmStatus = status; }
    public int getAtmBalance() { return atmBalance; }
    public void setAtmBalance(int balance) { this.atmBalance = balance; }
    public int getNoOfHundredDollarBills() { return noOfHundredDollarBills; }
    public void setNoOfHundredDollarBills(int n) { this.noOfHundredDollarBills = n; }
    public int getNoOfFiftyDollarBills() { return noOfFiftyDollarBills; }
    public void setNoOfFiftyDollarBills(int n) { this.noOfFiftyDollarBills = n; }
    public int getNoOfTenDollarBills() { return noOfTenDollarBills; }
    public void setNoOfTenDollarBills(int n) { this.noOfTenDollarBills = n; }
    public CardReader getCardReader() { return cardReader; }
    public CashDispenser getCashDispenser() { return cashDispenser; }
    public Keypad getKeypad() { return keypad; }
    public Screen getScreen() { return screen; }
    public Printer getPrinter() { return printer; }
    public User getActiveUser() { return activeUser; }
    public void setActiveUser(User user) { this.activeUser = user; }
    public ATMCard getInsertedCard() { return insertedCard; }
    public void setInsertedCard(ATMCard card) { this.insertedCard = card; }
    public boolean isAuthenticated() { return authenticated; }
    public void setAuthenticated(boolean auth) { this.authenticated = auth; }
}