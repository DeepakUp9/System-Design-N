package Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models;

import java.util.Date;

public class LibraryCard {
    private String cardNumber;
    private Date issued;
    private boolean active;
    
    public LibraryCard(String cardNumber, Date issued) {
        this.cardNumber = cardNumber;
        this.issued = issued;
        this.active = true;
    }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public String getCardNumber() { return cardNumber; }
    public Date getIssued() { return issued; }
}
