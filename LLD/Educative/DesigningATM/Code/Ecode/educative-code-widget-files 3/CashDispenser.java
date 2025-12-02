import java.util.*;

public class CashDispenser {
    public boolean dispenseCash(int amount, ATM atm) {
        if (amount > 0 && amount <= atm.getAtmBalance()) {
            // Try to dispense bills in denominations (simple greedy)
            int left = amount;
            int hundreds = Math.min(left / 100, atm.getNoOfHundredDollarBills());
            left -= hundreds * 100;
            int fifties = Math.min(left / 50, atm.getNoOfFiftyDollarBills());
            left -= fifties * 50;
            int tens = Math.min(left / 10, atm.getNoOfTenDollarBills());
            left -= tens * 10;
            if (left == 0) {
                atm.setNoOfHundredDollarBills(atm.getNoOfHundredDollarBills() - hundreds);
                atm.setNoOfFiftyDollarBills(atm.getNoOfFiftyDollarBills() - fifties);
                atm.setNoOfTenDollarBills(atm.getNoOfTenDollarBills() - tens);
                atm.setAtmBalance(atm.getAtmBalance() - amount);
                System.out.println("[ATM] Dispensing $" + amount + " as: " +
                    hundreds + "x$100, " + fifties + "x$50, " + tens + "x$10.");
                return true;
            }
        }
        System.out.println("[ATM] Unable to dispense the requested cash. Insufficient bills or balance.");
        return false;
    }
}
