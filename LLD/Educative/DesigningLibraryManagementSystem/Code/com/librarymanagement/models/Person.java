package Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models;

public class Person {
    private String name;
    private String email;
    private String phone;
    private Address address;
    
    public Person(String name, Address address, String email, String phone) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
    }
    
    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public Address getAddress() { return address; }
}
