# LLD Chapter: Airline Management System

## Topic: Expectations from the Interviewee

---

## What

**From an LLD interview standpoint, this section defines what dimensions of the system the interviewer is evaluating, not just the features.**

### The interviewer expects you to:

- ✅ Identify critical subsystems (reservation, payment, pricing, scheduling, crew)
- ✅ Ask clarifying design questions before jumping into classes
- ✅ Demonstrate awareness of constraints, edge cases, and conflicts
- ✅ Translate business rules into design decisions

> **This section is less about coding and more about design maturity.**

---

## Why

**Interviewers include this part to assess:**

| Assessment | Purpose |
|------------|---------|
| **Can you think beyond CRUD?** | Shows depth |
| **Do you understand real-world complexity?** | Shows experience |
| **Can you prevent design flaws early?** | Shows proactive thinking |

### In LLD, missing these questions often leads to:

- ❌ Incorrect class responsibilities
- ❌ Race conditions (seat booking)
- ❌ Rigid pricing or payment models
- ❌ Poor extensibility

> **So this phase proves that you design before you implement.**

---

## How (Expectation-wise Breakdown)

---

## 1. Flight Reservation

### What

Flight reservation ensures **seat-level consistency** and maps customers to flights and seats.

---

### Why

**This is the core transactional flow.**

> A single bug here breaks customer trust and revenue.

---

### How (LLD Thinking)

**Seat allocation must be atomic to prevent double booking**

Reservation should be tied to:
- Flight
- Aircraft seat map
- Passenger(s)

### Interview signals you should give:

| Signal | Why It Matters |
|--------|----------------|
| **Concurrency handling** | Locking / optimistic checks |
| **Multi-leg itineraries** | Complex bookings |
| **Group bookings** | Multiple passengers |
| **Full-aircraft charter** | Special case handling |

> **This naturally leads to entities like** `Seat`, `FlightInstance`, `Reservation`, `Itinerary`.

---

## 2. Payment Handling

### What

Handles **monetary transactions** linked to reservations.

---

### Why

**Payment failures or inflexible models directly impact booking completion rates.**

---

### How (LLD Thinking)

**Key principles:**
- Decouple `Payment` from `Reservation`
- Support multiple payment modes via abstraction
- Allow different payment timings:
  - Advance payment
  - Just-in-time payment before departure

### Interview signals:

| Signal | Why It Matters |
|--------|----------------|
| **PaymentStrategy pattern** | Flexible payment methods |
| **Payment state tracking** | PENDING, SUCCESS, FAILED |
| **Offline vs online** | Front desk vs customer |

```java
interface PaymentStrategy {
    PaymentResult process(Payment payment);
}

class CreditCardPayment implements PaymentStrategy {
    public PaymentResult process(Payment payment) {
        // Process credit card
    }
}

class CashPayment implements PaymentStrategy {
    public PaymentResult process(Payment payment) {
        // Process cash payment
    }
}

enum PaymentStatus {
    PENDING, SUCCESS, FAILED, REFUNDED
}
```

---

## 3. Price Variance

### What

Determines **how flight prices are calculated**.

---

### Why

**Pricing is dynamic and business-driven, and changes frequently.**

---

### How (LLD Thinking)

**Key principles:**
- Separate pricing logic from flight data
- Price influenced by:
  - Seat class
  - Demand
  - Day/time
  - Flight duration

### Interview signals:

| Signal | Why It Matters |
|--------|----------------|
| **Avoid hardcoding prices** | Flexibility |
| **Pricing component or rule engine** | Business logic separation |
| **Future pricing models** | Without touching booking flow |

```java
interface PricingStrategy {
    double calculatePrice(Flight flight, SeatClass seatClass, LocalDate bookingDate);
}

class DynamicPricing implements PricingStrategy {
    public double calculatePrice(Flight flight, SeatClass seatClass, LocalDate bookingDate) {
        double basePrice = flight.getBasePrice(seatClass);
        double demandMultiplier = calculateDemand(flight);
        double timeMultiplier = calculateTimeBasedPrice(bookingDate, flight.getDepartureDate());
        return basePrice * demandMultiplier * timeMultiplier;
    }
}
```

---

## 4. Flight Cancellation

### What

Manages **cancellation requests** and their impact on reservations and payments.

---

### Why

**Cancellations affect:**
- Seat availability
- Refunds
- Airline policies

---

### How (LLD Thinking)

**Define cancellation policies based on:**
- Time window
- User role

**Trigger downstream effects:**
- Seat release
- Refund initiation

### Interview signals:

| Signal | Why It Matters |
|--------|----------------|
| **Role-based permissions** | Authorization |
| **Policy-driven rules** | Business flexibility |
| **State transitions** | BOOKED → CANCELLED |

```java
interface CancellationPolicy {
    boolean canCancel(Booking booking, LocalDateTime now);
    double getRefundPercentage(Booking booking, LocalDateTime now);
}

class StandardCancellationPolicy implements CancellationPolicy {
    public boolean canCancel(Booking booking, LocalDateTime now) {
        Duration timeToFlight = Duration.between(now, booking.getFlight().getDepartureTime());
        return timeToFlight.toHours() > 24;
    }
    
    public double getRefundPercentage(Booking booking, LocalDateTime now) {
        Duration timeToFlight = Duration.between(now, booking.getFlight().getDepartureTime());
        if (timeToFlight.toHours() > 48) return 1.0;    // 100%
        if (timeToFlight.toHours() > 24) return 0.5;    // 50%
        return 0.0;                                      // 0%
    }
}
```

---

## 5. Flight Scheduling

### What

Controls **when and where** flights operate.

---

### Why

**Scheduling directly impacts:**
- Aircraft utilization
- Crew availability
- Delay management

---

### How (LLD Thinking)

**Key principles:**
- Separate `Flight` definition from `FlightInstance`
- Allow rescheduling with minimal ripple effects
- Maintain buffer times

### Interview signals:

| Signal | Why It Matters |
|--------|----------------|
| **Time overlap validation** | Prevent conflicts |
| **Reschedule vs cancel** | Different workflows |
| **Event-driven updates** | Delay propagation |

```java
class Flight {
    private String flightNumber;
    private Route route;
    private Schedule schedule; // Weekly pattern
}

class FlightInstance {
    private Flight flight;
    private LocalDateTime scheduledDeparture;
    private LocalDateTime actualDeparture;
    private FlightStatus status; // SCHEDULED, DELAYED, CANCELLED
    private Aircraft aircraft;
    private List<CrewMember> crew;
}

enum FlightStatus {
    SCHEDULED, BOARDING, DEPARTED, IN_AIR, LANDED, DELAYED, CANCELLED
}
```

---

## 6. Staff and Crew Management

### What

Tracks **pilots and crew assignments**.

---

### Why

**Crew mismanagement can ground flights entirely.**

---

### How (LLD Thinking)

**Key principles:**
- Track availability and duty hours
- Prevent overlapping assignments
- Handle last-minute unavailability

### Interview signals:

| Signal | Why It Matters |
|--------|----------------|
| **Availability calendars** | Schedule management |
| **Assignment validation rules** | Conflict detection |
| **Fallback/reassignment flows** | Resilience |

```java
class CrewMember {
    private String id;
    private CrewRole role; // PILOT, CO_PILOT, FLIGHT_ATTENDANT
    private List<Qualification> qualifications;
    private Schedule availability;
}

class CrewAssignment {
    private FlightInstance flight;
    private List<CrewMember> assignedCrew;
    
    public boolean isValid() {
        return hasRequiredRoles() && 
               noScheduleConflicts() && 
               withinDutyHours();
    }
    
    private boolean hasRequiredRoles() {
        // Must have: 1 pilot, 1 co-pilot, N attendants
    }
    
    private boolean noScheduleConflicts() {
        // Check no crew member assigned to overlapping flights
    }
    
    private boolean withinDutyHours() {
        // Check regulatory duty hour limits
    }
}
```

---

## Interview Add-ons (High-impact)

**You can briefly mention:**

### Concurrency control in reservations

> **"We need pessimistic locking or optimistic concurrency control to prevent double booking"**

### Extensibility in pricing and payments

> **"Using Strategy pattern allows adding new pricing models or payment methods without modifying existing code"**

### Strong separation of concerns

> **"Booking, payment, and pricing are separate domains with clear boundaries"**

### Policy-driven rules instead of hardcoding

> **"Cancellation and refund rules should be configurable, not embedded in code"**

> **This shows you are thinking like a system designer, not just a coder.**

---

## Question Framework

### Reservation Questions

| Question | Why It Matters | Design Impact |
|----------|----------------|---------------|
| **Can users book multiple seats?** | Group booking | Reservation-Passenger relationship |
| **Are seat selections mandatory?** | Assignment logic | Automatic vs manual |
| **How to handle overbooking?** | Business policy | Waitlist mechanism |
| **Multi-leg journeys supported?** | Complexity | Itinerary entity |

---

### Payment Questions

| Question | Why It Matters | Design Impact |
|----------|----------------|---------------|
| **Payment methods supported?** | Flexibility | Strategy pattern |
| **Partial payments allowed?** | Complexity | Payment tracking |
| **Refund processing?** | Cancellation flow | Payment state management |
| **Payment deadlines?** | Booking lifecycle | Expiration logic |

---

### Pricing Questions

| Question | Why It Matters | Design Impact |
|----------|----------------|---------------|
| **Dynamic vs static pricing?** | Business model | Pricing engine |
| **Seat class pricing?** | Categorization | Price modifiers |
| **Promotional codes?** | Discounts | Coupon system |
| **Price changes after booking?** | Immutability | Price locking |

---

### Cancellation Questions

| Question | Why It Matters | Design Impact |
|----------|----------------|---------------|
| **Who can cancel?** | Authorization | Role-based access |
| **Refund policies?** | Business rules | Policy engine |
| **Partial cancellations?** | Complexity | Booking modification |
| **Cancellation deadlines?** | Time constraints | Validation rules |

---

### Scheduling Questions

| Question | Why It Matters | Design Impact |
|----------|----------------|---------------|
| **Recurring vs one-time flights?** | Data model | Flight vs FlightInstance |
| **How to handle delays?** | Status management | State machine |
| **Aircraft reassignment?** | Flexibility | Assignment logic |
| **Crew changes?** | Operations | Reassignment workflow |

---

## Design Patterns Naturally Emerging

| Pattern | Use Case | Interview Phrasing |
|---------|----------|-------------------|
| **Strategy** | Payment methods, pricing rules | "Flexible, pluggable algorithms" |
| **State** | Booking/flight status | "Behavior changes with state" |
| **Factory** | Creating reservations, tickets | "Centralized creation logic" |
| **Observer** | Flight status changes | "Notify interested parties" |
| **Policy** | Cancellation rules | "Configurable business rules" |

---

## Red Flags to Avoid

| Mistake | Why It's Bad |
|---------|--------------|
| **Hardcoding prices in Flight** | Inflexible |
| **No concurrency handling** | Race conditions |
| **Tight coupling of Payment and Booking** | Hard to extend |
| **No state management** | Bugs in lifecycle |
| **Ignoring crew constraints** | Operational failures |

---

## Green Flags That Impress

| What You Mention | Why It Impresses |
|------------------|------------------|
| **"Need atomic seat allocation"** | Shows concurrency awareness |
| **"Pricing should be policy-driven"** | Shows flexibility thinking |
| **"Payment and booking are separate"** | Shows separation of concerns |
| **"Flight vs FlightInstance distinction"** | Shows data modeling maturity |
| **"Crew assignment validation"** | Shows constraint thinking |

---

## Summary

**This section establishes:**

- ✅ What subsystems exist (6 critical areas)
- ✅ Why each matters (business impact)
- ✅ How to think about each (LLD principles)
- ✅ What patterns apply (Strategy, State, Policy)
- ✅ What questions to ask (clarification framework)

> **Everything later (classes, interactions, diagrams) builds on this understanding.**

---

