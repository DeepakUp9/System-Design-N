# LLD Chapter — Introduction to the Restaurant Management System

## Topic: Understanding the Problem Clearly

Before designing any system in an LLD interview, the first thing you must do is **define the problem space**.

This is exactly what this step is about.

---

A **restaurant management system** is a platform that helps run day-to-day operations of a restaurant. It connects multiple activities that happen inside a restaurant and keeps everything organized.

---

## What? (Clear Problem Definition)

This system is used to manage:

### Table Management
Track which tables are free, occupied, or reserved.

### Reservations
Allow customers to book tables and let staff confirm or decline bookings.

### Orders (Food & Beverage Management)
Taking orders, modifying them, sending items to the kitchen, tracking food readiness.

### Billing & Payments
Generating a bill, adding taxes/discounts, splitting the bill, and recording payments.

### Menu Management
The restaurant can create and update menus, prices, categories, availability.

### Staff Interaction
Waiter assigns tables, chefs see kitchen tickets, manager views summaries.

---

### Restaurant Types Supported

- Quick service (fast food)
- Casual dining
- Fine dining with reservations
- Café-style or takeaway

> **Everything is controlled from a central interface.**

---

## Why? (Purpose of Such a System)

Interviewers want to know **why this system exists**.

It solves major real-world problems:

### 1. Operational Efficiency

**Without automation:**
- Table allocation becomes messy
- Delays in taking orders
- Wrong orders delivered
- Billing errors

> **This system reduces the friction.**

---

### 2. Better Customer Experience

- ✅ Faster seating
- ✅ Faster service
- ✅ Accurate billing
- ✅ Smooth ordering

> **Happy customer = repeat customer.**

---

### 3. Data Accuracy

- Real-time status of tables
- Real-time order status
- Live kitchen updates
- Accurate sales reports

---

### 4. Business Visibility

**Managers get:**
- Daily sales reports
- Fastest–slowest items
- Table occupancy trends
- Staff performance insights

> **These help in decision-making.**

---

## How? (How the System Operates Internally — LLD View)

This is where **interviewers judge your design thinking**.

### 1. Entities

The main building blocks include:

| Entity | Purpose |
|--------|---------|
| **Table** | Represents physical seating |
| **Customer** | Who places orders/reservations |
| **Reservation** | Booking information |
| **Order** | Customer's food request |
| **OrderItem** | Individual items in an order |
| **MenuItem** | Items on the menu |
| **Bill** | Final payment summary |
| **Payment** | Payment transaction |
| **Staff** | Waiter / Chef / Manager |

> **Each of these becomes a class or database entity.**

---

### 2. Interactions

```
Customer creates reservation 
    → System allocates table
    → Waiter assigns themselves to table
    → Waiter takes order
    → Order goes to kitchen
    → Chef updates order status
    → Completed order triggers billing
    → Customer pays
    → Table becomes free again
```

> **Each flow has multiple states and transitions (good for State Machine patterns).**

---

### 3. System Behavior

- ✅ Track table availability in real time
- ✅ Allow multiple dishes per order
- ✅ Update status: `ORDERED → COOKING → READY → SERVED`
- ✅ Generate final bill and handle discounts/taxes
- ✅ Produce daily sales summaries

---

### 4. Patterns You Will Use

Interviewers **love** when you mention patterns:

| Pattern | Use Case |
|---------|----------|
| **State Pattern** | For handling order states or table states |
| **Observer Pattern** | When kitchen or waiter needs to be notified about updates |
| **Repository Pattern** | Clean database access |
| **Factory Pattern** | For creating bills or payments based on restaurant type |

> **Mentioning these gives bonus points.**

---

## Extra Points to Add in an LLD Interview

Here are things candidates forget but **interviewers love**:

### 1. Edge Cases

- ⚠️ Overbooking tables
- ⚠️ Customer no-shows
- ⚠️ Restaurant closed hours
- ⚠️ Items unavailable
- ⚠️ Partial payment or split bills
- ⚠️ Order item cancellation
- ⚠️ Table reassignment mid-order

---

### 2. Constraints

- Reservation only allowed for available tables
- One waiter can handle limited tables
- Kitchen queue limit
- Time duration per table (fine dining vs fast food)

---

### 3. Assumptions (Always Required)

**"Assumptions" show clarity.**

**Example:**
- Each table can seat 2, 4, or 6 people
- Order belongs to only one table
- Each menu item has a price, category, availability flag

---

### 4. Types of Restaurants

Explain that the same system can scale:

| Type | Features |
|------|----------|
| **Small café** | Basic version |
| **Fine dining** | Reservation-heavy |
| **Large franchise** | Advanced inventory + analytics |

> **Shows you're thinking like an architect.**

---

## Interview-Ready Summary Line

### If an interviewer asks "What is a Restaurant Management System?", you should answer:

> **"It's a system that manages table allocation, reservations, orders, kitchen workflow, billing, and payments through a unified platform. It improves operational efficiency, enhances customer experience, and gives business insights."**

---

## System Architecture Overview

```
┌─────────────────────────────────────────────────────────┐
│            Restaurant Management System                  │
└─────────────────────────────────────────────────────────┘
                        │
        ┌───────────────┼───────────────┐
        │               │               │
   ┌────▼────┐    ┌─────▼─────┐   ┌────▼────┐
   │ Table   │    │  Order    │   │ Billing │
   │ Mgmt    │    │  Mgmt     │   │ System  │
   └─────────┘    └───────────┘   └─────────┘
        │               │               │
   ┌────┴────┐    ┌─────┴─────┐   ┌────┴────┐
   │• Track  │    │• Kitchen  │   │• Invoice│
   │• Reserve│    │• Status   │   │• Payment│
   │• Assign │    │• Items    │   │• Split  │
   └─────────┘    └───────────┘   └─────────┘
```

---

## Core Workflows

### 1. Reservation Flow

```
Customer → Request Reservation
    ↓
Check Table Availability
    ↓
Allocate Table (if available)
    ↓
Send Confirmation
    ↓
Update Table Status: RESERVED
```

---

### 2. Dining Flow

```
Customer Arrives
    ↓
Check-in (verify reservation or walk-in)
    ↓
Assign Waiter
    ↓
Take Order
    ↓
Send to Kitchen
    ↓
Kitchen Prepares (Status: COOKING)
    ↓
Mark Ready (Status: READY)
    ↓
Serve to Customer (Status: SERVED)
    ↓
Generate Bill
    ↓
Process Payment
    ↓
Release Table
```

---

### 3. Order Management Flow

```
Waiter Takes Order
    ↓
Create Order Entity
    ↓
Add OrderItems
    ↓
Send to Kitchen Queue
    ↓
Kitchen Receives Ticket
    ↓
Update Status: COOKING
    ↓
Mark Items as READY
    ↓
Notify Waiter (Observer Pattern)
    ↓
Waiter Serves
```

---

### 4. Payment Flow

```
Order Complete
    ↓
Generate Bill
    ↓
Calculate:
    • Subtotal
    • Tax
    • Discounts
    • Service Charge
    ↓
Present to Customer
    ↓
Accept Payment
    ↓
Record Transaction
    ↓
Print Receipt
    ↓
Update Table Status: AVAILABLE
```

---

## Key Entities Detail

### Table

```
Table
├── id (PK)
├── tableNumber
├── capacity (2, 4, 6, 8)
├── status (AVAILABLE, RESERVED, OCCUPIED)
├── location (indoor, outdoor, window)
└── assignedWaiter (FK, nullable)
```

---

### Reservation

```
Reservation
├── id (PK)
├── customerId (FK)
├── tableId (FK)
├── reservationTime
├── numberOfGuests
├── status (PENDING, CONFIRMED, CANCELLED, NO_SHOW, COMPLETED)
├── createdAt
└── notes
```

---

### Order

```
Order
├── id (PK)
├── tableId (FK)
├── waiterId (FK)
├── customerId (FK, nullable)
├── orderTime
├── status (PLACED, COOKING, READY, SERVED, COMPLETED)
├── items (List<OrderItem>)
└── totalAmount
```

---

### OrderItem

```
OrderItem
├── id (PK)
├── orderId (FK)
├── menuItemId (FK)
├── quantity
├── specialInstructions
├── status (PENDING, COOKING, READY, SERVED)
└── price
```

---

### MenuItem

```
MenuItem
├── id (PK)
├── name
├── description
├── category (APPETIZER, MAIN, DESSERT, BEVERAGE)
├── price
├── isAvailable
├── preparationTime (minutes)
└── imageUrl
```

---

### Bill

```
Bill
├── id (PK)
├── orderId (FK)
├── subtotal
├── tax
├── serviceCharge
├── discount
├── totalAmount
├── generatedAt
├── paidAt (nullable)
└── paymentMethod
```

---

## State Transitions

### Table States

```
AVAILABLE → RESERVED → OCCUPIED → AVAILABLE
               ↓
            NO_SHOW
               ↓
           AVAILABLE
```

---

### Order States

```
PLACED → COOKING → READY → SERVED → COMPLETED
   ↓                                      ↑
CANCELLED ────────────────────────────────┘
```

---

### Reservation States

```
PENDING → CONFIRMED → COMPLETED
    ↓          ↓
CANCELLED  NO_SHOW
```

---

## Actors in the System

| Actor | Responsibilities |
|-------|------------------|
| **Customer** | Make reservation, place order, pay bill |
| **Waiter** | Take orders, serve food, manage tables |
| **Chef** | View orders, update cooking status, mark ready |
| **Manager** | View reports, manage menu, handle staff |
| **Host/Receptionist** | Manage reservations, assign tables |
| **System** | Send notifications, generate reports, track inventory |

---

## Non-Functional Requirements

| Requirement | Target | Importance |
|-------------|--------|------------|
| **Availability** | 99.9% | High |
| **Response Time** | < 500ms | Critical |
| **Concurrent Users** | 50-100 per restaurant | Medium |
| **Data Consistency** | Strong for orders/billing | Critical |
| **Scalability** | Support multiple locations | High |

---

## Design Considerations

### Concurrency Handling

**Challenge:** Multiple waiters trying to assign same table

**Solution:**
- Pessimistic locking on table assignment
- Atomic operations for status updates
- Queue-based order processing

---

### Real-Time Updates

**Challenge:** Kitchen needs instant order updates

**Solution:**
- WebSocket connections for live updates
- Observer pattern for notifications
- Event-driven architecture

---

### Split Bills

**Challenge:** Customers want to split payment

**Solution:**
- Support multiple payment records per bill
- Track payment portions
- Partial payment handling

---

### Menu Management

**Challenge:** Prices change, items go out of stock

**Solution:**
- Version control for menu changes
- Real-time availability updates
- Historical price tracking

---

## Scalability Considerations

### Single Restaurant

- Simple monolithic architecture
- Single database
- In-memory caching

---

### Multi-Location Chain

- Microservices architecture
- Separate database per location
- Central analytics service
- Shared menu management

---

### Enterprise (Franchises)

- Multi-tenant architecture
- Data partitioning by location
- Central reporting dashboard
- Role-based access control

---

## Interview Discussion Points

### When asked to design a Restaurant Management System:

1. **Clarify restaurant type**  
   "Is this for quick service, casual dining, or fine dining? That affects reservation complexity."

2. **Identify core workflows**  
   "The main flows are reservation, order management, kitchen coordination, and billing."

3. **Explain state management**  
   "Tables and orders go through clear state transitions. I'd use the State pattern."

4. **Mention real-time needs**  
   "Kitchen and waiters need instant updates. I'd use Observer pattern or WebSockets."

5. **Address edge cases**  
   "We need to handle no-shows, table reassignments, split bills, and item cancellations."

6. **Show scalability thinking**  
   "The design should scale from a single restaurant to multi-location chains."

---

## Common Interview Questions

### Q: How do you prevent double-booking of tables?

**A:** Use pessimistic locking when creating reservations. Check availability atomically and update status in a single transaction. Maintain a reservation queue if all tables are booked.

---

### Q: How do you handle a customer who doesn't show up?

**A:** Track reservation status. After grace period (e.g., 15 minutes), mark as NO_SHOW. Automatically release table. Send notification to customer. Consider no-show penalties for repeat offenders.

---

### Q: How do you split a bill between multiple customers?

**A:** Store multiple payment records per bill. Each payment tracks amount and method. Bill shows remaining balance. Allow item-level splitting (assign items to specific customers).

---

### Q: How does the kitchen know what to cook?

**A:** When order is placed, create kitchen ticket with all items. Kitchen sees queue of pending orders. Chef updates status (COOKING → READY). Waiter gets notification when ready.

---

### Q: What if a menu item becomes unavailable mid-service?

**A:** Manager marks item as unavailable. System prevents new orders of that item. For existing orders, notify waiter to inform customer and suggest alternatives.

---
