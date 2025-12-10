public class Address {
  private int zipCode;
  private String address;
  private String city;
  private String state;
  private String country;

  public Address(int zipCode, String address, String city, String state, String country) {
    this.zipCode = zipCode;
    this.address = address;
    this.city = city;
    this.state = state;
    this.country = country;
  }

  public String getFullAddress() {
    return address + ", " + city + ", " + state + " " + zipCode + ", " + country;
  }

  public int getZipCode() { return zipCode; }
  public String getCity() { return city; }
  public String getState() { return state; }
  public String getCountry() { return country; }
}