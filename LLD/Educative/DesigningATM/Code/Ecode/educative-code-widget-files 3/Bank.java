import java.util.*;

public class Bank {
    private String name;
    private String bankCode;

    public Bank(String name, String code) {
        this.name = name;
        this.bankCode = code;
    }

    public String getBankCode() { return bankCode; }
}