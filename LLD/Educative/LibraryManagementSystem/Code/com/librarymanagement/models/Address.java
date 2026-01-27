package Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models;

public class Address {
    private String street;
    private String city;
    private String state;
    private String country;
    private int zipcode;
    
    public Address(String street, String city, String state, int zipcode, String country) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.country = country;
    }
    
    // Getters
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getCountry() { return country; }
    public int getZipcode() { return zipcode; }
    
    @Override
    public String toString() {
        return street + ", " + city + ", " + state + ", " + zipcode + ", " + country;
    }
}


