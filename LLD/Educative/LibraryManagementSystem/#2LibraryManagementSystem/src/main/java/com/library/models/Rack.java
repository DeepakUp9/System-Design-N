package com.library.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entity representing a physical rack in the library.
 * Tracks the location of book items within the library.
 * Requirement R2: Physical location tracking.
 */
public class Rack {
    private final String rackNumber;
    private final String locationIdentifier; // e.g., "Floor 1, Section A"
    private final int shelfNumber;
    private final List<BookItem> bookItems;

    public Rack(String rackNumber, String locationIdentifier, int shelfNumber) {
        this.rackNumber = rackNumber;
        this.locationIdentifier = locationIdentifier;
        this.shelfNumber = shelfNumber;
        this.bookItems = new ArrayList<>();
    }

    public String getRackNumber() {
        return rackNumber;
    }

    public String getLocationIdentifier() {
        return locationIdentifier;
    }

    public int getShelfNumber() {
        return shelfNumber;
    }

    public List<BookItem> getBookItems() {
        return new ArrayList<>(bookItems);
    }

    public void addBookItem(BookItem bookItem) {
        if (!bookItems.contains(bookItem)) {
            bookItems.add(bookItem);
        }
    }

    public void removeBookItem(BookItem bookItem) {
        bookItems.remove(bookItem);
    }

    public String getFullLocation() {
        return locationIdentifier + " - Rack: " + rackNumber + ", Shelf: " + shelfNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rack rack = (Rack) o;
        return Objects.equals(rackNumber, rack.rackNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rackNumber);
    }

    @Override
    public String toString() {
        return "Rack{" +
                "rackNumber='" + rackNumber + '\'' +
                ", location='" + locationIdentifier + '\'' +
                ", shelf=" + shelfNumber +
                '}';
    }
}
