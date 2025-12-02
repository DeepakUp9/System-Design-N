import java.util.*;

public class CardReader {
    public boolean readCard(ATMCard card) {
        System.out.println("[ATM] Reading card: " + card.getCardNumber());
        return card != null;
    }
}