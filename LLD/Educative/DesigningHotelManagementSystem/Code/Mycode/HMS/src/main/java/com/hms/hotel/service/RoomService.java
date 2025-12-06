package com.hms.hotel.service;

import com.hms.hotel.entity.Room;
import com.hms.hotel.repository.RoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // Edge Case: Room not found handling
    private Room getRoomOrThrow(String roomNumber) {
        return roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomNumber));
    }

    /**
     * Core business logic: Delegates the check-in action to the Room entity (Context).
     */
    @Transactional // Ensures state changes are saved to the DB
    public String checkInRoom(String roomNumber) {
        Room room = getRoomOrThrow(roomNumber);

        // The LLD magic happens here: the Room delegates to its current state object
        String result = room.performCheckIn();

        // Save the Room (which now has an updated 'statusName' field)
        roomRepository.save(room);
        return result;
    }

    @Transactional
    public String checkOutRoom(String roomNumber) {
        Room room = getRoomOrThrow(roomNumber);
        String result = room.performCheckOut();
        roomRepository.save(room);
        return result;
    }

    @Transactional
    public String cleanRoom(String roomNumber) {
        Room room = getRoomOrThrow(roomNumber);
        String result = room.performClean();
        roomRepository.save(room);
        return result;
    }

    // Basic CRUD methods
    public Room createRoom(Room newRoom) {
        return roomRepository.save(newRoom);
    }

    public Room getRoomDetails(String roomNumber) {
        return getRoomOrThrow(roomNumber);
    }
}