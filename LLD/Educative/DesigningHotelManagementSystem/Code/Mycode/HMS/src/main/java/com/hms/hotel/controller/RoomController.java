package com.hms.hotel.controller;

import com.hms.hotel.entity.Room;
import com.hms.hotel.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Endpoint 1: Retrieve room status
    @GetMapping("/{roomNumber}")
    public ResponseEntity<Room> getRoomDetails(@PathVariable String roomNumber) {
        Room room = roomService.getRoomDetails(roomNumber);
        return ResponseEntity.ok(room);
    }

    // Endpoint 2: Action - Check In (Delegates to State Pattern)
    @PostMapping("/{roomNumber}/checkin")
    public ResponseEntity<String> checkIn(@PathVariable String roomNumber) {
        // The service layer calls room.performCheckIn(), which uses the current IRoomState
        String result = roomService.checkInRoom(roomNumber);
        return ResponseEntity.ok(result);
    }

    // Endpoint 3: Action - Check Out (Delegates to State Pattern)
    @PostMapping("/{roomNumber}/checkout")
    public ResponseEntity<String> checkOut(@PathVariable String roomNumber) {
        // Successful checkout transitions the room to CleaningState
        String result = roomService.checkOutRoom(roomNumber);
        return ResponseEntity.ok(result);
    }

    // Endpoint 4: Action - Clean Room (Delegates to State Pattern)
    @PostMapping("/{roomNumber}/clean")
    public ResponseEntity<String> clean(@PathVariable String roomNumber) {
        // Successful cleaning transitions the room to VacantState
        String result = roomService.cleanRoom(roomNumber);
        return ResponseEntity.ok(result);
    }

    // Endpoint 5: Room Creation (Simple CRUD, requires a DTO in a full system)
    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room newRoom) {
        Room room = roomService.createRoom(newRoom);
        return ResponseEntity.status(201).body(room);
    }
}