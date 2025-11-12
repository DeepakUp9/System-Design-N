package Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models;

public class Author extends Person {
    private String description;
    
    public Author(String name, Address address, String email, String phone, String description) {
        super(name, address, email, phone);
        this.description = description;
    }
    
    public String getDescription() { return description; }
}
