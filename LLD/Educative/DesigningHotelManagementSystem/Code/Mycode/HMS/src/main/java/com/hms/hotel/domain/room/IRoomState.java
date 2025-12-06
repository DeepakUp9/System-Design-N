package com.hms.hotel.domain.room;

import com.hms.hotel.entity.Room;

/**
 * LLD: State Pattern - State Interface
 * Defines the contract for all concrete room states.
 * Methods represent actions that can be attempted on a room.
 */
public interface IRoomState {

    String getName();

    /**
     * Attempts to check a guest into the room.
     * @param room The Room object (Context) on which the action is performed.
     * @return A message indicating the result of the action.
     */
    String checkIn(Room room);

    /**
     * Attempts to check a guest out of the room.
     * @param room The Room object (Context) on which the action is performed.
     * @return A message indicating the result of the action.
     */
    String checkOut(Room room);

    /**
     * Attempts to clean the room.
     * @param room The Room object (Context).
     * @return A message indicating the result of the action.
     */
    String clean(Room room);

    /**
     * Attempts to put the room under maintenance.
     * @param room The Room object (Context).
     * @return A message indicating the result of the action.
     */
    String putUnderMaintenance(Room room);
}