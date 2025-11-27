# Movie Ticket Booking System - Requirements Analysis

## Introduction

Instead of just repeating each requirement, we'll deeply understand:

- What each requirement means
- Why it's important
- What design impact it has
- What interviewer might cross-question

**This will help you remember logically, not by memory.**

---

## Requirement-by-Requirement Breakdown

### R1: Multiple Cinemas in Different Cities with Multiple Halls

**Meaning**:
The system is geographically distributed and hierarchical.

**Design Impact**:
- Entities must support City → Cinema → Hall relationship
- Search must support city filtering
- Data model needs proper normalization to avoid redundancy

**Common Interview Cross-Question**:
> "Can the same cinema chain operate in different cities?"

**Expected Answer**:
Yes, but each location is treated as a separate cinema instance. For example, PVR in Delhi and PVR in Mumbai are two different Cinema entities, both linked to their respective City entities.

---

### R2: Multiple Shows per Movie, One Show per Hall at a Time

**Meaning**:
Many shows per movie is allowed, but time overlap in the same hall is forbidden.

**Design Impact**:
- Show entity must validate time conflicts when adding shows
- Admin panel must check hall availability before scheduling
- Need business logic to prevent double-booking at admin level

**Interview Trap Question**:
> "Can two shows run in the same hall if one ends at 6:00 PM and another starts at 6:00 PM?"

**Expected Answer**:
Depends on the business requirement. In real cinemas, there's typically a buffer time for cleaning and technical setup (10-30 minutes). You should clarify this with the interviewer: "I would assume there's a buffer period. Should we add 15 minutes between shows?"

---

### R3: Display All Available Showtimes for Selected Movie Across All Cinemas

**Meaning**:
A user shouldn't search cinema-by-cinema manually. They should see all options for a movie in one view.

**Design Impact**:
- Need global show searching by movie
- Indexing (Elasticsearch / optimized DB queries) helps scale
- Can use denormalized data or caching for fast retrieval
- Aggregation of shows across multiple cinema instances

---

### R4: Search Movies by Title, Language, Genre, or Release Date

**Meaning**:
Search must support multiple filters simultaneously.

**Design Impact**:
- Movie table needs attributes: title, language, genre, release_date
- Search service must support multiple query parameters
- Consider search indexing for performance
- Support filter combinations: "Hindi movies released in 2024"

---

### R5: Book Seats for Any Available Showtime at Any Cinema Hall

**Meaning**:
Booking is not restricted to local customers. Users can book from anywhere for any cinema.

**Design Impact**:
- UI/DB layer must support bookings for any show across cities
- No geographic restrictions
- Requires proper authentication and payment support

---

### R6: Bookings Online or Via Ticket Agent

**Meaning**:
Two types of users exist:
- Normal online customers (self-service)
- Agents booking on behalf of customers in cinema (staff)

**Design Impact**:
- User types / roles (CUSTOMER, AGENT, ADMIN)
- Audit logs must track agent ID for accountability
- Different workflows for each user type
- Authorization rules based on role

---

### R7: Payment Methods Based on Booking Mode

**Meaning**:
- Online customers: credit card only
- In-person customers: cash or credit card

**Design Impact**:
- Strategy pattern for payment methods
- Validation logic based on user type and booking mode
- Payment gateway integration for online
- Cash handling system for in-person

**Critical Point**:
Seat must NOT be permanently booked until payment clears. This is fundamental to avoiding fraud and double-booking.

---

### R8: Select Multiple Seats in One Booking Transaction

**Meaning**:
Cart-style system — user can reserve multiple seats and pay once for the entire group.

**Design Impact**:
- Ability to lock multiple seats at once (atomic operation)
- If payment fails: release ALL selected seats together
- Group booking becomes easier
- Payment total = sum of all seat prices

**Example Scenario**:
```
User selects: Seat A5, A6, A7 (3 seats)
    ↓
All 3 seats TEMP_HOLD
    ↓
User pays for all 3
    ↓
All 3 become BOOKED (together)
OR
Payment fails → All 3 released (together)
```

---

### R9: Seat Categories with Fixed Costs

**Meaning**:
Seats are categorized into Silver, Gold, and Platinum. Each category has a fixed price.

**Design Impact**:
- ShowSeat must contain seat_type attribute
- Pricing engine to calculate totals based on seat type
- Different prices for different shows possible (dynamic pricing)
- Display seat type to user before booking

**Schema Example**:
```
ShowSeat:
  - id
  - show_id
  - seat_no (A5)
  - seat_type (SILVER / GOLD / PLATINUM)
  - price (linked to category)
  - status (AVAILABLE / TEMP_HOLD / BOOKED)
```

---

### R10: Each Seat = One Ticket Only

**Meaning**:
A seat cannot belong to two people. One-to-one mapping between seat and ticket.

**Design Impact**:
- Exclusive locking mechanism
- Status tracking: AVAILABLE → TEMP_HOLD → BOOKED
- No sharing or partial booking of a seat
- Clear ownership after booking

---

### R11: Prevent Double-Booking for Same Seat

**Meaning**:
Concurrency protection is mandatory. System must guarantee only one person gets a seat.

**Design Impact**:
- Must handle race conditions
- Approaches: distributed lock / optimistic concurrency / row-level lock
- Timestamp-based auto-release for expired holds
- Handle case: both users click "confirm" at same millisecond → only one wins

**Critical Scenario**:
```
User A: clicks confirm at T=0ms for Seat A5
User B: clicks confirm at T=0ms for Seat A5 (SAME millisecond)
    ↓
Without locking: Both might get seat ❌
With locking: Only one gets seat ✅
```

---

### R12: Admin Users Can Manage Shows and Movies

**Meaning**:
Back-office management system required for administrators to add, delete, and update shows and movies.

**Design Impact**:
- Admin role with authorization checks
- Validation for hall availability when adding shows
- Audit trail for admin actions
- Separate admin API endpoints with role-based access control

**Validations Needed**:
- Cannot add show if hall is occupied during that time
- Cannot delete movie if active shows exist
- Cannot update show details if booking already started

---

### R13: Visually Distinguish Available vs Booked Seats

**Meaning**:
UI must always reflect current seat status in real-time. Users should see which seats are available and which are taken.

**Design Impact**:
- ShowSeat status must be stored and updated
- WebSocket / Server-Sent Events (SSE) / polling for live UI updates
- Cache seat status for fast retrieval
- Real-time sync across multiple browsers

**User Experience**:
```
User opens seat map
    ↓
Shows: GREEN (available), RED (booked), YELLOW (on hold)
    ↓
Another user books a seat
    ↓
That seat instantly turns RED for all viewing users
```

---

### R14: Send Notifications for Key Events

**Meaning**:
System must notify users for:
- New movie release to subscribed users
- Booking made
- Booking canceled

**Design Impact**:
- Observer / Event-driven notification system (email/SMS)
- Notification microservice is ideal for scalability
- Queue system (Kafka / RabbitMQ) for reliable delivery
- Template engine for notification content

**Example Flow**:
```
Booking confirmed
    ↓
Event: BOOKING_CONFIRMED published
    ↓
Notification service listens
    ↓
Sends email + SMS to user
    ↓
User receives confirmation
```

---

### R15: Notification When Booking is Modified

**Meaning**:
Any changes to booking (cancellation, date change, etc.) should trigger notifications to the customer.

**Design Impact**:
- Every booking update triggers notification event
- Track what changed (for audit trail)
- Send only relevant information to user
- Support multiple notification channels (email, SMS, push)

---

## Hidden Requirements (Not Explicitly Listed But Expected)

These questions impress interviewers and show real-world thinking:

### Q1: What happens when payment fails?

**Expected Answer**:
Seats should auto-release immediately. A background job sweeps for expired holds every minute to ensure seats don't stay locked forever.

**Design Impact**:
- Implement retry logic with backoff
- Idempotency key to prevent duplicate charges
- Clear error messages to user
- Option to retry payment

### Q2: Should users be able to cancel bookings?

**Expected Answer**:
Yes, but with rules:
- Can cancel before a certain time (e.g., 24 hours before show)
- Cancellation charges may apply
- Refund should be processed
- Seat becomes available again

**Design Impact**:
- Cancellation status in Booking entity
- Refund workflow (reverse payment)
- Seat release logic for canceled bookings

### Q3: Should there be booking history?

**Expected Answer**:
Yes. Users should see:
- Past bookings (with ticket details)
- Upcoming bookings
- Booking status (confirmed, cancelled, expired)

**Design Impact**:
- Booking archive table
- Query optimization for user's booking history
- Display QR code / booking ID for reference

### Q4: What time window to complete payment?

**Expected Answer**:
Most systems give 10–15 minutes to complete payment after seat selection. This balances user experience with preventing long seat holds.

**Design Impact**:
- TEMP_HOLD duration configurable (typically 15 minutes)
- Timer displayed to user: "Complete payment in 14:32"
- Auto-release seats if timer expires
- Option to extend hold if paying from far location

### Q5: Should QR codes be generated for entry?

**Expected Answer**:
Yes. Many modern booking systems do. QR codes contain:
- Booking ID
- Movie details
- Show timing
- Seat numbers

**Design Impact**:
- Generate QR code after booking confirmation
- Display in email, SMS, and app
- Scanner at cinema for entry verification
- Prevents ticket fraud

---

## Why Understanding This Is Important

**If you memorize requirements**:
- You might forget in interview
- Cannot answer follow-up questions
- Sound robotic

**If you understand why each requirement exists**:
- You never forget
- Can answer cross-questions confidently
- Sound like an experienced engineer
- Can make trade-off decisions
- Can suggest improvements

---

## Key Takeaways

✅ Requirements shape entities (City, Cinema, Hall, Movie, Show, ShowSeat, Booking, Payment, User)

✅ Requirements drive design decisions (concurrency, payment flow, notification system)

✅ Requirements validate your design (if design violates requirement, it's wrong)

✅ Understanding WHY > memorizing WHAT

✅ Be ready for hidden requirements — they show real-world thinking