public abstract class Account extends Person {
    private String accountId;
    private String password;
    private AccountStatus status;

    public Account() {} // For flexibility in the demo

    public Account(String name, Address address, String email, String phoneNumber,
                   String accountId, String password, AccountStatus status) {
        super.setName(name);
        super.setAddress(address);
        super.setEmail(email);
        super.setPhoneNumber(phoneNumber);
        this.accountId = accountId;
        this.password = password;
        this.status = status;
    }

    public String getAccountId() { return accountId; }
    public void setAccountId(String id) { accountId = id; }
    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus s) { status = s; }
    public void setPassword(String p) { password = p; }
    public String getPassword() { return password; }

    public abstract boolean resetPassword();
}
