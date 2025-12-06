# Chapter: Design Approach for the Hotel Management System

This chapter gives you the **mindset** you must follow when building the Hotel Management System in an LLD interview.

It explains **how to approach the design** before actually writing classes.

---

## 1️⃣ Design Approach (Bottom-Up)

You will use a **bottom-up approach**, which means:

### Step A — Start with the smallest building blocks

Examples of the smallest units:

- `Room`
- `Bed`
- `Customer`
- `RoomStatus`
- `RoomKey`
- `Payment`

> **These are simple, independent objects.**

---

### Step B — Combine small components to form bigger components

Once you define the smallest classes clearly, you combine them:

**Examples:**

| Small Components | Combined Into |
|------------------|---------------|
| Multiple `Room`s | `Floor` |
| Multiple `Floor`s | `Hotel` |
| `Customer` + `Booking` + `Payment` | `Reservation` |

> **This slowly builds the entire system layer by layer.**

---

### Step C — Keep repeating until you reach the full system

Eventually you reach the top layers:

- `BookingService`
- `NotificationService`
- `InvoiceService`
- `HotelManagementSystem` (main orchestrator)

### This approach ensures:

- ✅ Fewer mistakes
- ✅ Better reusability
- ✅ Proper clean design
- ✅ Easier extensions later (adding gym, spa, restaurant, etc.)

---

## 2️⃣ Why Interviewers Love This Approach

Because bottom-up shows you understand:

| Concept | Why It Matters |
|---------|----------------|
| **Class decomposition** | Breaking complex systems into manageable parts |
| **Object relationships** | How entities connect and interact |
| **Behavior modeling** | What each component does |
| **System scalability** | How to grow the system |
| **Extensibility** | How to add new features |

> **It proves you know how real systems are built, not just "write some classes".**

---

## 3️⃣ Design Pattern Discussion (Important in Interviews)

A hotel management system touches **multiple design patterns**.

Mentioning these immediately impresses interviewers.

### ✔ Core Patterns You Should Mention

---

### 1. Factory Pattern

**Used to create objects like:**

- `Booking`
- `Room`
- `Payment`

**Example:**

```java
RoomFactory.createRoom(RoomType.DELUXE)
RoomFactory.createRoom(RoomType.SUITE)
RoomFactory.createRoom(RoomType.STANDARD)
```

> `RoomFactory` might create different types of rooms (Deluxe, Suite, Standard).

---

### 2. Strategy Pattern

**Used in:**

- **Pricing strategies** (weekday, weekend, seasonal)
- **Payment strategies** (UPI, card, cash)

**Example:**

```java
interface PricingStrategy {
    double calculatePrice(Room room, int nights);
}

class WeekdayPricing implements PricingStrategy { ... }
class WeekendPricing implements PricingStrategy { ... }
class SeasonalPricing implements PricingStrategy { ... }
```

---

### 3. Observer Pattern

**Used for:**

- Notification system (email/SMS updates when booking is confirmed/canceled)

**Example:**

```java
interface BookingObserver {
    void onBookingConfirmed(Booking booking);
    void onBookingCancelled(Booking booking);
}

class EmailNotifier implements BookingObserver { ... }
class SMSNotifier implements BookingObserver { ... }
```

---

### 4. Singleton Pattern

**Used for:**

- `HotelManagementSystem` (main controller)
- `InventoryManager` (to keep room status consistent)

**Example:**

```java
class HotelManagementSystem {
    private static HotelManagementSystem instance;
    
    private HotelManagementSystem() {}
    
    public static HotelManagementSystem getInstance() {
        if (instance == null) {
            instance = new HotelManagementSystem();
        }
        return instance;
    }
}
```

---

### 5. State Pattern ⭐

**Used for room status transitions:**

```
AVAILABLE → RESERVED → OCCUPIED → DIRTY → AVAILABLE
```

**This is extremely realistic.**

**Example:**

```java
interface RoomState {
    void reserve();
    void checkIn();
    void checkOut();
    void clean();
}

class AvailableState implements RoomState { ... }
class ReservedState implements RoomState { ... }
class OccupiedState implements RoomState { ... }
class DirtyState implements RoomState { ... }
```

---

### 6. Builder Pattern

**Used when creating complex objects like:**

- `Invoice`
- `BookingDetails`
- `CustomerProfile`

**Example:**

```java
Invoice invoice = new InvoiceBuilder()
    .withBooking(booking)
    .withCustomer(customer)
    .withRoomCharges(roomCharges)
    .withTax(tax)
    .withDiscount(discount)
    .build();
```

---

## ✔ Why This Matters

When you mention these patterns, it shows:

| What You Demonstrate | Why It's Important |
|----------------------|-------------------|
| You think in terms of design principles | Professional software engineering mindset |
| You understand how to structure complex systems | Architectural capability |
| You can scale and maintain large applications | Production-ready thinking |

> **Interviewer immediately sees that you are senior-level in design thinking.**

---

## Design Pattern Mapping Table

| Pattern | Component | Purpose |
|---------|-----------|---------|
| **Factory** | Room, Booking, Payment creation | Object creation abstraction |
| **Strategy** | Pricing, Payment methods | Interchangeable algorithms |
| **Observer** | Notification system | Event-driven updates |
| **Singleton** | HotelManagementSystem, InventoryManager | Single instance management |
| **State** | Room status transitions | State behavior management |
| **Builder** | Invoice, BookingDetails | Complex object construction |

---

## Bottom-Up Design Flow

```
Level 1: Atomic Components
├── Room
├── Bed
├── Customer
├── RoomStatus
├── RoomKey
└── Payment

Level 2: Composite Components
├── Floor (multiple Rooms)
├── Hotel (multiple Floors)
└── Reservation (Customer + Booking + Payment)

Level 3: Service Layer
├── BookingService
├── PaymentService
├── NotificationService
└── InvoiceService

Level 4: System Orchestrator
└── HotelManagementSystem
```

---

## Interview Speaking Template

### When asked about design approach:

> **"I'll use a bottom-up approach, starting with atomic entities like Room, Customer, and Payment. Then I'll compose them into larger structures like Floor, Hotel, and Reservation. Finally, I'll add service layers for booking, payment, and notifications, coordinated by a central HotelManagementSystem."**

### When discussing design patterns:

> **"This system naturally uses several patterns: Factory for creating different room types, Strategy for pricing and payment methods, Observer for notifications, State for room status transitions, Singleton for the main system controller, and Builder for complex objects like invoices. These patterns ensure the system is maintainable, extensible, and follows SOLID principles."**

---

## Key Benefits of Bottom-Up Approach

| Benefit | Description |
|---------|-------------|
| **Clarity** | Each component has clear, limited responsibility |
| **Testability** | Small components are easy to unit test |
| **Reusability** | Components can be reused across features |
| **Maintainability** | Changes are localized to specific components |
| **Extensibility** | New features can be added without breaking existing code |
| **Scalability** | System can grow organically |

---

## SOLID Principles Application

| Principle | Application in HMS |
|-----------|-------------------|
| **SRP** | Each class has one responsibility (Room manages room data, BookingService manages bookings) |
| **OCP** | New room types can be added without modifying existing code (Factory) |
| **LSP** | Different pricing strategies are interchangeable (Strategy) |
| **ISP** | Interfaces are specific (Bookable, Payable, Notifiable) |
| **DIP** | High-level modules depend on abstractions, not implementations |

---

## Extension Scenarios

Show that your design supports future extensions:

### Current Scope:
- Room booking
- Payment processing
- Check-in/check-out

### Easy Extensions with This Design:
- 🏋️ Gym booking
- 🧖 Spa services
- 🍽️ Restaurant reservations
- 🚗 Parking management
- 🧹 Housekeeping tracking
- 💳 Loyalty programs
- 📊 Analytics dashboard

> **Mentioning extensibility shows long-term thinking.**

---

## Common Pitfalls to Avoid

| Pitfall | Why It's Bad | Solution |
|---------|--------------|----------|
| Starting with UI/API layer | Puts cart before horse | Start with domain models |
| Creating God classes | Everything in one class | Use single responsibility |
| Hardcoding business rules | Inflexible | Use Strategy pattern |
| Ignoring state transitions | Bugs in room status | Use State pattern |
| No abstraction layers | Tight coupling | Use interfaces |

---

## Interview Checklist

Before presenting your design:

- [ ] Identified atomic components
- [ ] Defined composition relationships
- [ ] Planned service layer
- [ ] Selected appropriate design patterns
- [ ] Considered SOLID principles
- [ ] Thought about extensibility
- [ ] Addressed concurrency concerns
- [ ] Planned for error handling

---

# 📘 Entities / Classes in the Hotel Management System

I am grouping them into layers so you understand the system cleanly (bottom-up).

---

## 1️⃣ Core Domain Entities (Most Important)

These represent the heart of the hotel.

### 1. Room

- `roomId`
- `roomNumber`
- `roomType`
- `roomStatus`
- `price`
- `floorNumber`

### 2. RoomType

- `typeName` (Standard, Deluxe, Suite)
- `capacity`
- `basePrice`
- `amenities` (WiFi, TV, AC, etc.)

### 3. RoomStatus (Enum)

- `AVAILABLE`
- `RESERVED`
- `OCCUPIED`
- `UNDER_MAINTENANCE`
- `DIRTY`

### 4. Floor

- `floorNumber`
- `list of rooms`

### 5. Hotel

- `hotelId`
- `name`
- `address`
- `list of floors`
- `list of rooms` (optional cache)

---

## 2️⃣ Customer & People Entities

These represent different types of users.

### 6. Customer

- `customerId`
- `name`
- `contactDetails`
- `idProof` (optional)
- `bookingHistory`

### 7. Receptionist / Staff

- `staffId`
- `name`
- `role`

### 8. Housekeeper

- `staffId`
- `assigned rooms/tasks`

---

## 3️⃣ Booking and Stay Entities

These represent the booking lifecycle.

### 9. Booking / Reservation

- `bookingId`
- `room`
- `customer`
- `checkInDate`
- `checkOutDate`
- `totalPrice`
- `reservationStatus`
- `paymentStatus`

### 10. ReservationStatus (Enum)

- `CREATED`
- `CONFIRMED`
- `CANCELLED`
- `CHECKED_IN`
- `CHECKED_OUT`

### 11. RoomKey

- `keyId`
- `roomId`
- `isActive`
- `assignedToCustomer`

---

## 4️⃣ Payment Entities

### 12. Payment

- `paymentId`
- `amount`
- `method`
- `time`
- `paymentStatus`
- `booking reference`

### 13. PaymentMethod (Enum)

- `CARD`
- `CASH`
- `UPI`
- `NET_BANKING`

### 14. Invoice

- `invoiceId`
- `booking`
- `amount`
- `taxes`
- `discounts`
- `generatedAt`

---

## 5️⃣ Utility & Supporting Entities

### 15. Notification

- `notificationId`
- `type` (SMS / Email)
- `recipient`
- `message`

### 16. RoomServiceRequest

- `requestId`
- `room`
- `category` (Cleaning, Laundry, Food)
- `assignedStaff`
- `status`

### 17. Amenities

- `amenityId`
- `name`
- `additionalCost`

---

## 6️⃣ Aggregator / Manager Classes (High-Level Services)

These classes manage the domain entities.

### 18. HotelManagementSystem

- Central orchestrator
- Manages bookings, check-in, check-out

### 19. BookingManager

- `createBooking`
- `cancelBooking`
- `modifyBooking`
- `calculatePrice`

### 20. RoomManager

- `checkAvailability`
- `updateRoomStatus`
- `getAvailableRooms`

### 21. PaymentManager

- `processPayment`
- `issueRefund`
- `generateInvoice`

### 22. NotificationService

- `sendBookingConfirmation`
- `sendCancellationMessage`

---

## 7️⃣ Repositories / Data Access Classes

- **23. RoomRepository**
- **24. BookingRepository**
- **25. CustomerRepository**
- **26. PaymentRepository**

These allow data storage/retrieval.

---

## 📌 Summary of All Entities

### Rooms & Hotel:
`Room`, `RoomType`, `RoomStatus`, `Floor`, `Hotel`

### People:
`Customer`, `Staff`, `Housekeeper`

### Booking:
`Booking`, `ReservationStatus`, `RoomKey`

### Payment:
`Payment`, `PaymentMethod`, `Invoice`

### Utility:
`Notification`, `Amenities`, `RoomServiceRequest`

### Managers (services):
`HotelManagementSystem`, `BookingManager`, `RoomManager`, `PaymentManager`, `NotificationService`

### Repositories:
`RoomRepository`, `BookingRepository`, `CustomerRepository`, `PaymentRepository`

---

# ⭐ Relationships Between the Classes

I'll break them into logical groups so you can easily visualize the UML structure.

---

## 1️⃣ Hotel ↔ Floor ↔ Room

### Hotel → Floor (1 → many)

- A hotel contains multiple floors
- One floor belongs to exactly one hotel

### Floor → Room (1 → many)

- Each floor has many rooms
- A room belongs to exactly one floor

### Hotel → Room (1 → many) (optional aggregated view)

- Hotels often maintain a room index for fast lookup

---

## 2️⃣ Room ↔ RoomType

### Room → RoomType (many → 1)

- Many rooms can share the same room type
- Example: 20 rooms may be of type "Deluxe"

---

## 3️⃣ Room ↔ Booking

### Room → Booking (1 → many over time)

- A room can have many bookings but **not overlapping in time**
- At any moment, a room can have:
  - 0 bookings (`AVAILABLE`)
  - 1 active booking (`RESERVED` / `OCCUPIED`)

### Booking → Room (1 → 1)

- A booking is always for a single room

---

## 4️⃣ Customer ↔ Booking

### Customer → Booking (1 → many)

- A customer can have multiple bookings over time

### Booking → Customer (1 → 1)

- Each booking belongs to exactly one customer

---

## 5️⃣ Booking ↔ Payment

### Booking → Payment (1 → many)

A booking may have:
- Advance payment
- Final payment
- Refund
- Additional charges

### Payment → Booking (1 → 1)

- A payment is always associated with exactly one booking

---

## 6️⃣ Booking ↔ Invoice

### Booking → Invoice (1 → 1 or 1 → many)

Depends on hotel policy:
- **1 → 1** if invoice generated only at checkout
- **1 → many** if separate invoices issued (e.g., room + minibar + spa)

---

## 7️⃣ Booking ↔ RoomKey

### Booking → RoomKey (1 → many)

- Multiple keys may be issued for the same booking

### RoomKey → Booking (1 → 1)

- A room key belongs to one booking

---

## 8️⃣ Room ↔ RoomStatus

### Room → RoomStatus (1 → 1)

- A room has only one status at a time
- **Enum** — not a separate entity

---

## 9️⃣ Staff / Housekeeper ↔ RoomServiceRequest

### RoomServiceRequest → Staff (many → 1)

- A request is handled by one staff member

### Staff → RoomServiceRequest (1 → many)

- A staff member can have multiple requests

### RoomServiceRequest → Room (many → 1)

- A service request belongs to one room

---

## 🔟 Amenities ↔ RoomType

### RoomType → Amenities (many ↔ many)

- A room type may have multiple amenities
- An amenity can be used by multiple room types

**Example:**  
WiFi is available in Deluxe, Super Deluxe, and Suite.

---

## 1️⃣1️⃣ Manager Classes (Service Layer)

These do not represent real-world objects — they orchestrate logic.

### BookingManager

Talks to:
- `RoomManager`
- `PaymentManager`
- `CustomerRepository`
- `BookingRepository`
- `NotificationService`

### RoomManager

Talks to:
- `RoomRepository`
- `BookingRepository`

### PaymentManager

Talks to:
- `PaymentRepository`
- Invoice generation
- External payment gateway

### NotificationService

- Sends confirmations, cancellations, reminders

> These relationships are **uses / depends on** (not composition/aggregation).

---

## ⭐ Final UML Relationship Summary (Text Version)

```
Hotel          1 — * Floor
Floor          1 — * Room
Room           * — 1 RoomType
Room           1 — * Booking (over time)
Customer       1 — * Booking
Booking        1 — * Payment
Booking        1 — 1 Invoice (or *)
Booking        1 — * RoomKey
RoomType       * — * Amenities
Staff          1 — * RoomServiceRequest
Room           1 — * RoomServiceRequest
```

---

# ⭐ FULLY EXPLAINED CLASS DIAGRAM (TEXT UML + EXPLANATION)

## 1️⃣ CLASS DIAGRAM (ASCII UML)

This is the complete visual structure:

```
+------------------+                 +------------------+
|      Hotel       |1--------------* |      Floor       |
+------------------+                 +------------------+
| id               |                 | id               |
| name             |                 | floorNumber      |
| address          |                 |                  |
+------------------+                 +------------------+
            |
            | 1
            | 
            | *
+------------------+        *--------------------*
|      Room        |1------>|      Booking       |<------1 Customer
+------------------+        *--------------------*
| id               |        | id                 |
| number           |        | checkInDate        |
| status           |        | checkOutDate       |
| typeId           |        | status             |
| floorId          |        | totalPrice         |
+------------------+        +--------------------+
       | 1                              |
       |                                |1
       | *                              |*
+------------------+         +--------------------+
|   RoomType       |         |      Payment       |
+------------------+         +--------------------+
| id               |         | id                 |
| name             |         | amount             |
| basePrice        |         | mode               |
+------------------+         | status             |
       | *                    | timestamp          |
       |                      +--------------------+
       | *                               ^
+------------------+                     |
|    Amenity       |---------------------*
+------------------+
| id               |
| name             |
+------------------+


+----------------------+
|     RoomKey          |
+----------------------+
| id                   |
| issuedAt             |
| expiresAt            |
+----------------------+
          ^
          |
        1 |
          | *
        Booking


+--------------------------+          +------------------------+
|   RoomServiceRequest     |  *----1  |        Staff           |
+--------------------------+          +------------------------+
| id                       |          | id                     |
| requestType              |          | name                   |
| status                   |          | role                   |
| createdAt                |          +------------------------+
| roomId                   |
| assignedStaffId          |
+--------------------------+
```

---

## ⭐ EXPLANATION OF THE CLASS DIAGRAM

### 2️⃣ Hotel

**Role:** Top-level container for the entire property.

**Why needed?**
- A large hotel can have many floors, rooms
- Helps scope all room and booking operations

**Key relationships:**
- `Hotel 1 → * Floor` (composition)
- Floors do not exist without a hotel

---

### 3️⃣ Floor

**Role:** Groups rooms inside a hotel.

**Why?**
- Makes room management structured and scalable

**Relationships:**
- `Floor 1 → * Room` (composition)

---

### 4️⃣ Room

**Role:** Physical unit that can be booked.

**Attributes:**
- `number`
- `status` (AVAILABLE, RESERVED, OCCUPIED, MAINTENANCE)
- `typeId`
- `floorId`

**Relationships:**
- `Room * → 1 RoomType`
- `Room 1 → * Booking` (over time)

**Why important?**
- Central to availability checks, booking conflicts, price calculation

---

### 5️⃣ RoomType

**Role:**  
Defines pricing & features for multiple rooms.

**Example:**
- Standard
- Deluxe
- Premium Suite

**Relationships:**
- `RoomType * ↔ * Amenity` (many-to-many)

---

### 6️⃣ Amenity

**Examples:**
- WiFi
- AC
- TV
- Breakfast

**Why?**
- Used in pricing logic & room search filters

---

### 7️⃣ Customer

**Role:** The user who books rooms.

**Relationship:**
- `Customer 1 → * Booking`

---

### 8️⃣ Booking

**Role:** Represents reservation + stay.

**Contains:**
- `checkIn`
- `checkOut`
- `status`
- `price`

**Relationships:**
- `Booking * → 1 Room`
- `Booking * → 1 Customer`
- `Booking 1 → * Payment`
- `Booking 1 → * RoomKey`

**Why?**
- Central entity for pricing, payments, conflict-checks

---

### 9️⃣ Payment

**Role:** Tracks each financial transaction.

**Supports:**
- Online card payment
- Cash
- Partial payments
- Refunds

**Relationships:**
- `Payment → Booking` (1 → 1)

---

### 🔟 RoomKey

**Role:** Access control representation.

**Why separate?**
- Multiple keys can be issued for same booking (family, staff help)

**Relationship:**
- `Booking 1 → * RoomKey`

---

### 1️⃣1️⃣ RoomServiceRequest

**Role:** Housekeeping & room service workflow.

**Relationship:**
- `RoomServiceRequest * → 1 Room`
- `RoomServiceRequest * → 1 Staff`

---

### 1️⃣2️⃣ Staff

**Role:** Employee handling tasks.

**Types:**
- Housekeeper
- Receptionist
- Manager

**Relationship:**
- `Staff 1 → * RoomServiceRequest`

---

## ⭐ HOW TO EXPLAIN THIS IN AN INTERVIEW

Here is the exact polished narration:

> **"The system is centered around Rooms and Bookings. A Hotel contains Floors, and Floors contain Rooms. Rooms are categorized by RoomType, which defines pricing and amenities. Customers create Bookings for Rooms, ensuring no overlapping dates. Each Booking generates multiple Payments and RoomKeys. Operational activities such as cleaning or service requests are modeled as RoomServiceRequests assigned to Staff. All relationships follow clear compositions and associations, making the system modular and expandable."**

**This sounds professional and complete.**

---

## Quick Reference: Relationship Types

| Relationship | Symbol | Example |
|--------------|--------|---------|
| **Composition** | 1—◆* | Hotel contains Floors |
| **Aggregation** | 1—◇* | RoomType has Amenities |
| **Association** | 1——* | Customer has Bookings |
| **Dependency** | - - -> | Manager uses Repository |

---

