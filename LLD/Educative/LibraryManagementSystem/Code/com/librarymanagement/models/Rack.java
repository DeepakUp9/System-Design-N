package com.librarymanagement.models;

/**
 * Physical location of a book item in the library (R2).
 * Structural integrity: Composition — BookItem has-one Rack (location).
 * Encapsulation: private final fields.
 */
public class Rack {
    private final int number;
    private final String locationIdentifier;

    public Rack(int number, String locationIdentifier) {
        this.number = number;
        this.locationIdentifier = locationIdentifier;
    }

    public int getNumber() { return number; }
    public String getLocationIdentifier() { return locationIdentifier; }

    @Override
    public String toString() {
        return "Rack " + number + " (" + locationIdentifier + ")";
    }
}
