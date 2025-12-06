package com.hms.hotel.security;

public enum Role {
    FRONT_DESK, // Can check-in/out guests, view bookings
    MANAGER,    // Can manage pricing, create rooms
    GUEST       // Can view their own bookings
}