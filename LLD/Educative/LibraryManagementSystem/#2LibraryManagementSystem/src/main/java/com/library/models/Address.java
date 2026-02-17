package com.library.models;

/**
 * Value Object representing a physical address.
 * Immutable class following Value Object pattern.
 */
public final class Address {
    private final String streetAddress;
    private final String city;
    private final String state;
    private final String zipCode;
    private final String country;

    public Address(String streetAddress, String city, String state, String zipCode, String country) {
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return streetAddress + ", " + city + ", " + state + " " + zipCode + ", " + country;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Address address = (Address) obj;
        return streetAddress.equals(address.streetAddress) &&
               city.equals(address.city) &&
               state.equals(address.state) &&
               zipCode.equals(address.zipCode) &&
               country.equals(address.country);
    }

    @Override
    public int hashCode() {
        int result = streetAddress.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + state.hashCode();
        result = 31 * result + zipCode.hashCode();
        result = 31 * result + country.hashCode();
        return result;
    }
}
