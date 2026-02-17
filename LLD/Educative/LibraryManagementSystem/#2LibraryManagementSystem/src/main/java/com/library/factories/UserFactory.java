package com.library.factories;

import com.library.models.Address;
import com.library.models.Librarian;
import com.library.models.Member;
import com.library.models.User;

/**
 * Factory for creating User objects.
 * 
 * DESIGN PATTERN: Factory Pattern
 * - Encapsulates object creation logic
 * - Provides a single point of creation for User and its subclasses
 * - Hides complexity of object instantiation from clients
 * 
 * SOLID PRINCIPLE: Single Responsibility Principle
 * - Factory is responsible only for creating User objects
 * - Separates creation logic from business logic
 */
public class UserFactory {
    
    /**
     * Create a Member user.
     * 
     * @param name Member's name
     * @param email Member's email
     * @param phone Member's phone
     * @param address Member's address
     * @return New Member instance
     */
    public static Member createMember(String name, String email, String phone, Address address) {
        validateUserInput(name, email, phone, address);
        return new Member(name, email, phone, address);
    }
    
    /**
     * Create a Librarian user.
     * 
     * @param name Librarian's name
     * @param email Librarian's email
     * @param phone Librarian's phone
     * @param address Librarian's address
     * @param department Librarian's department
     * @return New Librarian instance
     */
    public static Librarian createLibrarian(String name, String email, String phone, 
                                           Address address, String department) {
        validateUserInput(name, email, phone, address);
        
        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be null or empty");
        }
        
        return new Librarian(name, email, phone, address, department);
    }
    
    /**
     * Validate common user input parameters.
     * 
     * @param name User's name
     * @param email User's email
     * @param phone User's phone
     * @param address User's address
     */
    private static void validateUserInput(String name, String email, String phone, Address address) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be null or empty");
        }
        
        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
    }
}
