package com.library.models;

/**
 * Entity representing a librarian with administrative privileges.
 * Librarians can manage books, members, and perform all library operations.
 * 
 * SOLID PRINCIPLE: Liskov Substitution Principle
 * - Librarian can be used anywhere User is expected
 * - Extends User behavior without breaking contracts
 */
public class Librarian extends User {
    private final String employeeId;
    private String department;
    private double salary;

    public Librarian(String name, String email, String phone, Address address, String department) {
        super(name, email, phone, address);
        this.employeeId = generateEmployeeId();
        this.department = department;
    }

    private String generateEmployeeId() {
        return "EMP-" + System.currentTimeMillis();
    }

    @Override
    public boolean canBorrowBooks() {
        return true; // Librarians can also borrow books
    }

    @Override
    public boolean canAddBooks() {
        return true; // Librarians can add books to catalog
    }

    @Override
    public boolean canDeleteBooks() {
        return true; // Librarians can remove books from catalog
    }

    @Override
    public String getUserType() {
        return "LIBRARIAN";
    }

    // Getters and setters
    public String getEmployeeId() {
        return employeeId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    // Librarian-specific operations (delegated to services in actual implementation)
    public boolean canIssueFine() {
        return isActive();
    }

    public boolean canManageMembers() {
        return isActive();
    }

    public boolean canManageCatalog() {
        return isActive();
    }

    @Override
    public String toString() {
        return "Librarian{" +
                "employeeId='" + employeeId + '\'' +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", accountStatus=" + accountStatus +
                '}';
    }
}
