# Requirement Collection

The set of requirements for the car rental system is listed below:

## User Roles

- **R1:** The system supports two main user roles: Customers and Receptionists.

## Vehicle Management

- **R2:** The system manages multiple types of vehicles, including cars, trucks, vans, and motorcycles.

- **R3:** Each vehicle type may have multiple subtypes, such as:
  - **Cars:** Economy, Luxury, Standard, Compact, Intermediate, Full size, Premium
  - **Vans:** Passenger, Cargo
  - **Motorcycles:** Standard, Cruiser, Touring, Sports, Off-road, Dual purpose
  - **Trucks:** Light-duty, Medium-duty, Heavy-duty

## Reservation Management

- **R4:** The system must record every reservation, including the customer details and the date/time a vehicle is issued.

- **R5:** The system can track and report the number of vehicles each customer has rented, including rental history and active reservations.

- **R6:** Customers can cancel their reservations at any time before the pickup date, subject to company policies.

- **R7:** The system maintains a vehicle log to track all significant events related to each vehicle (e.g., maintenance, repairs, accidents, assignments, or status changes).

## Additional Services and Equipment

- **R8:** Customers can add equipment to their reservations, such as a ski rack, child seat, or navigation system.

- **R9:** Customers can add extra services to their reservations, including a driver, Wi-Fi, or roadside assistance.

## Notifications and Penalties

- **R10:** If a vehicle is not returned by the due date, the system must notify the customer and automatically generate a fine according to company policy.

## Search Functionality

- **R11:** Users can search for vehicles by type, model, features, or availability at specific locations and dates.

## Branch and Location Management

- **R12:** The system must support the management of multiple branches in different cities and locations.

- **R13:** Each branch must maintain a record of parking stalls for vehicles at that location, including current status (occupied, available, reserved).

## Payment Processing

- **R14:** The system should securely process payments, refunds, and fines using multiple payment methods (cash, card, online).

- **R15:** The Customer shall be able to make payment for the car rental in both scenarios: when making the reservation and when returning the vehicle. The system should validate both.

----
# ⭐ Requirements for the Car Rental System

These requirements define the exact scope of our LLD.  
They cover roles, inventory, operations, search, services, penalties, branch management, and payments.

Below is a breakdown of every requirement (R1–R15) with **meaning, reasoning, and design impact**.

---

## 🔹 R1: Two main user roles — Customers and Receptionists

### What

The system supports two roles:

* **Customer**: end-user who rents vehicles
* **Receptionist**: branch employee who manages reservations

### Why

Roles control:

* Permissions
* Allowed actions
* UI visibility
* Operational workflows

### How (Design impact)

* Create a `User` base class and derived classes: `Customer`, `Receptionist`
* Implement authorization rules
* Receptionist can override or modify reservations

---

## 🔹 R2: Multiple vehicle types

### What

System must handle:

* Cars
* Trucks
* Vans
* Motorcycles

### Why

Rental businesses have mixed fleets.  
Different types → different pricing, rules, features.

### How

* Use `VehicleType` enum
* Maintain separate inventory per category

---

## 🔹 R3: Vehicle subtypes

### What

Each type has sub-categories.

**Cars:**
* Economy, Compact, Standard, Full-size, Luxury, Premium, etc.

**Vans:**
* Passenger, Cargo

**Motorcycles:**
* Cruiser, Sports, Touring, etc.

**Trucks:**
* Light, Medium, Heavy-duty

### Why

Subtypes define:

* Price
* Minimum age requirement
* Insurance class
* Search filters

### How

Model this using:

* `VehicleCategory` and `VehicleSubCategory`
* Mapping tables or enums

---

## 🔹 R4: Record every reservation

### What

Each reservation must store:

* Customer details
* Vehicle details
* Pickup/return date/time

### Why

Mandatory for:

* Tracking
* Billing
* Auditing
* Avoiding double-booking

### How

Use a `Reservation` entity with:

* ID
* customerId
* vehicleId
* timestamps
* status

---

## 🔹 R5: Track number of vehicles rented per customer

### What

System keeps:

* Rental history
* Active reservations
* Count of total rentals

### Why

Useful for:

* Membership benefits
* Fraud prevention
* Marketing
* Personalization

### How

A `RentalHistoryService` or aggregated fields.

---

## 🔹 R6: Customers can cancel before pickup

### What

Cancellation possible before pickup time.

### Why

Real systems allow cancellation with:

* Refund rules
* Penalty rules

### How

Reservation state transitions:

```
Reserved → Cancelled
```

Apply cancellation fee if needed.

---

## 🔹 R7: Vehicle log for all events

### What

Track events like:

* Maintenance
* Repairs
* Accidents
* Branch transfers
* Status changes

### Why

Required for:

* Audit
* Insurance
* Legal compliance
* Maintenance scheduling

### How

Table: `VehicleLog(vehicleId, eventType, time, notes)`

---

## 🔹 R8: Customers can add equipment

### What

Add-ons like:

* Child seat
* Ski rack
* GPS

### Why

Rental revenue comes heavily from add-ons.

### How

* Use `Equipment` entity
* Associate with reservation

---

## 🔹 R9: Customers can add extra services

### What

Services like:

* Driver
* Wi-Fi
* Roadside assistance

### Why

These require:

* Additional pricing
* Resource allocation (drivers)

### How

* Use `Service` entity
* Attach to reservation → update bill

---

## 🔹 R10: Late return → notification + fine

### What

If return time < current time:

* System notifies customer
* Auto-generates penalty

### Why

This is real business logic.  
Late returns block future reservations.

### How

Scheduled task or event-triggered rule:

```java
if (currentTime > dueDate) {
    generateFine();
}
```

---

## 🔹 R11: Search by type, model, features, availability, location, dates

### What

Search supports:

* Location
* Vehicle type
* Model
* Features
* Availability within dates

### Why

User experience + accurate filtering.

### How

Query against inventory + reservation table to exclude booked vehicles.

---

## 🔹 R12: Support multiple branches

### What

Cars exist at different locations.

### Why

Most rental companies have:

* Airport branches
* City branches
* Inter-city branches

### How

Model:

* `Branch` entity
* Vehicle assigned to a branch
* Branch-level inventory

---

## 🔹 R13: Parking stall tracking

### What

Each branch tracks:

* Parking spot ID
* Spot status (occupied, available, reserved)

### Why

Needed for:

* Operational clarity
* Vehicle placement
* Pickup readiness

### How

```
ParkingStall(id, branchId, status, vehicleId)
```

---

## 🔹 R14: Payments, refunds, fines (secure, multi-method)

### What

Support:

* Card
* Cash
* Online
* Refunds
* Late fee payments

### Why

Payments are core to business.

### How

* Central `Payment Service`
* Store payment records
* Payment status per reservation

---

## 🔹 R15: Payment at reservation OR return

### What

Two payment flows:

* Pay when reserving
* Pay when returning

Both must be validated.

### Why

Some companies:

* Take full payment upfront
* Take deposit upfront + balance later

### How

**Reservation:**
```
paymentStatus = PARTIAL/PAID/UNPAID
```

**Return:**
* Recalculate final amount (late fee, add-ons)
* Generate invoice

---

## ⭐ Additional Requirements You Can Mention (Gives You Extra Credit)

Interviewers love when candidates proactively mention realistic extensions:

### ✔ R16: System should prevent double-booking

### ✔ R17: Vehicles must undergo cleaning/maintenance before reuse

### ✔ R18: Staff should be able to override pricing (special cases)

### ✔ R19: Vehicle transfer between branches must update inventory

### ✔ R20: Support notifications (SMS/Email) for confirmations

**These show maturity in system design thinking.**

---

## 📊 Requirements Summary Table

| **ID** | **Requirement** | **Priority** | **Complexity** |
|--------|----------------|--------------|----------------|
| R1 | User roles | High | Low |
| R2 | Multiple vehicle types | High | Medium |
| R3 | Vehicle subtypes | High | Medium |
| R4 | Record reservations | Critical | Low |
| R5 | Track rental history | Medium | Low |
| R6 | Cancellation support | High | Medium |
| R7 | Vehicle event logging | High | Medium |
| R8 | Equipment add-ons | Medium | Low |
| R9 | Extra services | Medium | Medium |
| R10 | Late return penalties | High | Medium |
| R11 | Advanced search | Critical | High |
| R12 | Multiple branches | Critical | High |
| R13 | Parking stall tracking | Medium | Low |
| R14 | Payment processing | Critical | High |
| R15 | Flexible payment timing | High | Medium |

---

**Complete requirements coverage for interview success! 📋**