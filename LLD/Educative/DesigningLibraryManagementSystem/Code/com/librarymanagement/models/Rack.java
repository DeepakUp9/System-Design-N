package Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models;

public class Rack {
    private int number;
    private String locationIdentifier;
    
    public Rack(int number, String locationIdentifier) {
        this.number = number;
        this.locationIdentifier = locationIdentifier;
    }
    
    public int getNumber() { return number; }
    public String getLocationIdentifier() { return locationIdentifier; }
}
