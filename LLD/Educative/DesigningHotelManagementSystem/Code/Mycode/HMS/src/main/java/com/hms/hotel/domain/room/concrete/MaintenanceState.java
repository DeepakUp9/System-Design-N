package com.hms.hotel.domain.room.concrete;

import com.hms.hotel.domain.room.IRoomState;
import com.hms.hotel.entity.Room;

/**
 * LLD: State Pattern - Concrete State: UNDER_MAINTENANCE
 * Cannot be checked in or out. Only allows transition to VACANT when maintenance is finished.
 */
public class MaintenanceState implements IRoomState {

    @Override
    public String getName() {
        return "UNDER_MAINTENANCE";
    }

    @Override
    public String checkIn(Room room) {
        return "ERROR: Room " + room.getRoomNumber() + " is UNDER MAINTENANCE. Cannot be booked.";
    }

    @Override
    public String checkOut(Room room) {
        return "ERROR: Room " + room.getRoomNumber() + " is UNDER MAINTENANCE. No guest is checked in.";
    }

    @Override
    public String clean(Room room) {
        return "INFO: Room " + room.getRoomNumber() + " requires maintenance, cleaning is secondary.";
    }

    @Override
    public String putUnderMaintenance(Room room) {
        // State transition logic: Maintenance is finished
        room.setCurrentState(new VacantState());
        return "SUCCESS: Room " + room.getRoomNumber() + " maintenance is complete. State transitioned to **VACANT**.";
    }
}