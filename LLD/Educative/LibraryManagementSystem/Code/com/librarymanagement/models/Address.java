package com.librarymanagement.models;

/**
 * Value object for address (Library and Person).
 * Structural integrity: Composition — Library and Person have-one Address.
 * Encapsulation: private final fields; immutable.
 */
public class Address {
    private final String street;
    private final String city;
    private final String state;
    private final int zipcode;
    private final String country;

    public Address(String street, String city, String state, int zipcode, String country) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.country = country;
    }

    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public int getZipcode() { return zipcode; }
    public String getCountry() { return country; }

    @Override
    public String toString() {
        return street + ", " + city + ", " + state + " " + zipcode + ", " + country;
    }
}
