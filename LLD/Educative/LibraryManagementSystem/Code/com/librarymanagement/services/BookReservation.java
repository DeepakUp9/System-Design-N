package Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.enums.ReservationStatus;

public class BookReservation {
    private static Map<String, BookReservation> reservations = new HashMap<>();
    private String itemId;
    private Date creationDate;
    private ReservationStatus status;
    private String memberId;
    
    public BookReservation(String itemId, String memberId) {
        this.itemId = itemId;
        this.creationDate = new Date();
        this.status = ReservationStatus.PENDING;
        this.memberId = memberId;
        reservations.put(itemId, this);
    }
    
    public static BookReservation fetchReservationDetails(String bookItemId) {
        return reservations.get(bookItemId);
    }
    
    // Getters
    public String getItemId() { return itemId; }
    public Date getCreationDate() { return creationDate; }
    public ReservationStatus getStatus() { return status; }
    public String getMemberId() { return memberId; }
}

