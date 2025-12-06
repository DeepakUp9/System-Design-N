package com.hms.hotel.domain.room.concrete;

import com.hms.hotel.domain.room.IRoomState;
import com.hms.hotel.entity.Room;

/**
 * LLD: State Pattern - Concrete State: CLEANING
 * Allows transition only to VACANT (when cleaning is finished) or MAINTENANCE (if damage is found).
 */
public class CleaningState implements IRoomState {

    @Override
    public String getName() {
        return "CLEANING";
    }

    @Override
    public String checkIn(Room room) {
        return "ERROR: Room " + room.getRoomNumber() + " is currently CLEANING. Cannot check in.";
    }

    @Override
    public String checkOut(Room room) {
        return "ERROR: Room " + room.getRoomNumber() + " is already in the cleaning cycle. Check-out is complete.";
    }

    @Override
    public String clean(Room room) {
        // State transition logic: Cleaning is finished
        room.setCurrentState(new VacantState());
        return "SUCCESS: Room " + room.getRoomNumber() + " cleaning is complete. State transitioned to **VACANT**.";
    }

    @Override
    public String putUnderMaintenance(Room room) {
        // State transition logic: Transition to maintenance if damage is discovered during cleaning
        room.setCurrentState(new MaintenanceState());
        return "SUCCESS: Room " + room.getRoomNumber() + " transitioning from CLEANING to **UNDER_MAINTENANCE** (damage found).";
    }
}