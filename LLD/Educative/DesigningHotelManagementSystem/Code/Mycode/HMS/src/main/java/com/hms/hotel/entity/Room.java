package com.hms.hotel.entity;

import com.hms.hotel.domain.room.IRoomState;
import com.hms.hotel.domain.room.concrete.*;
import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * LLD: State Pattern - Context (Integrated with JPA)
 * Holds the persistent state and delegates all status-changing actions to the currentState object.
 */
@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String roomNumber;

    @Column(nullable = false)
    private String roomType; // e.g., 'SINGLE', 'DOUBLE', 'SUITE'

    @Column(nullable = false)
    private BigDecimal basePrice;

    // --- State Pattern Fields ---

    /**
     * The persistent field storing the current state name (e.g., "VACANT", "OCCUPIED").
     * This is what is saved/loaded from PostgreSQL.
     */
    @Column(nullable = false)
    private String statusName;

    /**
     * The runtime LLD object (IRoomState) that encapsulates behavior.
     * This is NOT persisted by JPA. It is built from statusName when needed.
     */
    @Transient
    private IRoomState currentState;

    // --- Constructors, Getters, Setters (omitted for brevity) ---

    public Room() {
        // Default to Vacant on creation if not specified
        this.statusName = "VACANT";
    }

    // Custom constructor for easy creation
    public Room(String roomNumber, String roomType, BigDecimal basePrice) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.basePrice = basePrice;
        this.statusName = "VACANT";
    }

    // --- LLD Logic Delegation ---

    /**
     * Critical method: ensures the currentState object is initialized before use.
     */
    private IRoomState getCurrentStateObject() {
        if (this.currentState == null || !this.currentState.getName().equals(this.statusName)) {
            // Factory-like logic to instantiate the correct Concrete State based on the persistent statusName
            this.currentState = switch (this.statusName) {
                case "OCCUPIED" -> new OccupiedState();
                case "CLEANING" -> new CleaningState();
                case "UNDER_MAINTENANCE" -> new MaintenanceState();
                // Default or unknown state
                default -> new VacantState();
            };
        }
        return this.currentState;
    }

    /**
     * Setter used by the Concrete State classes to transition the Room's state.
     * This also updates the persistent statusName field.
     */
    public void setCurrentState(IRoomState newState) {
        this.currentState = newState;
        this.statusName = newState.getName();
    }

    // Delegate methods for all Room actions:

    public String performCheckIn() {
        return getCurrentStateObject().checkIn(this);
    }

    public String performCheckOut() {
        return getCurrentStateObject().checkOut(this);
    }

    public String performClean() {
        return getCurrentStateObject().clean(this);
    }

    public String performPutUnderMaintenance() {
        return getCurrentStateObject().putUnderMaintenance(this);
    }

    // Getters and Setters for JPA/Spring (Required by JPA)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }
    public String getStatusName() { return statusName; }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
        this.currentState = null; // Forces re-creation via getCurrentStateObject() if needed
    }
}