# Hotel Management System Requirements (Detailed LLD Chapter)

**Understanding requirements is the most important step in LLD.** It defines system scope, constraints, and future extensibility.  
We use the notation **Rn = Requirement number**. [web:11]

## 1️⃣ Requirement Collection (What / Why / How)

### R1: Four Types of Accounts
**What**: Multiple user roles - Guest (customer), Housekeeper, Receptionist, Admin/Manager  
**Why**: Hotels have different operations - Guests book rooms → Receptionists manage front desk → Housekeepers handle cleaning → Admin configures rooms  
**How**: Implement via Role-Based Access Control (RBAC). Each role sees different UI + API permissions [web:11]

### R2: Different Room Styles
**What**: Room categories (standard, deluxe, family suite, business suite) with different pricing and amenities  
**Why**: Hotels use pricing tiers; necessary for search + filtering + billing  
**How**: RoomType entity with configurable attributes: Base price, Capacity, Amenities, Description [web:11]

### R3: Search and Book Available Rooms
**What**: Core functionality - availability search + booking  
**Why**: Prevent double booking (race conditions). Allow filtering by date, room type, amenities  
**How**: Availability engine checks: Room is NOT already booked, Date ranges do NOT overlap. Booking stored as transactional operation [web:11]

### R4: Booking with Advance Payment
**What**: Check-in date, Stay duration, Advance payment  
**Why**: Payment confirms booking; avoids fake reservations  
**How**: Payment service, Payment status (PENDING → PAID → CONFIRMED), Calculate checkout = check-in + duration [web:11]

### R5: Cancellation with Refund Rules
**What**: Cancellation allowed, Refund rules apply  
**Why**: Standard hotel policy, Makes system realistic  
**How**: Cancellation engine checks: currentTime < checkInTime - 24 hours? → Refund 100%. Else partial/no refund [web:11]

### R6: Customer Notifications
**What**: Notifications for booking confirmed, payment success, cancellation, upcoming stay, checkout invoices  
**Why**: Essential for user experience  
**How**: Notification service sends SMS/email/push notifications. Event-driven pattern (Kafka-like "BOOKING_CONFIRMED") [web:11]

### R7: Housekeeping Task Management
**What**: Track housekeeping jobs - Room cleaning, Laundry pickup, Maintenance  
**Why**: Hotel cannot assign same room unless cleaned. Provides operational insight  
**How**: HousekeepingRequest entity assigned to housekeeper. Status: PENDING → IN_PROGRESS → DONE [web:11]

### R8: Additional Billable Services
**What**: Food orders, Spa, Laundry, Room service, Paid amenities  
**Why**: Increases hotel revenue. Required for bill generation  
**How**: ServiceRequest entity added to customer's bill. Payment at checkout or instantly [web:11]

### R9: Room Key Management
**What**: Specific key per room, Master key opens certain rooms  
**Why**: Security management, Staff convenience  
**How**: Each booking → issue digital/physical key. Master keys linked to staff roles. Key validity: check-in to check-out [web:11]

### R10: Multi-Branch Support
**What**: One brand → multiple hotel locations  
**Why**: System must scale to global hotels  
**How**: Each entity belongs to a branch (HotelBranchId): Rooms, Staff, Bookings, Payments [web:11]

## ⭐ Additional Requirements (Interview Bonus Points)

- **R11**: Handle concurrent bookings safely (transactional locking, seat-hold mechanism) [web:11]
- **R12**: Generate final invoice at checkout (room charges + tax + services + promotions) [web:11]
- **R13**: Support online payment gateways (Cards, UPI, Wallet, Pay-at-hotel) [web:11]
- **R14**: Maintain complete booking history for customers and hotel reports [web:11]
- **R15**: Rooms track status (available, booked, occupied, maintenance) [web:11]
- **R16**: Staff management (add staff, assign roles, manage shifts) [web:11]
- **R17**: Audit logs for critical operations (booking, cancellation, payment, key issuance) [web:11]
- **R18**: Admin dashboard (occupancy rate, revenue, pending cleaning, active bookings) [web:11]

## ⭐ Interview Explanation Script
> "I have categorized requirements into user management, booking flow, payments, housekeeping, and operational control. The system manages multiple user roles, room types, availability search, booking with payment, cancellation rules, notifications, and key management. It supports multi-branch operations and housekeeping activities. Additionally, I'd add concurrency control, billing engine, audit logs, reports, and staff management for production-readiness." [web:11]

**Save as `Hotel-Management-LLD-Requirements.md` and download using Ctrl+S or right-click → Save As** [web:16]
