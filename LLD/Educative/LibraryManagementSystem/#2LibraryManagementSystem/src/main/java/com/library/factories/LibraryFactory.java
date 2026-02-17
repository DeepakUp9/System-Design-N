package com.library.factories;

import com.library.models.Address;
import com.library.models.Author;
import com.library.models.Rack;

/**
 * Factory for creating library-related supporting objects.
 * 
 * DESIGN PATTERN: Factory Pattern
 * - Provides centralized creation for supporting entities
 * - Ensures consistent object creation across the system
 */
public class LibraryFactory {
    
    /**
     * Create an Author.
     * 
     * @param name Author's name
     * @param description Author's description
     * @return New Author instance
     */
    public static Author createAuthor(String name, String description) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be null or empty");
        }
        
        String authorId = generateAuthorId(name);
        return new Author(authorId, name, description);
    }
    
    /**
     * Create a Rack for book storage.
     * Requirement R2: Track physical location in library.
     * 
     * @param rackNumber Unique rack number
     * @param locationIdentifier Human-readable location
     * @param shelfNumber Shelf number within the rack
     * @return New Rack instance
     */
    public static Rack createRack(String rackNumber, String locationIdentifier, int shelfNumber) {
        if (rackNumber == null || rackNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Rack number cannot be null or empty");
        }
        
        if (locationIdentifier == null || locationIdentifier.trim().isEmpty()) {
            throw new IllegalArgumentException("Location identifier cannot be null or empty");
        }
        
        if (shelfNumber < 1) {
            throw new IllegalArgumentException("Shelf number must be positive");
        }
        
        return new Rack(rackNumber, locationIdentifier, shelfNumber);
    }
    
    /**
     * Create an Address.
     * 
     * @param streetAddress Street address
     * @param city City
     * @param state State
     * @param zipCode Zip code
     * @param country Country
     * @return New Address instance
     */
    public static Address createAddress(String streetAddress, String city, String state, 
                                       String zipCode, String country) {
        validateAddressInput(streetAddress, city, state, zipCode, country);
        return new Address(streetAddress, city, state, zipCode, country);
    }
    
    /**
     * Generate a unique author ID based on name.
     * 
     * @param name Author's name
     * @return Generated author ID
     */
    private static String generateAuthorId(String name) {
        String cleanName = name.replaceAll("[^A-Za-z]", "").toUpperCase();
        String prefix = cleanName.length() >= 3 ? cleanName.substring(0, 3) : cleanName;
        return "AUTH-" + prefix + "-" + System.currentTimeMillis();
    }
    
    /**
     * Validate address input parameters.
     */
    private static void validateAddressInput(String streetAddress, String city, String state, 
                                            String zipCode, String country) {
        if (streetAddress == null || streetAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Street address cannot be null or empty");
        }
        
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be null or empty");
        }
        
        if (state == null || state.trim().isEmpty()) {
            throw new IllegalArgumentException("State cannot be null or empty");
        }
        
        if (zipCode == null || zipCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Zip code cannot be null or empty");
        }
        
        if (country == null || country.trim().isEmpty()) {
            throw new IllegalArgumentException("Country cannot be null or empty");
        }
    }
}
