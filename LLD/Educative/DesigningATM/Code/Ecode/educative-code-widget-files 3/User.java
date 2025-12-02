import java.util.*;

public class User {
    private ATMCard card;
    private BankAccount account;

    public User(ATMCard card, BankAccount account) {
        this.card = card;
        this.account = account;
    }

    public ATMCard getCard() { return card; }
    public BankAccount getAccount() { return account; }
}