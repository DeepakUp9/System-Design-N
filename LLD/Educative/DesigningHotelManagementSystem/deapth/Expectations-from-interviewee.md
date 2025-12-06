# ✔ Expectations from the Interviewee — Hotel Management System (LLD Chapter)

In an LLD interview, the interviewer is **not looking for code at this stage**.

They want to see:

- How you think
- How you identify missing details
- How you manage ambiguity
- How you translate vague business rules into requirements

> This chapter explains what options exist, what questions you should ask, and why these questions matter.

---

## 1. Room Booking — Core of the System

Room booking is the **heart** of the Hotel Management System.

Its correctness directly affects revenue, customer satisfaction, and reliability.

### ✔ What the interviewer expects

You must show that you understand the complexity behind "booking a room."

They want to hear:

- Overlap handling
- Concurrency
- Different booking channels
- Real-time availability
- Room state transitions (Available → Reserved → Occupied → Checked-out)

### ✔ Key Clarifying Questions (with what/why/how)

#### 1. How will the system ensure multiple users do not book the same room?

| Aspect | Details |
|--------|---------|
| **What you're really checking** | Concurrency control + scheduling logic |
| **Why it matters** | Double-bookings destroy the system's credibility |
| **How systems usually solve this** | • Locking at room-level during reservation<br>• Atomic DB transactions<br>• Using availability calendar / time slots<br>• Conflict detection algorithms |

---

#### 2. What type of users are allowed to book a room?

**Why ask:**  
Different systems support different booking channels.

**Possible allowed users:**

- Anonymous customers (no login)
- Registered customers
- Hotel receptionist
- Manager (override permissions)

**How this affects design:**  
You may need:
- `Role` or `UserType` enum
- Different UIs or APIs
- Different validation rules

---

#### 3. Can users book a room in advance?

**Why ask:**  
Most hotels allow advance booking; some allow only near-term booking.

**Possible cases:**

- Book months ahead
- Book only for next 30 days
- Book same-day
- Book based on seasonal availability

**How it affects system design:**

- Booking calendar range
- Price calculation logic
- Seasonal pricing rules
- Reservation limits

---

## 2. Payment Handling

Payments are sensitive, involving **security, compliance, pricing rules, and cancellation policies**.

### ✔ What the interviewer expects

You understand:

- Payment timing
- Payment channels
- Partial vs full payment
- Refund handling
- Integration with third-party payment gateways

### ✔ Key Questions — with what/why/how

#### 1. What payment methods can the customer use?

| Aspect | Details |
|--------|---------|
| **What you're clarifying** | Payment types supported (UPI, card, wallet, net banking, cash) |
| **Why** | Impacts integration, UI, backend, and settlement flows |
| **How** | • Online → integrate with PG (Payment Gateway)<br>• Cash → receptionist collects manually |

---

#### 2. Where does the payment happen?

**Possibilities:**

- Online payment during booking
- Payment at check-in
- Payment at check-out
- Hybrid (partial online + remaining at check-in)

**How this impacts design:**

Booking status might need:
- `RESERVED`
- `CONFIRMED`
- `PENDING_PAYMENT`
- `CANCELLED_DUE_TO_PAYMENT_TIMEOUT`

Payment service / invoice module required.

---

#### 3. Can customers pay in advance or only at check-in?

**Why ask:**  
It affects:
- Fraud prevention
- Room blocking logic
- Refund system
- No-show handling

**Example:**
- If user pays nothing → we need "hold booking for X hrs."
- If fully prepaid → booking is guaranteed.

---

## 3. Price Variance

Pricing is **rarely constant**.

Hotels use multiple factors.

### ✔ What the interviewer expects

You understand that hotel pricing is **dynamic** and influenced by many attributes.

### ✔ Key Questions — with what/why/how

#### 1. How is booking price calculated?

**Factors may include:**

- Per night charges
- Seasonal surge
- Weekend premium
- Holiday rates
- Demand-based pricing

**What interviewer sees:**  
You're thinking beyond "fixed price."

---

#### 2. How do room features affect price?

**Examples:**

| Feature | Impact |
|---------|--------|
| AC vs Non-AC | Price difference |
| Sea-view | Premium pricing |
| Deluxe vs Executive | Tier-based pricing |
| Bed types | King/Queen/Twin variations |

**Why it matters:**  
Pricing model needs attributes like `RoomType`, `Amenity`, `Floor`, `Size`.

**How:**  
You may design:

```java
Room
  - basePrice
  - category
  - amenities
  - location
```

---

#### 3. How does booking duration affect payment?

**Possibilities:**

- Per night
- Per 24 hours
- Per hour
- Per stay package

**This affects:**
- Billing calculation
- Check-in/check-out logic

---

## 4. Booking Cancellation

Cancellations directly impact:

- Refund rules
- Payment flow
- Inventory management
- Customer expectations

### ✔ What the interviewer expects

You know cancellation is **not a simple yes/no**.

### ✔ Key Questions — with what/why/how

#### 1. Can the user cancel a booking?

**Why important:**

If **yes** → must handle:
- Refund calculation
- Releasing room inventory
- Updating availability calendar

If **no** → system simpler.

---

#### 2. Which users can cancel?

**Possible answers:**

- Only customers
- Only receptionists
- Managers can override

**Impact:**  
You need **RBAC**:
- Role-based access control
- Authorization rules

---

#### 3. Refund policy?

**Why ask:**  
Directly impacts:
- Business logic
- Payment service
- Wallet credits (if allowed)

**Possible policies:**

| Timing | Refund |
|--------|--------|
| >24 hours before check-in | 100% refund |
| <24 hours | 50% refund |
| Same-day cancellation | No refund |
| Special category | Non-refundable rooms |

---

## 5. Additional Areas You Should Bring Up (Shows Maturity)

### ✔ 1. Room State Machine (Important in LLD)

Rooms move through states:

```
AVAILABLE → RESERVED → OCCUPIED → CHECKED_OUT → CLEANING → AVAILABLE
```

> **Interviewers love seeing this.**

---

### ✔ 2. Concurrency Control

Multiple users may try to book the same room.

**You should show awareness of:**

- Database row locking
- Optimistic locking
- Atomic reservation transactions
- Availability checks

---

### ✔ 3. Notifications (Nice-to-mention)

- Booking confirmation
- Reminder before check-in
- Cancellation notification

---

### ✔ 4. Extensibility

Show that your design can be extended to:

- Loyalty system
- Inventory (food, laundry)
- Staff management
- Multi-hotel chain

> **This tells the interviewer you think long-term.**

---

## ✔ Summary (Interview Perfect)

This chapter shows the interviewer that you can:

| Capability | Why It Matters |
|------------|----------------|
| Understand hidden complexity | Shows depth of thinking |
| Ask clarifying and boundary-setting questions | Shows thoroughness |
| Think about business logic, pricing, payments, cancellation, and concurrency | Shows real-world awareness |
| Think like a system designer, not just a coder | Shows architectural maturity |

---

## Quick Reference: Key Areas to Cover

### Room Booking

- [ ] Concurrency control
- [ ] User types and permissions
- [ ] Advance booking rules
- [ ] Room state transitions
- [ ] Availability checking

### Payment

- [ ] Payment methods
- [ ] Payment timing (advance/check-in/check-out)
- [ ] Payment gateway integration
- [ ] Invoice generation
- [ ] Payment status tracking

### Pricing

- [ ] Dynamic pricing factors
- [ ] Room feature impact
- [ ] Duration-based calculation
- [ ] Seasonal variations
- [ ] Amenity-based pricing

### Cancellation

- [ ] Cancellation permissions
- [ ] Refund policy
- [ ] Inventory release
- [ ] Notification handling
- [ ] RBAC implementation

### Advanced Topics

- [ ] State machine design
- [ ] Concurrency handling
- [ ] Notification system
- [ ] System extensibility

---

## Interview Response Framework

### When discussing room booking:

> **"For room booking, I need to understand: Who can book rooms? How do we prevent double-booking through concurrency control? Can users book in advance, and if so, how far? What's the room state lifecycle—from available to reserved to occupied to checked-out? Should we use database locking or optimistic locking for availability checks?"**

### When discussing payments:

> **"For payments, I'd like to clarify: What payment methods do we support—online, cash, or both? When does payment happen—during booking, at check-in, or at check-out? Do we need partial payments? How do we handle refunds if cancellations are allowed? Should we integrate with a payment gateway?"**

### When discussing pricing:

> **"For pricing, I need to know: Is pricing fixed or dynamic? Do factors like season, weekend, holidays affect price? How do room features like AC, view, or type impact pricing? Is pricing per night, per hour, or per stay? This will affect our pricing calculation logic."**

### When discussing cancellation:

> **"For cancellations, I'd ask: Can users cancel bookings? What's the refund policy based on timing? Who has permission to cancel—customers, staff, or managers? How quickly do we release inventory after cancellation? Do we need role-based access control?"**

---

## Signals That Impress Interviewers

| What You Mention | Signal It Sends |
|------------------|-----------------|
| Double-booking prevention | Concurrency awareness |
| Room state machine | State management expertise |
| Dynamic pricing | Business logic understanding |
| RBAC for cancellation | Security mindset |
| Payment gateway integration | Real-world system knowledge |
| Notification system | Complete solution thinking |
| Extensibility considerations | Long-term architectural vision |

---

