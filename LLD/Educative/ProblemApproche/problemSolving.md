# Approach to Solve a Real-World Problem (OOD Interview)

> **Step-by-Step Guide for Object-Oriented Design Interviews**

---

## 🎯 1. Interview Context

### What OOD Interviews Test:
- ✅ Object-oriented thinking, not just coding
- ✅ Design of flexible, scalable, maintainable systems
- ✅ Problem-solving approach and communication

### Typical Format:
- **Problem**: Ambiguous real-world scenario
- **Time**: 30–45 minutes
- **Goal**: Design a system architecture

---

## 📋 Step-by-Step OOD Interview Approach

---

## Step 1: Identify the Requirements 📝

### ⏰ Time: First 5–10 minutes (CRITICAL)

### What to Do:

| Action | Purpose |
|--------|---------|
| ✅ Ask clarifying questions | Reduce ambiguity |
| ✅ Understand scope | Set boundaries |
| ✅ Identify constraints | Technical limitations |
| ✅ Confirm expectations | What interviewer wants |

### Key Questions to Ask:

```
Functional Requirements:
├─ What are the core features?
├─ What are the primary use cases?
├─ Are there any edge cases?
└─ What's in scope vs out of scope?

Non-Functional Requirements:
├─ Scale: How many users?
├─ Performance: Response time?
├─ Security: Authentication needed?
└─ Availability: Uptime requirements?

Design Expectations:
├─ High-level design only?
├─ Full implementation needed?
├─ Should I apply SOLID principles?
└─ Are design patterns expected?
```

### What Interviewer Evaluates:
- ✅ Requirement gathering skills
- ✅ Ability to reduce ambiguity
- ✅ Time management
- ✅ Logical thinking
- ✅ Communication clarity

### 📌 Pro Tip:
> **"Never start coding immediately. Spend 5-10 minutes clarifying requirements first."**

---

## Step 2: Model the Problem 🎭

### What to Do:

1. **Identify primary use cases**
2. **Discuss them aloud** with the interviewer
3. **Uncover missing requirements** through discussion

### Optional: Draw Use Case Diagram

**Components:**
- **Actors** (who uses the system)
- **Use cases** (what they can do)
- **System boundaries** (scope)

### Example: Parking Lot System

```
           Customer
              │
              ├─── Park Vehicle
              ├─── Pay Fee
              └─── Exit Parking

           Admin
              │
              ├─── Add Parking Spot
              ├─── View Reports
              └─── Update Pricing
```

### Benefits:
- ✅ Validates understanding
- ✅ Reveals hidden requirements
- ✅ Gets interviewer alignment

---

## Step 3: Identify Classes and Relationships 🏗️

### ⭐ MOST IMPORTANT STEP ⭐

---

### 3a. Identify Objects 🎯

**Rule:** Nouns → Classes, Verbs → Methods

### Example: Parking Lot System

**Entities (Classes):**
- Vehicle
- ParkingSpot
- Ticket
- Entrance
- Exit
- Payment
- ParkingLot
- Floor

### 📌 Technique:
```
Read the problem statement
    ↓
Highlight all nouns
    ↓
Group related nouns
    ↓
Each group = potential class
```

---

### 3b. Define Attributes and Methods 📦

**Each class should have:**
- **Attributes** (state/data)
- **Behaviors** (methods/functions)

### Example Classes:

#### Vehicle
```java
class Vehicle {
    // Attributes
    - String vehicleNumber
    - VehicleType type
    - String color
    
    // Methods
    + getType()
    + getVehicleNumber()
}
```

#### ParkingSpot
```java
class ParkingSpot {
    // Attributes
    - int spotId
    - SpotSize size
    - SpotStatus status
    - Vehicle assignedVehicle
    
    // Methods
    + assignVehicle(Vehicle)
    + removeVehicle()
    + isAvailable()
}
```

#### Ticket
```java
class Ticket {
    // Attributes
    - String ticketId
    - Vehicle vehicle
    - ParkingSpot spot
    - DateTime entryTime
    - DateTime exitTime
    
    // Methods
    + calculateFee()
    + markExit()
}
```

---

### 3c. Define Relationships 🔗

**Types of Relationships:**

| Relationship | Symbol | Example |
|--------------|--------|---------|
| **Association** | `────` | Customer uses ParkingLot |
| **Aggregation** | `◇────` | ParkingLot has ParkingSpots |
| **Composition** | `◆────` | ParkingLot owns Floors |
| **Inheritance** | `────▷` | Truck extends Vehicle |
| **Dependency** | `- - -▶` | Payment depends on Ticket |

### Example Relationships:

```
Vehicle
   △
   │ (Inheritance)
   ├─── Car
   ├─── Bike
   └─── Truck

ParkingLot ◆──── Floor
           ◆──── Entrance
           ◆──── Exit

Floor ◇──── ParkingSpot

ParkingSpot ───── Vehicle (Association)

Payment ─ ─ ─▶ Ticket (Dependency)
```

### Key Questions to Answer:
- ✅ Why interfaces or abstract classes?
- ✅ How SOLID principles are applied?
- ✅ Where polymorphism fits?
- ✅ What design patterns are suitable?

### 📌 Draw High-Level Class Diagram

```
┌──────────────────────────────────────────────┐
│            ParkingLot                        │
├──────────────────────────────────────────────┤
│ - floors: List<Floor>                        │
│ - entrances: List<Entrance>                  │
│ - exits: List<Exit>                          │
├──────────────────────────────────────────────┤
│ + addFloor(Floor)                            │
│ + findAvailableSpot(VehicleType)            │
└──────────────────────────────────────────────┘
              │
              │ (Composition)
              ↓
┌──────────────────────────────────────────────┐
│               Floor                          │
├──────────────────────────────────────────────┤
│ - floorNumber: int                           │
│ - spots: List<ParkingSpot>                   │
├──────────────────────────────────────────────┤
│ + addSpot(ParkingSpot)                       │
│ + getAvailableSpots()                        │
└──────────────────────────────────────────────┘
```

---

## Step 4: Sequence & Activity Diagrams 📊

### When to Use:
- ✅ Explaining complex system flows
- ✅ Showing object interactions
- ✅ Clarifying order of operations

### Example: Park Vehicle Flow

```
Customer    Entrance    System    ParkingSpot    Ticket
   │           │          │            │           │
   ├─────▶     │          │            │           │  1. Request entry
   │           ├────▶     │            │           │  2. Find spot
   │           │          ├───────▶    │           │  3. Check availability
   │           │          ◀───────     │           │  4. Spot available
   │           │          ├────────────────────▶   │  5. Generate ticket
   │           ◀──────────┼────────────────────    │  6. Return ticket
   │◀──────    │          │            │           │  7. Receive ticket
```

### Benefits:
- ✅ Visualizes control flow
- ✅ Shows timing of operations
- ✅ Helps identify missing steps

---

## Step 5: Apply Design Patterns 🎨

### Why Use Patterns:
- ✅ Shows design maturity
- ✅ Provides proven solutions
- ✅ Makes code extensible

### Common Patterns for OOD Problems:

| Pattern | Use Case | Example |
|---------|----------|---------|
| **Singleton** | One instance needed | ParkingLot, Configuration |
| **Factory** | Object creation | VehicleFactory, SpotFactory |
| **Strategy** | Multiple algorithms | PaymentStrategy (Cash, Card) |
| **Observer** | Event notification | SpotAvailabilityNotifier |
| **State** | State transitions | SpotState (Available, Occupied) |

### Example: Strategy Pattern for Payment

```java
interface PaymentStrategy {
    void pay(double amount);
}

class CashPayment implements PaymentStrategy {
    void pay(double amount) {
        // Cash payment logic
    }
}

class CardPayment implements PaymentStrategy {
    void pay(double amount) {
        // Card payment logic
    }
}

class PaymentProcessor {
    PaymentStrategy strategy;
    
    void processPayment(double amount) {
        strategy.pay(amount);
    }
}
```

### 📌 Always Explain:
- **Why** this pattern fits
- **What problem** it solves
- **How** it improves design

---

## Step 6: Coding the Design 💻

### What to Code:

**Priority Order:**
1. ✅ Interfaces / Abstract classes
2. ✅ Core classes
3. ✅ Key methods
4. ✅ Relationships

### Focus On:
- ✅ High-level structure
- ✅ Clean, readable design
- ✅ SOLID principles
- ✅ Extensibility

### Example: Core Structure

```java
// 1. Interfaces
interface Parkable {
    boolean park(Vehicle vehicle);
    boolean unpark(Vehicle vehicle);
}

// 2. Abstract Classes
abstract class Vehicle {
    protected String number;
    protected VehicleType type;
    
    abstract VehicleType getType();
}

// 3. Concrete Classes
class Car extends Vehicle {
    @Override
    VehicleType getType() {
        return VehicleType.CAR;
    }
}

// 4. Main System Class
class ParkingLot implements Parkable {
    private List<Floor> floors;
    
    @Override
    public boolean park(Vehicle vehicle) {
        // Implementation
    }
}
```

### Don't:
- ❌ Implement every detail
- ❌ Write complete logic
- ❌ Focus on edge cases initially

---

## 🎯 Design Approach Strategy

### Design Flow

```
1. Identify use cases
        ↓
2. Note constraints
        ↓
3. Keep design simple
        ↓
4. Plan for scalability
```

### 📌 Example: Parking Lot

**Start Simple:**
- Begin with 1 floor
- Basic vehicle types

**Design for Growth:**
- Easy to add multiple floors
- Support new vehicle types
- Extensible payment methods

---

## 🆚 Top-Down vs Bottom-Up Design

| Aspect | Top-Down | Bottom-Up |
|--------|----------|-----------|
| **Starting point** | High-level components | Small components |
| **Approach** | Break into sub-modules | Build up to larger modules |
| **Thinking** | Backward-looking | Forward-looking |
| **Best for** | Structural programming | Object-oriented design |
| **Redundancy** | More data redundancy | Minimal redundancy |
| **OOD Preference** | Less common | ✅ **Preferred** |

### 📌 For OOD Interviews:
> **Bottom-Up is usually preferred** — start with entities, build relationships, then compose the system.

---

## ✅ Key Interview Takeaways

### Do's ✅
- ✅ Ask questions first
- ✅ Think aloud
- ✅ Design before coding
- ✅ Justify every decision
- ✅ Keep system extensible
- ✅ Use proper naming conventions
- ✅ Apply SOLID principles
- ✅ Mention design patterns

### Don'ts ❌
- ❌ Jump into coding immediately
- ❌ Make assumptions without asking
- ❌ Over-engineer the solution
- ❌ Ignore edge cases
- ❌ Design in isolation (communicate!)
- ❌ Write implementation details first

---

## 🎓 Interview Checklist

**Before You Start:**
- [ ] Clarified functional requirements
- [ ] Understood non-functional requirements
- [ ] Identified constraints and scale
- [ ] Confirmed design depth expected

**During Design:**
- [ ] Identified all major entities
- [ ] Defined attributes and methods
- [ ] Established relationships
- [ ] Applied SOLID principles
- [ ] Considered design patterns
- [ ] Thought about extensibility

**During Coding:**
- [ ] Started with interfaces/abstractions
- [ ] Wrote clean, readable code
- [ ] Used meaningful names
- [ ] Showed key relationships
- [ ] Demonstrated OOP concepts

---

## 💡 Sample Interview Dialogue

**Interviewer:** "Design a parking lot system."

**You:**
```
"Thank you. Before I start, let me clarify a few things:

1. Functional Requirements:
   - Should the system support multiple vehicle types?
   - Do we need to handle payments?
   - Should we track entry/exit times?

2. Scale:
   - How many floors?
   - How many spots per floor?
   - Expected vehicles per day?

3. Design Scope:
   - Should I focus on high-level design or full implementation?
   - Are design patterns expected?

Based on your answers, I'll start by identifying the core entities..."
```

---

## 📝 One-Line Interview Summary

> **"In an OOD interview, I clarify requirements, identify use cases, model entities and relationships, apply SOLID principles and design patterns, and then implement a clean, scalable structure."**

---

## 🎯 Common OOD Interview Problems

| Problem | Key Entities | Design Patterns |
|---------|-------------|-----------------|
| **Parking Lot** | Vehicle, Spot, Ticket | Singleton, Factory, Strategy |
| **Library System** | Book, Member, Loan | Observer, State, Composite |
| **Elevator System** | Elevator, Request, Floor | State, Strategy, Observer |
| **ATM System** | Account, Card, Transaction | State, Factory, Command |
| **Hotel Booking** | Room, Reservation, Guest | Factory, Observer, Strategy |

---

