package com.librarymanagement.models;

/**
 * Base person data: name, email, phone, address (used by User and Author).
 * Structural integrity: Composition — Person has-one Address. Inheritance — Author extends Person.
 * SOLID: SRP — person identity and contact only.
 */
public class Person {
    private final String name;
    private final String email;
    private final String phone;
    private final Address address;

    public Person(String name, Address address, String email, String phone) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public Address getAddress() { return address; }
}
