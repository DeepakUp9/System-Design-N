public class Address {
    private String streetAddress;
    private String city;
    private String state;
    private int zipCode;
    private String country;

    public Address(String street, String city, String state, int zip, String country) {
        this.streetAddress = street;
        this.city = city;
        this.state = state;
        this.zipCode = zip;
        this.country = country;
    }

    public String getStreetAddress() { return streetAddress; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public int getZipCode() { return zipCode; }
    public String getCountry() { return country; }

    @Override
    public String toString() {
        return streetAddress + ", " + city + ", " + state + " " + zipCode + ", " + country;
    }
}
