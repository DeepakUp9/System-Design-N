package com.library.enums;

/**
 * Enum representing the status of a user account in the library system.
 */
public enum AccountStatus {
    ACTIVE("Account is active and in good standing"),
    CLOSED("Account is permanently closed"),
    CANCELLED("Account is cancelled by user"),
    BLACKLISTED("Account is blacklisted due to violations"),
    SUSPENDED("Account is temporarily suspended");

    private final String description;

    AccountStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
