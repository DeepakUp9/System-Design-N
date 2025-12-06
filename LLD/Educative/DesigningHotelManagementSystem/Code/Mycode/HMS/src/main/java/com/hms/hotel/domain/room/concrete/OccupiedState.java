package com.hms.hotel.domain.room.concrete;

import com.hms.hotel.domain.room.IRoomState;
import com.hms.hotel.entity.Room;

/**
 * LLD: State Pattern - Concrete State
 * Represents a room currently holding a guest.
 */
public class OccupiedState implements IRoomState {

    @Override
    public String getName() {
        return "OCCUPIED";
    }

    @Override
    public String checkIn(Room room) {
        return "ERROR: Room " + room.getRoomNumber() + " is currently OCCUPIED. Cannot check in a new guest.";
    }

    @Override
    public String checkOut(Room room) {
        // State transition logic: Must be cleaned after checkout.
        room.setCurrentState(new CleaningState());
        return "SUCCESS: Room " + room.getRoomNumber() + " is now **CLEANING**. Check-out complete.";
    }

    @Override
    public String clean(Room room) {
        return "ERROR: Room " + room.getRoomNumber() + " is OCCUPIED. Cannot clean while guest is present.";
    }

    @Override
    public String putUnderMaintenance(Room room) {
        // Edge Case: Requires moving the guest or logging a critical task.
        return "ALERT: Room " + room.getRoomNumber() + " is OCCUPIED. Cannot put under maintenance until vacant.";
    }
}