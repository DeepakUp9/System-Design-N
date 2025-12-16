import java.util.List;

public abstract class Person {
  private String name;
  private Address address;
  private String email;
  private String phone;
  private Account account;

  public Person(String name, Address address, String email, String phone, Account account) {
    this.name = name;
    this.address = address;
    this.email = email;
    this.phone = phone;
    this.account = account;
  }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public Address getAddress() { return address; }
  public void setAddress(Address address) { this.address = address; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }

  public Account getAccount() { return account; }
  public void setAccount(Account account) { this.account = account; }
  
  public abstract List<FlightInstance> viewSchedule();
}
