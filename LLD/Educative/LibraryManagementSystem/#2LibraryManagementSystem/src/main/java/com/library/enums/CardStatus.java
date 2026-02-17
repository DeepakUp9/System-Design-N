package com.library.enums;

/**
 * Enum representing the status of a library card.
 */
public enum CardStatus {
    ACTIVE("Card is active"),
    SUSPENDED("Card is suspended"),
    EXPIRED("Card has expired"),
    LOST("Card is reported lost"),
    BLOCKED("Card is blocked");

    private final String description;

    CardStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
