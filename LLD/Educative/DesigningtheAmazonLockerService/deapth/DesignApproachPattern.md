# ✅ Step 3: Design Approach (Bottom-Up) — Explained Simply

In the Amazon Locker system, we use a **bottom-up approach**, which means:

➤ We start from smallest, most basic components  
➤ Then slowly combine them to form complete workflows  
➤ Finally, we build the entire system layer by layer  

This is the **correct approach** for an LLD interview.

Let's break it down.

---

## ⭐ 1. Identify the Core Entities (Bottom Layer)

First step is to find the simple building blocks.

The **four most important entities** in this system are:

### 1. Locker
A single physical locker box (small/medium/large).  
**Stores:** size, status, code, package reference.

### 2. LockerLocation
A physical hub that contains many lockers.  
**Stores:** address, opening hours, list of lockers.

### 3. Package
Represents the physical parcel with size.  
**Stores:** dimensions, weight, order reference.

### 4. Order
Represents the Amazon order associated with a package.  
**Stores:** customer, package, status.

> These are the **"atoms"** of your design.  
> Everything else builds on these.

---

## ⭐ 2. Model How Lockers Are Assigned

This is the **center of the problem**.

You must show the interviewer that you'll design:

✔ Locker selection logic  
✔ Size matching  
✔ Availability check  
✔ Concurrency control  
✔ Unique pickup code generation  
✔ Booking expiry timer  

This is where your system **"comes alive"**.

### Locker assignment flow:

```
Customer chooses locker location
         ↓
System checks package size
         ↓
System finds suitable locker
         ↓
Locker is locked atomically
         ↓
Unique pickup code is generated
         ↓
Package is inserted
         ↓
Customer is notified
```

This is the **main design challenge**.

---

## ⭐ 3. Handle Pickups, Returns & Expiry

After assigning a locker, the system must support:

### ✔ Package Pickup
Customer enters the pickup code → locker opens → marked as free again.

### ✔ Returns
Customer scans return barcode → system assigns locker → logistics picks it later.

### ✔ Uncollected / Expired Packages
If time window expires:
- Locker auto-unlocks for staff
- Package is returned to warehouse
- Customer is refunded
- Locker status resets

These workflows must be **part of the design**.

---

## ⭐ 4. Enforce Operating Hours + Time Limits

Each `LockerLocation` may have:
- opening time
- closing time

You must mention:

> "Locker operations must respect location availability."

This shows **real-world understanding**.

---

## ⭐ 5. Ensure Secure Access

Security requirements:

✔ Every locker must have a **unique pickup code**  
✔ Code is valid only within **access window**  
✔ Code expires if time runs out  
✔ Code must not collide with other lockers  
✔ Admin override supported  

This is **expected in interviews**.

---

## ⭐ 6. Concurrency + Edge Cases

You need to mention:

✔ Atomic locker assignment  
✔ Prevent two customers getting same locker  
✔ Dead lockers (hardware failure)  
✔ Wrong package size  
✔ Customer picks wrong locker  
✔ Expired code  
✔ Location closed  

This shows **maturity**.

---

## ⭐ 7. Follow SOLID Principles

Interviewers love when candidates explain design justification.

### ✔ SRP (Single Responsibility Principle)
- `Locker` manages only locker data
- `LockerAssignmentService` handles assignment
- `Package` handles size
- `CodeGenerator` handles unique codes

### ✔ OCP (Open-Closed Principle)
Easy to add more locker sizes or pickup methods.

### ✔ LSP (Liskov Substitution Principle)
Substitutable types (e.g., `LargeLocker` extends `Locker`).

### ✔ ISP (Interface Segregation Principle)
Interfaces for `PickupService`, `ReturnService`, `NotificationService`.

### ✔ DIP (Dependency Inversion Principle)
High-level module depends on abstractions, not concrete classes.

> Even mentioning this gives a **strong impression**.

---

## 🧩 Design Patterns (What to Mention in Interview)

The interviewer expects you to confidently mention which design patterns apply.

### ✔ Factory Pattern
To create lockers, packages, orders.

### ✔ Strategy Pattern
To select locker assignment strategy:
- size-based
- nearest-based
- least-used locker
- first-fit locker

### ✔ State Pattern
For locker states:
- `AVAILABLE`
- `RESERVED`
- `OCCUPIED`
- `OUT_OF_SERVICE`

### ✔ Observer Pattern
For:
- notifying customer when package is delivered
- notifying staff when package expires

### ✔ Singleton Pattern
For:
- `LockerAssignmentService`
- `CodeGenerator`
- `NotificationService`

### ✔ Builder Pattern
For constructing complex `Package` or `LockerLocation` objects.

> Mentioning these patterns makes you look **senior**.

---

## 🔥 Short Summary (What You Should Say in Interview)

**"I will use a bottom-up approach.**

**I'll first design core entities like Locker, LockerLocation, Package, and Order.**

**Then I will build services that handle locker assignment, pickup, return, and expiry.**

**I will ensure concurrency safety, size matching, unique code generation, and respect operating hours.**

**My design will follow SOLID principles and use patterns like Factory, Strategy, State, and Observer."**

---

## 📊 Visual Design Summary

```
Core Entities (Bottom)
    ↓
Services Layer (Middle)
    ↓
Business Logic (Top)
    ↓
API/Interface Layer
```

### Layer Breakdown:

| Layer | Components |
|-------|-----------|
| **Core Entities** | Locker, LockerLocation, Package, Order, PickupCode |
| **Services** | LockerAssignmentService, PickupService, ReturnService, NotificationService |
| **Business Logic** | Size matching, Availability check, Expiry handling, Concurrency control |
| **API Layer** | REST endpoints, User interface, Admin panel |

---

## ✅ Key Takeaways

1. **Start small** - Begin with core entities
2. **Build up** - Add services and logic layer by layer
3. **Think real-world** - Operating hours, concurrency, failures
4. **Apply principles** - SOLID, design patterns
5. **Show maturity** - Edge cases, security, scalability

---

## 🎯 Interview Impact

If you say this confidently → **interviewer will be impressed**.

This approach demonstrates:
- 🧠 Structured thinking
- 🏗️ Architectural knowledge
- 🔐 Security awareness
- ⚙️ Real-world experience
- 📚 Design pattern expertise