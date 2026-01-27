package Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.system;

import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models.Address;
import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.search.Catalog;

public class Library {
    private static Library instance = null;
    private String name;
    private Address address;
    private Catalog catalog;
    
    private Library(String name, Address address) {
        this.name = name;
        this.address = address;
        this.catalog = new Catalog();
    }
    
    public static Library getInstance(String name, Address address) {
        if (instance == null) {
            instance = new Library(name, address);
        }
        return instance;
    }
    
    public String getName() { return name; }
    public String getAddress() { return address.toString(); }
    public Catalog getCatalog() { return catalog; }
}
