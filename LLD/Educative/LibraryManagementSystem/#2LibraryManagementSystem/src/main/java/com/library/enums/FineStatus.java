package com.library.enums;

/**
 * Enum representing the status of a fine.
 */
public enum FineStatus {
    PENDING("Fine is pending payment"),
    PAID("Fine has been paid"),
    WAIVED("Fine has been waived by librarian");

    private final String description;

    FineStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
