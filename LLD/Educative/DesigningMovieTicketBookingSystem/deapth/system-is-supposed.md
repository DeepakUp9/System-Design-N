# Movie Ticket Booking System - Low Level Design

## Getting Ready: What the System Does

The platform enables users to discover movies across different cities, view showtimes at various cinemas, inspect real-time seat layouts, select available seats, and complete secure bookings through online payments with confirmation upon success.

The backend manages the hierarchical structure: Cities → Cinemas → Cinema Halls → Seats, along with Movies → Shows scheduled on specific dates and times.

## Core Engineering Focus Areas

### 1️⃣ Data Modeling of the Domain

The system must model:
- **Hierarchical structure**: Cities, cinemas, halls, movies, and shows
- **Seat layout per hall**: Defined once at setup
- **Show-wise seat availability**: Critically, availability must be tracked per show, not inferred from the hall layout. Each show instance has its own real-time availability state

### 2️⃣ Safe Seat Reservation Workflow

Multiple users attempting to book the same seats simultaneously creates concurrency challenges:
- **Temporary locking**: Seats must be locked during user selection to prevent other users from booking them
- **Double booking prevention**: No two users should confirm the same seat for the same show
- **Lock timeout management**: Locks must auto-release to prevent indefinite seat holds

### 3️⃣ Payment Completion Flow

The booking lifecycle depends on payment success:
- **Reservation precedes payment**: Users reserve seats first, then pay
- **Confirmation on payment success**: Bookings finalize only after successful payment
- **Automatic rollback on failure**: Reserved seats automatically release if payment fails

### 4️⃣ Notifications

The system sends notifications for:
- Ticket booking confirmations
- Payment confirmations
- Optional booking reminders

## Why This Problem Tests Critical Skills

Seat booking systems evaluate your ability to handle:

| Challenge | Expected Solution |
|-----------|-------------------|
| Multiple users selecting same seats | Correct distributed locking strategy |
| Real-time seat availability for UI | Fast and consistent read operations |
| Payment delays and failures | Reliable rollback mechanisms |
| Peak demand (big release weekends) | Scalability and performance optimization |

An interviewer uses this problem to assess your thinking on:
- Transaction management and ACID properties
- Distributed locking and optimistic concurrency control
- Caching strategies for high-read scenarios
- Idempotency and retry logic
- Trade-offs between consistency and availability

## Generic Applicability

This design pattern applies beyond movie tickets to any seat-based booking system:
- Concert ticketing
- Sports event ticketing
- Conference and event registration
- Bus, train, and flight reservations with seat selection

Understanding this LLD provides a reusable foundation for similar systems across multiple industries.