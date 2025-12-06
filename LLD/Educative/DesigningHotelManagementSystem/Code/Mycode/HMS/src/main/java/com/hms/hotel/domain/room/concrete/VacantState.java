package com.hms.hotel.domain.room.concrete;

import com.hms.hotel.domain.room.IRoomState;
import com.hms.hotel.entity.Room;

/**
 * LLD: State Pattern - Concrete State
 * Represents a clean, ready-to-be-booked room.
 */
public class VacantState implements IRoomState {

    @Override
    public String getName() {
        return "VACANT";
    }

    @Override
    public String checkIn(Room room) {
        // State transition logic
        room.setCurrentState(new OccupiedState());
        return "SUCCESS: Room " + room.getRoomNumber() + " is now **OCCUPIED**. Check-in complete.";
    }

    @Override
    public String checkOut(Room room) {
        return "ERROR: Room " + room.getRoomNumber() + " is already VACANT. Cannot check out an empty room.";
    }

    @Override
    public String clean(Room room) {
        return "INFO: Room " + room.getRoomNumber() + " is already VACANT/CLEAN. No further cleaning needed.";
    }

    @Override
    public String putUnderMaintenance(Room room) {
        // State transition logic
        room.setCurrentState(new MaintenanceState());
        return "SUCCESS: Room " + room.getRoomNumber() + " is now **UNDER_MAINTENANCE**.";
    }
}