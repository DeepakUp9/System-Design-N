# LLD Chapter: Airline Management System

## Topic: Problem Definition

---

## What

An **Airline Management System (AMS)** is a centralized software system that manages the core operational and customer-facing activities of an airline.

### From an LLD interview perspective, this system mainly deals with:

| Domain | Components |
|--------|-----------|
| **Operational entities** | Flights, aircraft, airports, pilots, crew, schedules |
| **Customer-facing features** | Flight search, ticket booking, itineraries, payments |
| **Administrative controls** | Managing inventory, staff availability, system configurations |

> **In short, it is the source of truth for both airline operations and booking workflows.**

---

## Why

**In an interview, the "why" explains the problem being solved and the business need.**

### Airline operations are complex and time-sensitive

**Manual handling leads to:**
- ❌ Overbooking or underutilized flights
- ❌ Crew scheduling conflicts
- ❌ Inconsistent flight information

---

### Customers expect:

- ✅ Real-time availability
- ✅ Online booking and instant confirmation

### Staff (front desk, admin) need:

- ✅ Fast ticket reservation
- ✅ Accurate itineraries
- ✅ Reliable payment processing

---

### So, the system exists to:

| Goal | Benefit |
|------|---------|
| **Reduce operational errors** | Automation and validation |
| **Centralize airline data** | Single source of truth |
| **Improve customer experience** | Self-service and speed |
| **Ensure scalability** | Handle growth in flights, routes, users |

> **Interviewers want to see that you understand why such a system cannot be trivial.**

---

## How

**This is where LLD thinking starts, even at the problem-definition stage.**

From a design perspective, the system works by **separating responsibilities**:

---

### Customer side

- View available flights
- Book tickets
- View itineraries and payment status

---

### Front desk officer

- Reserve tickets on behalf of customers
- Create or modify itineraries
- Process payments for walk-in customers

---

### Admin

- Manage flights, routes, aircraft
- Assign pilots and crew
- Monitor availability and schedules

---

### Internally, the system:

| Responsibility | Description |
|----------------|-------------|
| **Maintains stateful entities** | Flight, Aircraft, Pilot, Booking |
| **Tracks availability** | Seats, pilots, aircraft |
| **Ensures consistency** | Between schedules and reservations |

---

### In an interview, this sets the stage for:

- ✅ Identifying core classes
- ✅ Defining actors and their permissions
- ✅ Discussing constraints like seat limits, pilot availability, and time overlaps

---

## Interview Add-on (High-value)

**You can subtly mention:**

### Reliability

> **"The system must be highly reliable (no double booking)"**

### Extensibility

> **"It should support future extensions (new routes, pricing rules)"**

### Concurrency

> **"Concurrency matters (multiple users booking the same flight)"**

> **This signals that you are already thinking beyond just features and into design quality, which is exactly what LLD interviewers look for.**

---

## System Overview

```
┌─────────────────────────────────────────────────────┐
│           Airline Management System                  │
└─────────────────────────────────────────────────────┘
                      │
        ┌─────────────┼─────────────┐
        │             │             │
   ┌────▼────┐   ┌────▼────┐   ┌───▼────┐
   │Operations│   │Customer │   │ Admin  │
   │  Layer   │   │  Layer  │   │ Layer  │
   └────┬────┘   └────┬────┘   └───┬────┘
        │             │             │
   ┌────┴────┐   ┌────┴────┐   ┌───┴────┐
   │Flights  │   │Search   │   │Manage  │
   │Aircraft │   │Book     │   │Fleet   │
   │Crew     │   │Pay      │   │Staff   │
   └─────────┘   └─────────┘   └────────┘
```

---

## Core Domains

### 1. Flight Management

| Entity | Purpose |
|--------|---------|
| **Flight** | Scheduled air travel instance |
| **Route** | Origin-destination pair |
| **Schedule** | Time and frequency |
| **FlightStatus** | Scheduled, delayed, cancelled |

---

### 2. Aircraft Management

| Entity | Purpose |
|--------|---------|
| **Aircraft** | Physical airplane |
| **AircraftType** | Model specifications |
| **SeatLayout** | Configuration and classes |
| **Availability** | Aircraft assignment to flights |

---

### 3. Crew Management

| Entity | Purpose |
|--------|---------|
| **Pilot** | Licensed flight operator |
| **CrewMember** | Flight attendant |
| **CrewSchedule** | Assignment to flights |
| **Qualifications** | Certifications and ratings |

---

### 4. Booking Management

| Entity | Purpose |
|--------|---------|
| **Booking** | Reservation record |
| **Ticket** | Individual passenger ticket |
| **Passenger** | Traveler information |
| **Payment** | Transaction record |

---

### 5. Inventory Management

| Entity | Purpose |
|--------|---------|
| **SeatInventory** | Available seats per flight |
| **PriceClass** | Economy, business, first |
| **Pricing** | Dynamic fare rules |

---

## Actors and Responsibilities

### Customer

| Action | Description |
|--------|-------------|
| **Search Flights** | Find by route, date, time |
| **Book Tickets** | Reserve and pay |
| **View Itinerary** | Check booking details |
| **Cancel/Modify** | Change reservations |

---

### Front Desk Officer

| Action | Description |
|--------|-------------|
| **Create Booking** | On behalf of walk-in customers |
| **Issue Tickets** | Print physical tickets |
| **Process Payment** | Handle cash/card payments |
| **Modify Reservations** | Change or cancel bookings |

---

### Admin

| Action | Description |
|--------|-------------|
| **Manage Flights** | Create, update, cancel |
| **Assign Aircraft** | Link planes to flights |
| **Schedule Crew** | Assign pilots and attendants |
| **Monitor System** | View reports and analytics |

---

## Key Constraints

### Operational Constraints

| Constraint | Implication |
|------------|-------------|
| **One aircraft per flight** | Aircraft availability tracking |
| **Minimum crew required** | Pilot + attendants |
| **No pilot overlap** | Schedule conflict detection |
| **Seat capacity limit** | Cannot overbook |

---

### Business Constraints

| Constraint | Implication |
|------------|-------------|
| **Advance booking only** | Cannot book past-dated flights |
| **Payment before confirmation** | No unpaid reservations |
| **Cancellation penalties** | Refund rules |
| **Route restrictions** | Valid origin-destination pairs |

---

### Technical Constraints

| Constraint | Implication |
|------------|-------------|
| **Concurrency** | Multiple users booking simultaneously |
| **Atomicity** | All-or-nothing bookings |
| **Consistency** | No double booking |
| **Availability** | System uptime critical |

---

## Problem Scope

### In Scope

- ✅ Flight scheduling and management
- ✅ Ticket booking and payment
- ✅ Seat inventory management
- ✅ Crew and aircraft assignment
- ✅ Customer and staff portals

---

### Out of Scope (Unless Specified)

- ❌ Real-time flight tracking (GPS)
- ❌ In-flight services management
- ❌ Cargo/freight handling
- ❌ Loyalty programs
- ❌ Third-party travel agency integration
- ❌ Airport ground operations

---

## Interview Response Template

### Opening Statement

> **"An Airline Management System centralizes flight operations, booking workflows, and administrative functions. It manages flights, aircraft, crew, and customer reservations while ensuring operational consistency and preventing issues like overbooking or crew conflicts. The system serves three main actors: customers who book tickets, front desk officers who assist walk-ins, and admins who manage the fleet and schedules."**

---

### After Scope Clarification

> **"I'll design a system that handles flight scheduling, seat inventory, booking management, and crew assignment. Key entities include Flight, Aircraft, Pilot, Booking, and Passenger. The design will ensure no double booking through proper concurrency control, maintain consistency between schedules and assignments, and support future extensions like dynamic pricing or new routes."**

---

## Non-Functional Requirements

### Performance

| Requirement | Target |
|-------------|--------|
| **Search response** | < 500ms |
| **Booking confirmation** | < 2 seconds |
| **Concurrent bookings** | 1000+ simultaneous |

---

### Reliability

| Requirement | Target |
|-------------|--------|
| **Uptime** | 99.9% |
| **Data consistency** | Strong for bookings |
| **No double booking** | Guaranteed |

---

### Scalability

| Requirement | Target |
|-------------|--------|
| **Flights** | 10,000+ daily |
| **Users** | Millions |
| **Bookings** | 100,000+ daily |

---

### Security

| Requirement | Target |
|-------------|--------|
| **Payment security** | PCI-DSS compliant |
| **Data encryption** | At rest and in transit |
| **Role-based access** | RBAC enforced |

---

## Key Design Challenges

### Challenge 1: Concurrency

**Problem:** Multiple users booking same seat simultaneously

**Consideration:**
- Pessimistic locking
- Optimistic locking
- Queue-based booking

---

### Challenge 2: Consistency

**Problem:** Ensuring schedules, crew, and aircraft align

**Consideration:**
- Transaction boundaries
- Validation rules
- Conflict detection

---

### Challenge 3: Availability

**Problem:** Real-time seat count accuracy

**Consideration:**
- Inventory management
- Reservation holds
- Timeout mechanisms

---

### Challenge 4: Scalability

**Problem:** Handling peak booking periods

**Consideration:**
- Caching strategies
- Database partitioning
- Load balancing

---

## Why This Problem is Good for LLD

**It tests multiple competencies:**

| Competency | How |
|------------|-----|
| **Entity Modeling** | Complex domain with many entities |
| **State Management** | Bookings, flights have lifecycles |
| **Constraint Handling** | Capacity, scheduling conflicts |
| **Concurrency** | Multiple users, shared resources |
| **Role-Based Access** | Different user types |
| **Real-World Complexity** | Operational + customer concerns |

---

## Interview Evaluation Criteria

### What Interviewers Look For

| Criterion | What It Shows |
|-----------|---------------|
| **Domain Understanding** | Do you grasp airline operations? |
| **Entity Identification** | Can you model the domain? |
| **Constraint Recognition** | Do you see the limits? |
| **Scalability Thinking** | Can you handle growth? |
| **Concurrency Awareness** | Do you understand shared resources? |

---

## Summary

**This problem definition establishes:**

- ✅ What the system does (operations + bookings)
- ✅ Why it's needed (automation, consistency, scale)
- ✅ How it works (actor separation, entity management)
- ✅ What constraints exist (capacity, conflicts, concurrency)

> **Everything later (classes, patterns, APIs) builds on this foundation.**

---

