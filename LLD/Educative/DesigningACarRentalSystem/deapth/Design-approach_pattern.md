# ⭐ Design Approach & Design Patterns

Below is a clear, interview-ready, deeply reasoned explanation of the Design Approach and Design Pattern expectations.  
It includes **what, why, and how**, plus extra points that interviewers typically expect but candidates often miss.

---

## ⭐ Design Approach

The interviewer wants to see if you can structure your solution systematically.  
Here's how you explain it.

---

## 1️⃣ Identify Core Components (Bottom-Up)

### ✔ What?

Start with the atomic building blocks — the entities:

* Vehicle
* VehicleType
* Branch
* Customer
* Reservation
* Payment
* Invoice
* Services (driver, insurance, roadside assistance)
* VehicleStatus (available, reserved, inService, droppedOff)

### ✔ Why?

Because bottom-up ensures:

* No component is missed
* Entities are consistent
* Relationships become clear
* You don't get stuck at high-level abstractions

### ✔ How?

Define:

* **Attributes**
* **Behaviors**
* **State transitions**
* **Associations** (1-to-1, 1-to-many)

You get a stable base to build larger modules.

---

## 2️⃣ Compose Subsystems

After defining entities, group them into logical modules.

### Here are the typical subsystems:

#### Inventory Management Subsystem
Tracks vehicle availability, movement across branches

#### Reservation Subsystem
Creates, modifies, and cancels bookings

#### Pricing & Billing Subsystem
Calculates fees, taxes, add-ons, penalties

#### Payment Subsystem
Handles multi-branch payments, refunds, deposits

#### Customer Subsystem
Stores user profile, license info, rental history

#### Notification Subsystem
Sends emails, SMS for confirmations

### ✔ Why is this important?

Because car rental is modular by nature, and without subsystem boundaries:

* Code becomes tightly coupled
* Features break each other
* Extensibility suffers

### ✔ How?

You group **entities + behaviors + state machines** into cohesive modules.  
Each module exposes clear interfaces to other modules.

---

## 3️⃣ Integrate Subsystems

This is where your architecture comes together.

### Integration examples:

* **Reservation subsystem** checks **inventory subsystem** for available cars
* **Payment subsystem** generates invoices for **reservation subsystem**
* **Inventory subsystem** updates vehicle status after payment
* **Notification subsystem** receives events from **reservation subsystem**

### ✔ Why integrate this way?

Integration ensures:

* Reuse
* Loose coupling
* SOLID compliance
* Maintainability

### ✔ How to integrate cleanly?

Use:

* Interfaces
* Domain services
* Clear API boundaries
* Event-driven updates (if distributed)

**Example:**

```
ReservationService → calls InventoryService → gets available vehicles → confirms booking
```

---

## 4️⃣ Document Assumptions

This is **VERY IMPORTANT** in interviews.

### ✔ What assumptions should you state?

Examples:

* A vehicle cannot be double-booked
* Branches share a central database
* Payments must succeed before pickup
* Drivers are optional add-ons
* Vehicle location must always be tracked

### ✔ Why?

Because:

* Requirements are always incomplete
* Interviewers check how you deal with ambiguity
* Good assumptions show domain understanding

### ✔ How?

Just say:  
*"I assume X because Y. Under a different assumption, the system would behave like Z."*

This is where you demonstrate **clarity of thinking**.

---

## 🟦 Additional Things You Can Mention About Design Approach

### ✔ Concurrency handling

Prevent double booking using locks or optimistic concurrency

### ✔ Edge cases

* Late returns
* Early returns
* Double payments
* Vehicle stuck in maintenance
* Driver unavailable

### ✔ Separation of read vs write flows

CQRS-like approach for search vs booking

### ✔ Testing strategy

* Unit tests
* Integration tests
* Mocking external systems

**Mentioning these gives you a big advantage.**

---

## ⭐ Design Patterns (What, Why, How)

Here are patterns that strongly apply in a Car Rental System and interviewers love hearing them.

---

## 1️⃣ Factory Pattern

### ✔ What?

Used to create vehicle objects based on type (SUV, Sedan, Electric).

### ✔ Why?

* Avoid if/else blocks
* Add new vehicle types without breaking code

### ✔ How?

```java
VehicleFactory.createVehicle(type) // returns the correct subclass
```

---

## 2️⃣ Strategy Pattern

### ✔ What?

Used for pricing strategies.

### ✔ Why?

Different pricing models:

* Weekend pricing
* Holiday pricing
* Luxury pricing
* Dynamic pricing

### ✔ How?

Inject the right pricing strategy based on conditions.

---

## 3️⃣ State Pattern

### ✔ What?

Vehicles and reservations have clear state transitions:

**Vehicle:**
```
Available → Reserved → PickedUp → DroppedOff → InService
```

**Reservation:**
```
Pending → Confirmed → PickedUp → Completed → Cancelled
```

### ✔ Why?
* To avoid huge if/else blocks
* Each state has its own behavior

### ✔ How?
Define a state interface and concrete classes for each state.

---

## 4️⃣ Observer / Publisher-Subscriber Pattern
### ✔ What?
Notify other systems when:
* Reservation created
* Reservation cancelled
* Vehicle returned
* Payment completed

### ✔ Why?
Loose coupling between modules.
### ✔ How?
**ReservationService** publishes events:
* `ReservationConfirmedEvent`
* `ReservationCancelledEvent`
* `VehicleReturnedEvent`

**NotificationService** and **InvoiceService** subscribe.
---

## 5️⃣ Singleton Pattern
### ✔ What?
Used for:

* Payment gateway service
* Configuration manager
* Notification dispatcher

### ✔ Why?
Only one instance needed for global coordination.

---

## 6️⃣ Repository Pattern
### ✔ What?
Abstracts away database operations.

### ✔ Why?
Easier to test, maintain, and swap DB.

### ✔ How?
`VehicleRepository`, `ReservationRepository`, etc.

---

## 7️⃣ Builder Pattern (Optional but good)

### ✔ What?
Used for creating complex objects like `Vehicle` or `Reservation` with many fields.

### ✔ Why?
Avoid telescoping constructors.

---

## ⭐ Why Patterns Matter in This Interview
Patterns show:

* ✅ You understand clean design
* ✅ You can handle complexity
* ✅ You build modular, maintainable systems
* ✅ You know SOLID principles

**You don't need to mention all patterns —**  
even discussing **3–4 strongly** will impress the interviewer.

---
