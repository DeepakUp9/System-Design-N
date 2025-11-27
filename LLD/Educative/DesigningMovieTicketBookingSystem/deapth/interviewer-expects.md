# Movie Ticket Booking System - Complete Low Level Design Guide

## Introduction: Pre-Design Clarification Stage

This section is all about what the interviewer expects you to think about and clarify before jumping into the design. The best candidates don't immediately draw diagrams — they first identify hidden constraints by asking the right questions.

Below is the meaning and purpose of each expectation.

## Part 1: Pre-Design Clarification

### Goal of This Stage

Show the interviewer you understand real-world complexities and surface missing requirements by asking the right questions. You're not expected to know exact answers—you're expected to think critically.

---

## Critical Areas to Clarify

### Seat Selection & Concurrency

**The Core Problem**: Concurrency is unavoidable because many users may select the same seat simultaneously.

**Key Questions to Ask**:
- How do we avoid two people booking the same seat?
- Are seats temporarily reserved while someone is paying?
- How long does the seat remain in "reserved" state before auto-release?
- Do we need locking (DB lock, distributed lock, optimistic concurrency, token-based hold)?

**Why They Care**: Interviewers want to see if you understand seat status must change in real-time, and locking must be safe but not too strict—over-locking kills scalability.

### Payment Handling

**The Challenge**: Booking is only confirmed after payment succeeds. A poor design will block the system during payment or hold seats forever if payment fails.

**Key Questions to Ask**:
- What payment methods exist (UPI, credit card, third-party, in-person counter)?
- Does payment happen online only?
- How do we handle when payment succeeds, fails, or times out?

**Why They Care**: They want to check if you understand payments are asynchronous, and webhooks, retries, and idempotency matter critically.

### Price Variance

**The Reality**: Pricing is not fixed—it depends on multiple dynamic factors.

**Key Questions to Ask**:
- Does pricing vary based on demand, popularity, weekends, or new releases?
- Are premium or VIP seats more expensive?
- Are discount/coupon codes applicable? Referral rewards? Membership benefits?

**Why They Care**: This exposes whether you think about configurable pricing rules, revenue optimization, external pricing engines, and promotional systems—all influence entity design and APIs.

### Duplication & Data Structure

**The Structure**: Same cinema has multiple halls; same hall has multiple shows; same movie runs multiple times.

**Key Questions to Ask**:
- Can a cinema contain many halls running different movies simultaneously?
- Can the same movie run multiple times in the same hall on the same day?
- Do we identify a show by cinema + hall + movie + date-time?

**Why They Care**: If you understand duplication, you'll model Show and SeatInstance per show correctly and avoid the classic mistake of modifying hall seats directly instead of show-specific seat mapping.

### What These Questions Demonstrate

| Skill | Why It's Powerful |
|------|-------------------|
| Clarifying functional requirements | You don't assume wrong things |
| Thinking about concurrency | You understand real-world conflicts |
| Thinking about money flow | You understand risk and rollback |
| Thinking in entities and instances | You avoid ambiguous database models |

---

## Part 2: Deep Dive Into Locking & Payment (Real Engineering)

Perfect — now we're going deep into the real engineering behind preventing double-booking and handling payments asynchronously. What follows is not surface-level theory, but how things are actually implemented in high-traffic ticketing systems.

### Part 2A: Locking & Seat Reservation (Core to Concurrency)

When multiple users try to select the same seat, the system must decide who gets it first and how to represent "temporarily reserved" vs "confirmed booked."

There are four real strategies used in industry — each with trade-offs.

#### Approach A: Pessimistic DB Lock (SELECT … FOR UPDATE)

**How It Works**:
- When user selects seats → update the row (seat_instance) with a DB row lock
- Others trying the same seat get blocked until the lock holder releases/commits/rolls back

**Why This Is Dangerous**:
- Works at small scale, fails during heavy traffic (release days)
- Database becomes bottleneck and threads wait
- Used mostly by small platforms / single theater systems

#### Approach B: Optimistic Concurrency (Version Check / CAS Update)

**How It Works**:
- Seat status row has a `version` or `updated_at` field
- UI requests: "reserve seat if version == current"
- DB update succeeds only if no one changed version in meantime

**Outcome**:
- First user succeeds
- Others get conflict error (409 – seat already reserved)

**Advantage**: Scales much better than DB locks

**Used In**: Event-ticketing systems with high scale

#### Approach C: Distributed Lock (Redis-based Locking)

**Workflow**:
1. Client tries acquiring lock on seat key → `SEAT_LOCK_SHOW123_SEAT_A5`
2. If lock granted → mark seat as `reserved`
3. Lock expires automatically after TTL (e.g., 5 minutes)

**Why Redis**:
- Extremely fast
- Built-in expiry mechanism
- Works with Redlock algorithm for correctness
- Required because multiple instances of ticketing service run

**Used By**: BookMyShow / Ticketmaster scale platforms

#### Approach D: Token-based Hold (Reservation Token Model)

**How It Works**:
Instead of locking the seat itself, system issues a reservation token:
- Seat selected → token created → seat is in temporary hold
- Booking confirmation request must present that same token
- If payment fails or time expires → token invalid → seat auto-released

**Benefits**:
- Stateless API (token in request)
- Can work with caches + DB
- Completely decouples reservation from booking

**Used By**: Uber events / large event platforms. Often combined with distributed lock + TTL.

### Hold Timeout & Auto-Release

Regardless of strategy, a good system follows this lifecycle:

| Phase | Seat State |
|-------|-----------|
| User selects seats | TEMP_HOLD |
| User is paying | TEMP_HOLD (with timer) |
| Payment success | BOOKED |
| Payment failure / timeout | RELEASED |

**Implementation Detail**:
- A sweeper/background job scans expired reservations and releases seats
- UI refresh auto updates seat map in near-real time

### Part 2B: Payment is Asynchronous (Crucial)

A booking doesn't get confirmed instantly when user presses "Pay."

**Why?**

Payment gateways are asynchronous:
- Payment may take seconds or minutes
- User might close browser
- Gateway might send webhook after delay
- Response might get lost and retried

**So system must handle**:
- Duplicate notifications
- Retry deliveries
- Payment success after timeout
- Payment success with expired reservation

### Correct Technical Pattern: SAGA + Idempotency

**Flow (Ideal)**:

1. Reserve seats
2. Initiate payment — create `PaymentAttempt` entity with `idempotency_key`
3. Payment gateway redirects user / processes payment
4. Payment gateway notifies us via webhook
5. Webhook handler:
   - Verify idempotency key
   - Update booking state → BOOKED
   - Release seats only if FAILED and not later succeeded
6. Send notification

**Key Principle**: `BOOKING_STATE` is the source of truth, not the UI request.

### Retry Safety — Idempotency Token

**Why Required**: Payment gateways retry webhooks if we don't respond in time. So without idempotency your system might:
- confirm a seat twice
- trigger double refund
- send multiple confirmations

**Solution**: Every booking/payment attempt is wrapped with a unique idempotency key.

**Database Update Logic**:
```
if payment_attempt.status == COMPLETED:
    return "already processed"
else:
    update status → COMPLETED
    update booking → CONFIRMED
```

This makes duplicate notifications harmless.

### Combined Workflow (High-Level but Realistic)

```
User picks seats →
System verifies lock availability →
Seat(s) TEMP_HOLD with TTL →
Payment initiated →
Webhook confirms/denies →
If success: hold → BOOKED
If fail/timeout: hold → RELEASED
```

At each step:
- No two users should ever transition the same seat from HOLD → BOOKED
- Idempotency prevents repeated execution
- Token/lock prevents race conditions

### Bonus Areas Interviewers LOVE

They won't ask these directly — but mentioning them shows deep understanding:

| Concept | Why It's Impressive |
|---------|-------------------|
| **Hot show sharding** | Prevents overload on DB partitions due to blockbuster movie |
| **Partial booking rollback** | Some seats in group succeed, some fail — conflict resolution |
| **Visual seat map caching** | Reduce DB hits from 100k live users |
| **Circuit breaker for payment** | If gateway is down → release holds fast |
| **Event-driven release reminders** | Users asked to complete payment within 5 mins |

Listing even two of these in an interview signals senior-level thinking.

---

## Part 3: Understanding the Foundation (From Zero)

### First: What is Really Happening When Multiple Users Book Seats?

Imagine:
- Show has Seat A5
- 50 people are trying to book that seat at the same time

If 2 people pay for the same seat → company loses trust → system reputation dies.

**So the backend must guarantee**: Only 1 person should be able to confirm Seat A5.

**This is the root reason why locking, reservation tokens, etc., exist.**

### First Fundamental Question: Should We Allow Immediate Payment?

**Should we allow a user to pay immediately without locking the seat?**

**No, because**:
- While payment is happening (10–60 seconds), someone else might also book the same seat
- Which one should system confirm? It becomes a fight

**So seat booking systems follow 2-step process**:

```
Step 1 → Reserve the seat temporarily
Step 2 → If payment succeeds → convert reservation into confirmed booking

If payment fails → release seat back to available
```

**This is the basic foundation.**

Imagine a show with Seat A5 and 50 people trying to book it simultaneously. If 2 people pay for the same seat, the company loses trust and reputation dies.

**The Backend Must Guarantee**: Only 1 person should be able to confirm Seat A5.

This is why locking, reservation tokens, and other mechanisms exist.

### Two-Step Booking Process

**Should we allow immediate payment without locking the seat?** No.

**Reason**: While payment is happening (10–60 seconds), someone else might also book the same seat. The system won't know whom to give it to.

**Solution**: Follow a 2-step process:

1. **Reserve the seat temporarily**
2. **If payment succeeds → convert reservation into confirmed booking**

If payment fails → release seat back to available.

---

## Part 5: Seat States & Lifecycle

### The Most Important Concept: Four Seat States

A seat does not have just "available" or "booked" in real systems.

| State | Meaning | Duration |
|-------|---------|----------|
| AVAILABLE | Anyone can select | N/A |
| TEMP_HOLD | User is paying or in time window | 5 minutes (auto-release) |
| BOOKED | Payment successful | Permanent for this show |
| RELEASED | Temp hold expired or payment failed | Back to AVAILABLE |

**The TEMP_HOLD is what prevents double booking.**

### Why Do We Need Locking?

Let's say two users try to reserve the same seat at the same millisecond:

```
User A: Selects Seat A5
User B: Selects Seat A5 (at EXACT same time)
```

The backend must decide which one gets the seat first. "Locking" means:

**"While I am reserving this seat, nobody else should modify it."**

It's like standing at a shop counter—the first person puts their hand on the item; others must wait.

### Simple Mental Image for Locking Techniques

Forget technical names for now.

| Technique | Simple Explanation |
|-----------|-------------------|
| DB Lock | "I alone am modifying this row. Others wait." |
| Optimistic Concurrency | "You can try to modify, but if someone already modified before you—fail." |
| Distributed Lock (Redis) | "One central fast service decides who gets first rights." |
| Reservation Token | "You get a secret token proving you reserved. No one else has that token." |

**All techniques have the SAME GOAL**: Give only one user the right to continue booking the seat.

---

## Part 6: Asynchronous Payments & Idempotency

### Why Payments Are Asynchronous

After reserving, the user makes payment. But payment is not instant:

- What if payment succeeds after 40 seconds?
- What if the user closes the browser?
- What if internet goes off?
- What if the payment gateway sends a webhook twice?

So the backend cannot depend only on the UI.

### Idempotency Explained

**Idempotent** means: "Even if the same payment information comes multiple times, the system should confirm booking only once."

**Example**: If payment gateway sends SUCCESS twice → system must not create 2 bookings.

To achieve this, backend stores a unique transaction ID (idempotency key) so repeated calls do nothing.

### Complete Beginner-Friendly Flow

```
User selects seats
    ↓
System checks if available
    ↓
System temporarily holds seats for that user
    ↓
User pays
    ↓
Payment gateway sends success/failure to backend
    ↓
If success → seats become BOOKED
If failure → seats become AVAILABLE again
```

**The hold during payment is crucial** because without it:
- Many users could pay for the same seat
- System won't know whom to give it to

**The idempotency for payment is crucial** because without it:
- Duplicate payment success messages will ruin booking records

### Real-Life Analogy: Online Food Order

| Step | Real Life | Ticket System | State |
|------|-----------|----------------|-------|
| Reserve item | Restaurant marks dish unavailable temporarily | Seat is reserved | TEMP_HOLD |
| Customer pays | UPI/card processing | Payment in progress | TEMP_HOLD with timer |
| Payment succeeds | Dish confirmed for customer | Seat confirmed | BOOKED |
| Payment fails | Dish becomes available again | Seat becomes available | RELEASED |

This is exactly how ticket booking works, just with seats instead of food.

---

## Part 5: Step-by-Step Implementation

### Step 1: Seat Selection API Creates Temporary Hold

When user selects seats from UI, the backend API is called:

```
POST /reserveSeats
Body: { showId: 1001, seats: ["A5", "A6"], userId: 42 }
```

Backend checks seat status in database. If AVAILABLE → convert to TEMP_HOLD.

**SeatInstance Table Structure**:

| Column | Purpose |
|--------|---------|
| id | Unique seat for that show |
| show_id | Show identifier |
| seat_no | Example: A5 |
| status | AVAILABLE / TEMP_HOLD / BOOKED |
| hold_expiry_time | Expiry timestamp (null if not held) |
| held_by | userId who selected seat |

**Example After Hold**:

| seat_no | status | held_by | hold_expiry_time |
|---------|--------|---------|-----------------|
| A5 | TEMP_HOLD | 42 | 2025-11-22 09:32:00 |
| A6 | TEMP_HOLD | 42 | 2025-11-22 09:32:00 |

**Note**: `hold_expiry_time = current_time + 5 minutes` (window for user to pay)

From this point, other users simply see A5 and A6 as unavailable on the UI.

### Step 2: Payment Begins (Seat Still in Temporary Hold)

User presses "Proceed to Pay". Backend creates a `Booking` entry:

| booking_id | user_id | show_id | seats | status |
|------------|---------|---------|-------|--------|
| B9001 | 42 | 1001 | A5,A6 | PENDING_PAYMENT |

**Key Notes**:
- Booking exists but not confirmed yet
- Seats are NOT BOOKED, only TEMP_HOLD
- This is safe—confirmation happens after payment

### Step 3: Payment Gateway Responds (Asynchronously)

Two outcomes possible: SUCCESS or FAILURE

Payment gateway sends webhook to backend:

```
{
  payment_status: "SUCCESS" or "FAILURE",
  payment_reference_id: "unique_reference",
  booking_id: "B9001"
}
```

**If Payment SUCCESS**:

Backend updates:
1. `Booking.status = CONFIRMED`
2. Seat status: `TEMP_HOLD → BOOKED`
3. Clear `held_by` and `hold_expiry_time`

**Final Seat State**:

| seat_no | status | held_by | hold_expiry_time |
|---------|--------|---------|-----------------|
| A5 | BOOKED | null | null |
| A6 | BOOKED | null | null |

Now seats are fully locked forever for that show.

**If Payment FAILURE**:

Backend updates:
1. `Booking.status = FAILED`
2. Seat status: `TEMP_HOLD → AVAILABLE`
3. Clear `held_by` and `hold_expiry_time`

Seats become visible again to other users.

### Step 4: Automatic Timeout & Release

**Scenario**: User selects seats → closes app → never pays

When `hold_expiry_time` is exceeded, seats auto-release via a background job/sweeper:

```
Runs every 1 minute:
  for each seat where status = TEMP_HOLD and current_time > hold_expiry_time:
    set status = AVAILABLE
    clear held_by and hold_expiry_time
```

This maintains system health and prevents seats from being held forever.

### Full Seat Lifecycle (Simple & Memorable)

```
AVAILABLE
     ↓ user selects
TEMP_HOLD (with 5 min expiry)
     ├─→ payment success → BOOKED (forever)
     └─→ payment fails or timeout → AVAILABLE
```

Remember this and you understand ticket booking fundamentally.

### Where Locking Actually Fits

Locking is applied during the moment seat transitions from `AVAILABLE → TEMP_HOLD`.

**Why?** That transition must be atomic, otherwise many users will mark the same seat as TEMP_HOLD simultaneously.

Once TEMP_HOLD is done, locking is not needed because others cannot touch those seats until expiry.

---

## Part 7: Interview Cross-Questions & Answers

### If Interviewer Asks: "What if payment success message comes twice?"

**Answer**: We use an idempotency key with booking/payment record. Even if duplicate message comes, booking status transitions to CONFIRMED only once.

```
if payment_attempt.status == COMPLETED:
    return "already processed"
else:
    update status → COMPLETED
    update booking → CONFIRMED
```

This makes duplicate notifications harmless.

### If Interviewer Asks: "What if payment succeeds after hold expiry?" 

**Rare case but possible.**

**Answer**: Check if seats are still available:
- If available → BOOK the seats
- If not available → refund the payment

(Mentioning this shows deep thinking.)

---

## Part 8: Locking Strategies in Real Systems

### Why Different Locking Strategies Exist

Not every system has the same number of users, traffic, budget, or server setup.

**Examples**:
- A single small-town cinema: few hundred users
- BookMyShow on blockbuster release: lakhs of users simultaneously

So locking strategy depends on scale.

### Strategy 1: Pessimistic DB Lock (Row Lock)

**Simple Explanation**: "This seat row is mine until I finish updating it. Others wait."

**Technical Implementation**: `SELECT ... FOR UPDATE`

**Good For**:
- Small systems (< 500 concurrent users)

**Problems**:
- If many users come → database gets slow
- Many threads wait for locks
- Not suitable for high-traffic platforms

**Where Used**:
- Local theatre booking websites
- Small projects

### Strategy 2: Optimistic Concurrency (Version Check)

**Simple Explanation**: "You can try to reserve this seat, but if someone else got it before you, your update will fail."

**Technical Implementation**:
```sql
update seat
set status = TEMP_HOLD
where seat_id = X and version = 12
```

If version changed → someone else updated seat → update fails.

**Good For**:
- Medium to high traffic (5k – 100k users)
- Less waiting than DB locks

**Problems**:
- Needs correct UI logic to handle failure
- Must show user "seat already taken"

**Where Used**:
- Most modern ticketing systems
- Flight/train seat booking systems

### Strategy 3: Distributed Lock (Redis)

**Simple Explanation**: Imagine a super fast referee that decides who gets the seat first.

**Technical Implementation**:
```
Client tries acquiring lock on seat key → SEAT_LOCK_SHOW123_SEAT_A5
If lock granted → mark seat as RESERVED
Lock expires automatically after TTL (e.g., 5 minutes)
```

**Why Redis?**:
- Extremely fast
- Built-in expiry mechanism
- Works with Redlock algorithm for correctness
- Required when multiple backend instances run

**Good For**:
- High traffic systems (100k – millions)
- Multiple backend servers (microservices)

**Cost**: Needs Redis cluster + good infrastructure setup

**Where Used**:
- BookMyShow
- Ticketmaster
- MakeMyTrip-scale platforms

### Strategy 4: Token-Based Reservation

**Simple Explanation**: Instead of locking the seat itself, system issues a magic token to the user.

**Technical Implementation**:
```
User selects seats
    ↓
System issues reservation token (e.g., TKN-88HJ3)
    ↓
User must use that token to confirm booking
    ↓
If payment fails or expires → token becomes invalid → seat auto-released
```

**Benefits**:
- Stateless API (token in request)
- Works with caches + database
- Completely decouples reservation from booking

**Good For**:
- Load balancing across servers
- Microservices architecture
- Distributed environments

**Requires**: Correct expiry management

**Where Used**:
- Uber Events
- IPL/FIFA ticketing
- Ticketing systems with queue management

---

## Part 9: Choosing the Right Strategy

### Which Strategy is "Best"?

There is no single best strategy. It depends on scale.

| Traffic Volume | Best Strategy | Reasoning |
|-----------------|---------------|-----------|
| < 5k peak users | Pessimistic DB Lock | Simple to implement, sufficient |
| 5k – 100k users | Optimistic Concurrency | Scales much better than locks |
| 100k+ users | Redis Lock + Token | Handles massive concurrent requests |

### Best Interview Answer

**If asked: "Which locking strategy would you choose?"**

"I would use optimistic concurrency for initial implementation because it scales better than DB locks and doesn't require additional infrastructure. If concurrency spikes drastically during peak hours (like blockbuster releases), we can upgrade to distributed lock using Redis. For extreme scale and microservices architecture, token-based reservation becomes ideal because it's completely stateless."

**This answer shows**:
- Deep understanding (not memorization)
- Practical thinking about scalability
- Knowledge of trade-offs
- Real-world deployment thinking

---

## Part 10: Advanced Interview Scenarios

### Cross-Question: What if distributed lock fails or Redis goes down?

**Answer**: Use fallback optimistic concurrency OR make booking service degrade gracefully.

Strategy: If Redis is unavailable, fall back to optimistic concurrency checks. Send alerts to ops team but keep system working.

### Cross-Question: What if user holds seat then leaves without paying?

**Answer**: Seat has `hold_expiry_time`. Background job resets seat to AVAILABLE after expiry.

The sweeper job runs every 1 minute and releases all expired holds automatically.

### Cross-Question: Should locking happen during payment?

**Answer**: No. Lock only during `AVAILABLE → TEMP_HOLD` transition. During payment, seat remains in TEMP_HOLD automatically.

Locking during payment would be wasteful since the seat is already held.

---

## Part 11: Complete Workflow Summary

### SAGA + Idempotency Pattern

This is the industry-standard approach:

1. **Reserve seats** → Create TEMP_HOLD with expiry
2. **Initiate payment** → Create `PaymentAttempt` entity with unique `idempotency_key`
3. **Payment gateway processes** → May take seconds or minutes
4. **Gateway notifies via webhook** → With payment result
5. **Webhook handler verifies**:
   - Check idempotency key (prevent duplicates)
   - Update booking state → BOOKED (if success)
   - Release seats (if failed)
6. **Send notifications** → To user

**Key Principle**: `BOOKING_STATE` is the source of truth, not the UI request.

### Complete Workflow Flow

```
User picks seats
    ↓
System verifies lock availability
    ↓
Seat(s) TEMP_HOLD with 5 min TTL
    ↓
Payment initiated
    ↓
Webhook confirms/denies
    ↓
If success: hold → BOOKED
If fail: hold → RELEASED
```

At each step:
- No two users can transition same seat from HOLD → BOOKED
- Idempotency prevents repeated execution
- Token/lock prevents race conditions

---

## Part 12: Bonus Topics (Senior-Level Thinking)

Mentioning even 2-3 of these in an interview signals senior-level understanding:

| Concept | Why It's Impressive |
|---------|-------------------|
| **Hot show sharding** | Prevents overload on DB partitions due to blockbuster movie - distributes data across shards |
| **Partial booking rollback** | Some seats in group succeed, some fail — conflict resolution strategy |
| **Visual seat map caching** | Reduce DB hits from 100k live users by caching seat status in memory/Redis |
| **Circuit breaker for payment** | If gateway is down → release holds fast instead of blocking |
| **Event-driven release reminders** | Users asked to complete payment within 5 mins via notifications |
| **Optimistic seat inventory** | Show available count without querying all seat states |
| **Write-behind caching** | Cache seat updates, flush to DB periodically |
| **Payment state machine** | Explicit state transitions with validation at each step |

---

## Key Takeaways

✅ **Seat States**: AVAILABLE → TEMP_HOLD → BOOKED (or back to AVAILABLE)

✅ **Two-Step Process**: Reserve first, confirm after payment

✅ **Locking Importance**: Only during AVAILABLE → TEMP_HOLD transition

✅ **Idempotency**: Prevents duplicate payment confirmations

✅ **Async Payments**: Payment doesn't block; webhook confirms later

✅ **Strategy Selection**: Scale determines which locking approach to use

✅ **Auto-Release**: Background job releases expired holds every minute

Once you internalize these concepts, any follow-up question becomes answerable through logic, not memorization.