package com.librarymanagement.users;

import com.librarymanagement.enums.AccountStatus;
import com.librarymanagement.models.LibraryCard;
import com.librarymanagement.models.Person;

/**
 * Abstract User (R5): id, password, Person, LibraryCard, AccountStatus.
 * Structural integrity: Inheritance — Librarian and Member extend User (LSP: substitutable as User).
 * Composition: User has-one Person, has-one LibraryCard.
 * Encapsulation: private fields; protected not used (subclasses use getters).
 * SOLID: LSP — Member and Librarian can replace User where base behaviour is used.
 */
public abstract class User {
    private final String id;
    private final String password;
    private AccountStatus status;
    private final Person person;
    private final LibraryCard card;

    public User(String id, String password, Person person, LibraryCard card) {
        this.id = id;
        this.password = password;
        this.person = person;
        this.card = card;
        this.status = AccountStatus.ACTIVE;
    }

    public boolean resetPassword() {
        return true;
    }

    public String getId() { return id; }
    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }
    public Person getPerson() { return person; }
    public LibraryCard getCard() { return card; }
}
