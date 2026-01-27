package Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.users;

import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.enums.AccountStatus;
import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models.LibraryCard;
import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models.Person;

public abstract class User {
    private String id;
    private String password;
    private AccountStatus status;
    private Person person;
    private LibraryCard card;

    public User(String id, String password, Person person, LibraryCard card) {
        this.id = id;
        this.password = password;
        this.person = person;
        this.card = card;
        this.status = AccountStatus.ACTIVE;
    }
    
    public boolean resetPassword() {
        System.out.println("Reset password for user " + id);
        return true;
    }
    
    // Getters and setters
    public String getId() { return id; }
    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }
    public Person getPerson() { return person; }
    public LibraryCard getCard() { return card; }
}

