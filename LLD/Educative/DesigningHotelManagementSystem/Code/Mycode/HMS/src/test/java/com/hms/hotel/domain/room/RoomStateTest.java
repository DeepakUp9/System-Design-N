package com.hms.hotel.domain.room;

import com.hms.hotel.entity.Room;
import com.hms.hotel.domain.room.concrete.OccupiedState;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the core, decoupled State Pattern logic.
 * These tests ensure the LLD structure behaves as expected.
 */
public class RoomStateTest {

    private Room createTestRoom() {
        // Initializes Room with a default VacantState
        return new Room("101", "SINGLE", new BigDecimal("150.00"));
    }

    @Test
    void testInitialStateIsVacant() {
        Room room = createTestRoom();
        // The persistent field should match the default state name
        assertEquals("VACANT", room.getStatusName());
    }

    @Test
    void testVacantToOccupiedTransition() {
        Room room = createTestRoom(); // Starts Vacant

        // 1. Perform Check-in action
        String result = room.performCheckIn();

        // 2. Assert success message and state transition
        assertTrue(result.contains("SUCCESS"));
        assertEquals("OCCUPIED", room.getStatusName());
       // assertTrue(room.getCurrentStateObject() instanceof OccupiedState);
    }

    @Test
    void testInvalidActionOnOccupiedState() {
        Room room = createTestRoom();
        room.performCheckIn(); // Transition to OCCUPIED

        // Attempting to check in an already occupied room
        String result = room.performCheckIn();

        // Assert failure message and that the state did NOT change
        assertTrue(result.contains("ERROR: Room 101 is currently OCCUPIED"));
        assertEquals("OCCUPIED", room.getStatusName());
    }

    @Test
    void testOccupiedToCleaningTransition() {
        Room room = createTestRoom();
        room.performCheckIn(); // Vacant -> OCCUPIED

        // Perform Check-out action
        String result = room.performCheckOut();

        // Assert success and transition to CLEANING state
        assertTrue(result.contains("SUCCESS"));
        assertEquals("CLEANING", room.getStatusName());
        // We can similarly test the transition from Cleaning to Vacant, etc.
    }
}