# Getting Ready: The Hotel Management System — LLD Interview Chapter

Before jumping into classes, flows, and diagrams, interviews always begin with a **problem understanding phase**.

This chapter explains what the system is, why it's needed, and what questions you must ask to refine the requirements.

---

## 1. Problem Definition (LLD Interview Version)

A **Hotel Management System (HMS)** is software that centralizes and automates key hotel operations:

- Room booking
- Room availability tracking
- Guest check-in & check-out
- Payment calculation
- Staff management (depending on scope)
- Notifications
- Maintaining digital records for customers, rooms, and reservations

> **The system replaces manual work with an online platform, reducing human errors, increasing transparency, and improving customer convenience.**

---

## 2. Why Does a Hotel Need This System? (Interview Perspective)

### ✔ Efficiency

Managing rooms manually leads to double-booking, lost records, and operational confusion.

### ✔ Automation

Automatically handles billing, availability, booking, and check-in/out timings.

### ✔ Transparency

Real-time availability and pricing displayed to both customers and staff.

### ✔ Better Customer Experience

Customers can view rooms, filter by price, amenities, and book instantly.

### ✔ Accurate Billing

The system calculates price based on duration and services consumed.

### ✔ Reporting

Manager can see occupancy, revenue, upcoming bookings, etc.

> **These points help the interviewer understand you know why the system exists.**

---

## 3. What Does the System Do? (Scope Explanation)

The system must handle the entire lifecycle of the hotel's room operations:

### 1. Manage Rooms

- Maintain a fixed list of rooms
- Store room types (Single, Double, Suite, etc.)
- Track availability
- Update status when booked / checked-in / checked-out

### 2. Manage Customers

- Collect customer details
- Maintain booking history
- Generate stay record

### 3. Manage Bookings

- Allow users to search for rooms by date, price, type
- Create a reservation
- Prevent double booking
- Modify or cancel bookings

### 4. Check-in / Check-out

- Track the exact check-in time
- Track check-out time
- Calculate bill based on stay duration

### 5. Payments

- Calculate total cost (room cost × days)
- Add tax or extra services
- Generate invoice

### 6. Staff / Manager Dashboard

- They can see upcoming stays
- Manage room inventory
- Manage hotel operations

---

## 4. Functional Expectations (High-Level)

### ✔ Customer Portal

- View room details
- Book room
- View booking confirmation

### ✔ Manager/Admin Portal

- Add new rooms
- View current occupancy
- Edit room pricing
- View customer reservations
- Check guests in/out
- Generate bills

### ✔ Backend Processes

- Prevent overlapping bookings
- Store booking history
- Maintain accurate room state transitions

---

## 5. Key Clarifying Questions You MUST Ask (Interview Critical Section)

These questions show you think like a real system designer.

**Interviewers expect these.**

### A. Room-Related Questions

- Do rooms have categories (Single, Double, Suite)?
- Do rooms have different pricing?
- Can prices change seasonally or dynamically?
- Do rooms have amenities we need to store (AC, TV, Wi-Fi)?
- How many rooms does the hotel have? Is it fixed or dynamic?

### B. Booking-Related Questions

- Can a customer book multiple rooms at once?
- Is same-day booking allowed?
- Do we support partial-day charges (hourly)?
- Can customers modify their booking dates?
- Cancellation rules? Refund rules?

### C. Check-in/Check-out

- Is early check-in allowed? Late check-out?
- How is billing calculated — per day, per hour, per night?

### D. Payment

- Do we integrate with an online payment gateway?
- Do we store invoices and payment history?

### E. User Types

- Do we have different roles — Manager, Receptionist, Customer?
- Does the customer need an account? Or can they book as guest?

### F. Staff Management (Optional Scope)

- Do we include staff management (salaries, shifts)?
- Is staff management part of MVP?

### G. Notifications

- Do we send booking confirmation emails?
- Should the system send reminders before check-in or check-out?

---

## 6. What the Interviewer Really Wants To See Here

| What You Show | Why It Matters |
|---------------|----------------|
| You understand the real-world domain | Shows practical thinking |
| You ask clarifying questions | Shows thoroughness |
| You do not jump straight into classes | Shows discipline |
| You identify what should be included vs excluded | Shows scope management |
| You talk about scope management | Shows maturity |
| You recognize the system is large but stay focused on **Room + Booking + Payment as the MVP** | Shows prioritization |

---

## Quick Reference: System Components Overview

```
Hotel Management System
├── Room Management
│   ├── Room inventory
│   ├── Room types
│   ├── Availability tracking
│   └── Status updates
│
├── Customer Management
│   ├── Customer details
│   ├── Booking history
│   └── Stay records
│
├── Booking Management
│   ├── Search & filter
│   ├── Reservation creation
│   ├── Double-booking prevention
│   └── Modification/cancellation
│
├── Check-in/Check-out
│   ├── Time tracking
│   └── Bill calculation
│
├── Payment Processing
│   ├── Cost calculation
│   ├── Tax & extras
│   └── Invoice generation
│
└── Staff Dashboard
    ├── Occupancy view
    ├── Inventory management
    └── Operations control
```

---

## MVP Scope (Minimum Viable Product)

For LLD interviews, focus on:

### Core Features (MUST HAVE)

- ✅ Room management (CRUD operations)
- ✅ Booking management (search, create, cancel)
- ✅ Availability checking
- ✅ Check-in/check-out
- ✅ Payment calculation
- ✅ Customer records

### Optional Features (NICE TO HAVE)

- Staff management
- Advanced reporting
- Dynamic pricing
- Loyalty programs
- Room service management
- Housekeeping tracking

> **Always clarify with the interviewer which features are in scope!**

---

## Problem Understanding Checklist

Before moving to requirements and design:

- [ ] Understood the domain (hotel operations)
- [ ] Identified key actors (Customer, Manager, Receptionist)
- [ ] Listed core operations (Book, Check-in, Check-out, Pay)
- [ ] Asked clarifying questions about scope
- [ ] Defined what's included in MVP
- [ ] Identified what's excluded from current scope
- [ ] Understood room lifecycle states
- [ ] Understood booking lifecycle
- [ ] Clarified payment handling

---

## Interview Response Template

When asked to design a Hotel Management System:

> **"Before I start designing, let me clarify the scope. A Hotel Management System handles room inventory, bookings, check-in/check-out, and payments. I'd like to understand a few things: Should we focus on room booking as the MVP? Do we need to handle staff management? Are there different room types with different pricing? Can customers modify or cancel bookings? Should we integrate with payment gateways? Once I understand the scope, I'll design a system with proper entities like Room, Booking, Customer, and services for availability checking and booking management."**

---

## Key Terminology

| Term | Definition |
|------|------------|
| **HMS** | Hotel Management System |
| **MVP** | Minimum Viable Product |
| **Booking** | Reservation made by customer |
| **Check-in** | Guest arrives and takes possession of room |
| **Check-out** | Guest leaves and settles bill |
| **Occupancy** | Percentage of rooms currently occupied |
| **Room Type** | Category like Single, Double, Suite |
| **Availability** | Whether a room is free for booking |

---

