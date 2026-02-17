package com.librarymanagement.models;

/**
 * Author extends Person; used in Book (R3).
 * Structural integrity: Inheritance — Author extends Person. Association — Book has many Authors.
 * SOLID: LSP — Author substitutable for Person.
 */
public class Author extends Person {
    private final String description;

    public Author(String name, Address address, String email, String phone, String description) {
        super(name, address, email, phone);
        this.description = description;
    }

    public String getDescription() { return description; }
}
