package com.librarymanagement.factory;

import com.librarymanagement.models.LibraryCard;
import com.librarymanagement.models.Person;
import com.librarymanagement.users.Librarian;
import com.librarymanagement.users.Member;
import com.librarymanagement.users.User;

/**
 * Class diagram design pattern: Factory pattern.
 * Centralizes creation of Member and Librarian (User subtypes) with consistent setup.
 * Reference: Class Diagram for the Library Management System.md — "Factory pattern: UserFactory".
 */
public final class UserFactory {

    private UserFactory() {}

    /**
     * Create a Member with new LibraryCard (R5, R6).
     */
    public static Member createMember(String id, String password, Person person) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Member id required");
        if (person == null) throw new IllegalArgumentException("Person required");
        LibraryCard card = new LibraryCard();
        return new Member(id, password, person, card);
    }

    /**
     * Create a Librarian with new LibraryCard (R5, R6).
     */
    public static Librarian createLibrarian(String id, String password, Person person) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Librarian id required");
        if (person == null) throw new IllegalArgumentException("Person required");
        LibraryCard card = new LibraryCard();
        return new Librarian(id, password, person, card);
    }
}
