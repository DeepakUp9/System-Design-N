public abstract class Person {
    private String name;
    private Address address;
    private String email;
    private String phoneNumber;

    public void setName(String n) { name = n; }
    public String getName() { return name; }

    public void setEmail(String email) { this.email = email; }
    public String getEmail() { return email; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getPhoneNumber() { return phoneNumber; }

    public void setAddress(Address address) { this.address = address; }
    public Address getAddress() { return address; }
}
