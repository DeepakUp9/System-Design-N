# 🔶 Step 1: Understanding the Amazon Locker Service Problem

Before designing any system (LLD or HLD), the first skill interviewers check is:

👉 **Can you fully understand and frame the problem?**  
👉 **Can you identify hidden constraints?**  
👉 **Can you ask the right clarifying questions?**

So let's analyze this scenario from basic to advanced.

---

## ✅ 1. What Amazon Locker Service Actually Does (Simple Version)

Amazon Locker is like a **self-service delivery box**.

- Amazon delivers packages to a locker instead of your home.
- Each locker contains many **physical compartments**.
- Each compartment has a **size** (Small, Medium, Large).
- When a package arrives:
  - Locker system assigns a compartment based on size.
  - Customer receives a **pickup code**.
- Customer goes to the locker, enters the code → locker opens.

**Also:**
- Customers can **return products** by putting them into a locker.
- Lockers keep packages for a **limited time** (e.g., 3 days).
- If not collected → system removes the package and handles refund.

---

## ✅ 2. Why This Problem Is Perfect for LLD (Important!)

This problem includes:

### 🔹 Resource allocation
Assigning the right locker size to the right package at the right time.

### 🔹 State management
Lockers change states:
- `AVAILABLE`
- `OCCUPIED`
- `IN_MAINTENANCE`
- `RESERVED`
- `EXPIRED`

### 🔹 Concurrency
- Multiple deliveries happening at the same time.
- Multiple users requesting lockers simultaneously.

### 🔹 Time-based constraints
- Lockers have operational hours.
- Packages expire after X hours/days.

### 🔹 Security
- Unique pickup codes
- Safe access window
- Preventing unauthorized access

### 🔹 Two workflows
1. **Delivery flow** (Amazon → locker → customer)
2. **Return flow** (Customer → locker → Amazon pickup)

This makes it a **rich and realistic LLD problem**.

---

## ✅ 3. What The Interview Wants From You at This Step

Before diving into classes, diagrams, or flows, interviewer wants to see:

### 🔸 Can you identify key entities?

**Examples:**
- `LockerLocation`
- `Locker`
- `LockerSize`
- `Package`
- `PickupCode`
- `AccessWindow`
- `Order`
- `ReturnRequest`

### 🔸 Can you detect constraints?

**Examples:**
- Package must fit locker size
- Every locker can store only 1 package at a time
- Packages have expiry window
- Lockers may have downtime (maintenance)

### 🔸 Do you notice system challenges?

- What if multiple packages need a "Large" locker but only 1 is available?
- What happens when a locker fails physically?
- What happens when customer arrives after operational hours?
- What if the pickup code expires?

---

## ✅ 4. Hidden Questions You Must Ask (Advanced Interview Skill)

To simplify the problem before designing, you ask questions like:

### 📌 Locker Constraints
- What sizes are available? (S/M/L or custom volumes)
- How many lockers are there per location?
- Are locker sizes fixed per location?

### 📌 Package Constraints
- What is the mapping of package size → locker size?
- What do we do if no locker is available? (queue, fail, retry)

### 📌 Operational Constraints
- What are locker operating hours?
- Are pickups allowed outside hours?

### 📌 Time Constraints
- How long do we store packages?
- What happens if a package is uncollected?

### 📌 Retrieval Constraints
- How are pickup codes generated?
- Do they expire?
- Can customers regenerate a code?

### 📌 Return Flow
- Are all lockers available for return?
- Does the system allocate a locker first or customer picks any?

### 📌 Failure Scenarios
- Locker is broken
- Delivery agent cannot find allotted locker
- Customer enters wrong code too many times
- System outage

**These clarifying questions show you understand the real problem.**

---

## ✅ 5. Summary (What You Should Take Away From Step 1)

You must understand **3 things clearly**:

### 1️⃣ What is the system?
A smart automated locker system for storing & retrieving packages safely.

### 2️⃣ What must the system do?
- Allocate locker based on size & availability
- Provide pickup/return flow
- Manage locker states
- Handle expiration, codes, hours

### 3️⃣ What challenges must system solve?
- Size mismatch
- Locker unavailability
- Concurrency issues
- Expiration & failure handling

---
