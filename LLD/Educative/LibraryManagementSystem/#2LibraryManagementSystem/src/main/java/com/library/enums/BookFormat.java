package com.library.enums;

/**
 * Enum representing different formats of library materials.
 */
public enum BookFormat {
    HARDCOVER("Hardcover book"),
    PAPERBACK("Paperback book"),
    AUDIOBOOK("Audio book"),
    EBOOK("Electronic book"),
    NEWSPAPER("Newspaper"),
    MAGAZINE("Magazine"),
    JOURNAL("Academic journal");

    private final String description;

    BookFormat(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
