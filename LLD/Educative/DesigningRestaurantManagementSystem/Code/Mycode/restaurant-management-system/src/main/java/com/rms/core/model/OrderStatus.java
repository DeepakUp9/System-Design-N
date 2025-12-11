package com.rms.core.model;

public enum OrderStatus {
    NEW,
    PROCESSING,
    READY_FOR_SERVICE, // Ready for Pickup/Delivery/Serve
    SERVED,
    DELIVERED,
    PAID,
    CANCELLED
}