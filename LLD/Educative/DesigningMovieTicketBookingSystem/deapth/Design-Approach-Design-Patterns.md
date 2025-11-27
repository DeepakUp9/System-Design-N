# Movie Ticket Booking System - Design Approach (Bottom-Up)

## Why Bottom-Up?

Because instead of jumping directly to "APIs + workflows," we first build the base foundation:

**Identify entities → Define responsibilities → Combine them to create features**

This makes the design clean, scalable, and follows OOP principles naturally.

---

## Step 1: Identify Core Entities

These are the building blocks of the system:

| Entity | Meaning / Responsibility |
|--------|-------------------------|
| City | Has many cinemas |
| Cinema | Has multiple cinema halls |
| CinemaHall | Has seating layout (static) |
| Movie | Information about a film |
| Show | A movie playing at a hall at specific time |
| ShowSeat | Seat availability for a show (dynamic) |
| User | Customer using the system |
| Booking | Holds selected seats + payment status |
| Payment | Tracks payment transaction |

### Important Rule for Beginners

**Hall seats ≠ Show seats**

- **Hall seats** = permanent (static layout)
- **Show seats** = created per show to track availability (dynamic)

This distinction is critical for avoiding double-booking bugs.

### Why This Separation Matters

**Hall entity example**:
```
Hall A: 100 seats (fixed forever)
- Seat layout never changes
- Physical structure is permanent
```

**ShowSeat entity example**:
```
Show 1 (Movie X, 2PM): ShowSeats created with status AVAILABLE
Show 2 (Movie X, 5PM): Different ShowSeats created for SAME hall
Show 3 (Movie Y, 8PM): Another set of ShowSeats
```

If you modify Hall directly when someone books a seat, **Show 2 and Show 3 will also lose that seat** — causing double-booking errors.

**Correct approach**: Each Show has its own ShowSeat records that are independent.

---

## Step 2: Model User Flow Using Entities

**Real-world flow**:

```
User → City → Cinema → Hall → Show → ShowSeats
    ↓
User selects seats → System blocks seats temporarily → User pays → Booking confirmed
```

This mapping ensures you understand:
- Which entity interacts with which
- In what order

### Entity Relationships Explained

**Why this hierarchy?**

- **User searches** → City (Which city?)
  - **User selects cinema** → Cinema (Which theater chain?)
    - **User sees halls** → CinemaHall (Which screen?)
      - **User picks show** → Show (Which timing?)
        - **User selects seats** → ShowSeat (Which seats? What status?)

Each step narrows down the options until user gets to available seats for that specific show.

---

## Step 3: Add Concurrency Control

We already learned locking strategies earlier. At this step, we apply them:

| Stage | Rule |
|-------|------|
| Seat selection | Apply lock / optimistic concurrency / distributed lock |
| Payment in progress | Seat remains in TEMP_HOLD |
| Payment success | Seat status = BOOKED |
| Payment failure / timeout | Seat released automatically |

**This proves to interviewer**: You understand real-world problems and not just class diagrams.

### How Concurrency Control Works Per Entity

**ShowSeat entity during concurrent booking**:

```
Multiple users try to book Seat A5:

Thread 1: ShowSeat.status = AVAILABLE
Thread 2: ShowSeat.status = AVAILABLE
    ↓
Both try to update to TEMP_HOLD (PROBLEM!)
    ↓
Without lock: Both succeed → double booking ❌
With lock: Only one succeeds, other gets conflict error ✅
```

**Why apply at this step?**

Because you now understand which entity (ShowSeat) needs protection and when (during selection).

---

## Step 4: Integrate Payments

Payment service sends webhook callbacks. We update booking only when payment returns success to avoid fraud.

| Booking Status | Meaning |
|----------------|---------|
| INITIATED | Seats selected |
| PAYMENT_PENDING | Payment started |
| PAYMENT_SUCCESS | Ticket confirmed |
| PAYMENT_FAILED | Seats released |

**Idempotency is crucial** here to avoid duplicate success updates.

### Payment Integration with Entities

**Booking entity lifecycle**:

```
User selects seats
    ↓
Booking created: status = INITIATED
ShowSeats status = TEMP_HOLD
    ↓
User clicks Pay
    ↓
Booking: status = PAYMENT_PENDING
Payment entity created: status = IN_PROGRESS
    ↓
Payment gateway responds (async)
    ↓
If success:
  - Payment: status = SUCCESS
  - Booking: status = PAYMENT_SUCCESS
  - ShowSeats: status = BOOKED (permanent)
    
If failure:
  - Payment: status = FAILED
  - Booking: status = PAYMENT_FAILED
  - ShowSeats: status = AVAILABLE (released)
```

**Why separate entities?**

- **Booking**: Tracks user's intent and seat selection
- **Payment**: Handles money transaction independently
- **ShowSeat**: Maintains availability state

This separation makes debugging, rollback, and retries easier.

---

## Step 5: Design for Scalability

Later requirements become easy because bottom-up design separates responsibilities:

- **Seat reservation service** can be scaled independently
- **Payment service** works asynchronously
- **Notification service** (SMS/email) is decoupled
- **Search service** can use Elasticsearch to speed up queries

**This shows long-term thinking.**

### Why Separation Enables Scalability

**Scenario: Blockbuster movie release**

With proper entity separation:

```
Normal load:
  Reservation Service: 1 instance
  Payment Service: 1 instance
  Notification Service: 1 instance

Blockbuster release (100x traffic):
  Reservation Service: 50 instances (handle seat locks)
  Payment Service: 5 instances (payment is slower)
  Notification Service: 10 instances (send 1M SMS)
```

Each scales independently based on its bottleneck.

**Without separation** (if all logic mixed):
- You must scale everything 100x
- Wastes resources
- More expensive
- Slower to respond

**With bottom-up design**:
- You scale only what's needed
- Cost-efficient
- Better resource utilization
- Fast response times

---

## Design Patterns: Must Mention in Interview

This part impresses interviewers if said naturally.

| Pattern | Where Used | Why |
|---------|-----------|-----|
| **Factory** | Entity creation (ShowSeat list, Booking) | Avoid mixing object creation logic with business logic |
| **Singleton** | Payment Gateway / Notification clients | Only one instance, avoids overhead |
| **Strategy** | Payment methods (Card, UPI, Wallet) | Add new payment methods without changing logic |
| **Observer** | Notifications after booking success | Send email/SMS on status update |
| **Template Method** | Payment flow (request → callback → verify) | Common steps with customizable parts |
| **Repository** | DB access for each entity | Separation of business logic from database |
| **State** | Booking lifecycle (INITIATED → SUCCESS/FAILED) | Clear transitions and validations |
| **Circuit Breaker** | Payment gateway failure | Prevent cascading failures |

**You don't need to memorize them** — if the flow is clear, design patterns automatically make sense.

### Pattern Examples in Movie Ticket System

**Factory Pattern - ShowSeat Creation**:
```
// Instead of:
showSeats = new ShowSeat[] { ... }  (cluttered)

// Use:
ShowSeatFactory factory = new ShowSeatFactory();
showSeats = factory.createSeatsForShow(show, hallLayout);
```

**Strategy Pattern - Payment Methods**:
```
// New payment method? Just add a new strategy:
PaymentStrategy card = new CardPayment();
PaymentStrategy upi = new UPIPayment();
PaymentStrategy wallet = new WalletPayment();
// No changes to Booking or Payment entities
```

**State Pattern - Booking Lifecycle**:
```
Booking booking = new Booking();
booking.selectSeats(seats);        // State: INITIATED
booking.initiatePayment();         // State: PAYMENT_PENDING
booking.confirmPayment(success);   // State: PAYMENT_SUCCESS or PAYMENT_FAILED
```

**Repository Pattern - DB Separation**:
```
// Business logic doesn't know about DB:
BookingService uses BookingRepository
BookingRepository handles all SQL queries
// If you change database, only repository changes
```

---

## How to Confidently Answer in an Interview

### If Interviewer Asks: "What design approach will you follow?"

**Ideal Short Answer**:

"I'll follow a bottom-up design. First, I'll model core entities — City, Cinema, CinemaHall, Movie, Show, and ShowSeat — with clear responsibilities. Then I'll design user flows like searching shows and reserving seats. After that, I'll add concurrency control to avoid double booking and integrate asynchronous payment with idempotency. Finally, I'll ensure scalability and clean architecture using patterns like Factory, Strategy, Observer, and State.

Each entity has a single responsibility, payment processing is decoupled, and the system can scale independently for different components."

**This single explanation covers**:
- ✅ OOP principles
- ✅ Data modeling
- ✅ Concurrency control
- ✅ Asynchronous payments
- ✅ Design patterns
- ✅ Scalability thinking