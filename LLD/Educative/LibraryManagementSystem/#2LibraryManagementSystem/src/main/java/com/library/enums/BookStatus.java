package com.library.enums;

/**
 * Enum representing the status of a book item in the library.
 * Used to track the current state of physical book copies.
 */
public enum BookStatus {
    AVAILABLE("Available for checkout"),
    BORROWED("Currently borrowed by a member"),
    RESERVED("Reserved for a member"),
    LOST("Book is lost"),
    DAMAGED("Book is damaged and under repair"),
    UNDER_REPAIR("Book is being repaired");

    private final String description;

    BookStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
