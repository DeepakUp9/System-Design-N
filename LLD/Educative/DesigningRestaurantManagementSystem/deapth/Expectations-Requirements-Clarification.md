# LLD Chapter — Understanding Expectations & Requirements Clarification

A strong LLD interview **always starts with requirement clarification**.

Interviewers expect you to **ask smart questions** before jumping into design.

This part shows:
- ✅ You understand real-world constraints
- ✅ You don't assume things
- ✅ You approach design like a backend engineer, not a coder

---

## Topic: What Are Interviewers Expecting?

Interviewers want you to:

| Expectation | Why It Matters |
|-------------|----------------|
| **Understand all major components** | Shows domain knowledge |
| **Identify hidden constraints** | Shows attention to detail |
| **Ask clarifying questions** | Shows systematic thinking |
| **Think about scalability** | Shows architectural maturity |
| **Consider operational flow** | Shows real-world experience |
| **Consider restaurant variations** | Shows adaptability |
| **Avoid over/under-engineering** | Shows balanced approach |

> **They want to see your thinking approach, not only UML diagrams.**

---

## Topic: Why Are These Questions Important?

These questions reveal:

- 📋 Boundaries of the system
- 📋 Optional vs mandatory features
- 📋 Real-world needs
- 📋 Scope of the LLD
- 📋 Integrations needed
- 📋 How complex your solution must be

> **Skipping questions leads to wrong design, which is a common LLD failure.**

---

## Topic: How to Ask Correct Questions in an LLD Interview

You should **group your questions by functional area**.

This makes your thought process look **structured and professional**.

Below are the grouped questions with full interview-ready reasoning.

---

## Section 1 — Restaurant Services (Customer-Facing Features)

These questions clarify **how customers interact with the system**.

---

### 1. Does the restaurant provide delivery service?

#### What it impacts:

- Need for delivery module
- Delivery status tracking
- Delivery staff or third-party API integration
- Extra states: `OUT_FOR_DELIVERY → DELIVERED`

#### Why it matters:

It changes the whole flow—orders are no longer tied to tables.

#### How you use it in design:

You create separate classes like:
- `DeliveryOrder`
- `DeliveryPartner`
- Delivery queues

---

### 2. Can a customer place an online order?

#### What it impacts:

- Need for mobile/web portal
- Customer profile
- Online menu
- Address management
- Online payment gateway

#### Why it matters:

Online orders follow a different workflow → no table assignment.

#### How you use it:

Add separate flow in the state machine:

```
ORDER_PLACED → CONFIRMED → PREPARING → READY → PICKED_UP
```

---

### 3. Does the restaurant accept online/card payments?

#### What it impacts:

- Payment gateway integration
- Payment confirmation callback
- Refund flow
- Bill split logic

#### Why it matters:

This adds complexity — handling failed payments, multiple payment modes.

#### How it affects LLD:

You will model:
- `Payment`
- `PaymentMethod`
- `Charge`
- `Refund`

> **You may mention Strategy Pattern for different payment methods.**

---

## Section 2 — Restaurant Management (Back-Office Operations)

These questions clarify **how the restaurant is structured**.

---

### 4. Can the restaurant have multiple branches?

#### What it impacts:

- Multi-tenant architecture
- Branch-specific menus
- Inventory per branch
- Separate staff per branch

#### Why it matters:

A **single-branch design** is MUCH simpler than multi-branch.  
Asking this question shows foresight.

#### How you use it:

Add entities:
- `Branch`
- `BranchMenu`
- `BranchStaff`
- `BranchInventory`

Add relationships:

```
Branch → Tables
Branch → Orders
Branch → Payments
```

---

### 5. Do we need to account for inventory management?

#### What it impacts:

- Ingredient tracking
- Low-stock alerts
- Menu availability
- Wastage reporting

#### Why it matters:

Inventory is a **large module**—without asking, you may overbuild or underbuild.

#### How you use it in design:

Add classes like:
- `Ingredient`
- `InventoryItem`
- `StockMovement`
- `Supplier`

You can also mention:
> **Observer Pattern for "low stock notification".**

---

## Extra Important Questions You Should Ask (Interviewers Love These)

### A. Table & Reservation Rules

| Question | Why It Matters |
|----------|----------------|
| Can customers choose specific tables? | Affects reservation logic |
| Is there a maximum time allowed per table? | Affects table turnover calculation |
| What is the cancellation policy? | Affects reservation state management |

---

### B. Order Handling

| Question | Why It Matters |
|----------|----------------|
| Can items be modified after ordering? | Affects order state management |
| Can items be cancelled partially? | Affects kitchen workflow |
| Do we support special notes? (e.g., "no onion", "extra spicy") | Affects OrderItem entity |

---

### C. Menu Management

| Question | Why It Matters |
|----------|----------------|
| Does the menu change daily? | Affects menu versioning |
| Are there combos or special discounts? | Affects pricing logic |
| Are items unavailable when ingredients are low? | Affects inventory integration |

---

### D. Staff Module

| Question | Why It Matters |
|----------|----------------|
| Should the system track waiter assignments? | Affects table assignment logic |
| Do chefs have their own interface for viewing orders? | Affects role-based access |
| Do managers need daily/weekly reports? | Affects reporting module |

---

### E. Billing

| Question | Why It Matters |
|----------|----------------|
| Is split bill required? | Affects payment logic |
| Do we need GST/tax calculations? | Affects bill calculation |
| Do we allow loyalty points or discount coupons? | Affects pricing and payment |

---

### F. Kitchen Workflow

| Question | Why It Matters |
|----------|----------------|
| Do we track cooking time? | Affects order scheduling |
| Do chefs update order status? | Affects state transitions |
| Do we need a separate bar/kitchen section? | Affects order routing |

---

### G. Non-functional Requirements

**Interviewers expect these:**

| Requirement | Question |
|-------------|----------|
| **Expected load** | How many orders per hour during peak? |
| **Peak hour traffic** | How many concurrent users? |
| **System downtime tolerance** | What's the availability requirement? |
| **SLA for online orders** | Response time requirements? |

> **These questions show you're thinking like a real systems engineer.**

---

## LLD Interview Bonus Tips (Highly Valued)

### 1. Never jump to class diagram immediately.

**Always start with clarifying questions.**

---

### 2. Use these phrases:

- 💬 "Before designing, I want to clarify functional boundaries."
- 💬 "I want to understand the scale to avoid over-engineering."
- 💬 "These questions help define the scope of the system."

---

### 3. After asking questions, you summarize:

> **"Based on these clarifications, I'll design a system that supports dine-in, reservations, billing, and optionally online orders and inventory depending on scope."**

---

## Question Categories Summary

### Essential Questions (Must Ask)

| Category | Key Questions |
|----------|---------------|
| **Service Type** | Dine-in only? Delivery? Takeaway? |
| **Payment** | Cash only? Cards? Online payment? |
| **Scale** | Single branch? Multiple locations? |
| **Features** | Reservations? Online ordering? Inventory? |

---

### Advanced Questions (Shows Depth)

| Category | Key Questions |
|----------|---------------|
| **Reservation** | Table selection? Time limits? Cancellation? |
| **Order** | Modifications? Partial cancellation? Special notes? |
| **Menu** | Dynamic? Combos? Availability tracking? |
| **Staff** | Role-based access? Chef interface? Reports? |
| **Billing** | Split bills? Tax? Discounts? Loyalty? |

---

### Non-Functional Questions (Shows Maturity)

| Category | Key Questions |
|----------|---------------|
| **Performance** | Expected load? Peak traffic? |
| **Reliability** | Downtime tolerance? Backup? |
| **Scalability** | Growth projections? Multi-tenant? |
| **Security** | Payment security? Data privacy? |

---

## Impact Analysis Table

| Feature | If YES | If NO |
|---------|--------|-------|
| **Delivery** | Add: DeliveryOrder, DeliveryPartner, tracking | Simpler: Table-only flow |
| **Online Order** | Add: Customer portal, address, online payment | Simpler: In-house only |
| **Multi-Branch** | Add: Branch entity, multi-tenant | Simpler: Single database |
| **Inventory** | Add: Ingredient, Stock, Supplier | Simpler: Assume unlimited stock |
| **Reservations** | Add: Reservation entity, confirmation flow | Simpler: Walk-in only |

---

## Interview Flow Template

### Step 1: Initial Clarification (2-3 minutes)

```
"Before I start designing, let me clarify a few things:

1. Service Model:
   - Is this dine-in only, or do we support delivery/takeaway?
   
2. Payment:
   - Cash only, or do we need online payment integration?
   
3. Scale:
   - Single restaurant or multiple branches?
   
4. Core Features:
   - Do we need reservations?
   - Should we track inventory?
   - Online ordering required?"
```

---

### Step 2: Deep Dive (3-5 minutes)

```
"Let me dig deeper into a few areas:

Table Management:
- Can customers select specific tables?
- Any time limits per table?

Order Handling:
- Can orders be modified after placing?
- Do we support special preparation notes?

Billing:
- Split bill support needed?
- Tax calculations required?

Kitchen:
- Do chefs update order status themselves?
- Separate bar/kitchen sections?"
```

---

### Step 3: Non-Functional (1-2 minutes)

```
"A few questions on scale and performance:

- Expected order volume during peak hours?
- Acceptable response time?
- Any specific availability requirements?"
```

---

### Step 4: Summarize (1 minute)

```
"Based on our discussion, I'll design a system that:

Core Features:
- Table management with real-time status
- Order management with kitchen workflow
- Bill generation with tax calculation
- Payment processing

Optional (based on clarification):
- Reservation system
- Online ordering
- Delivery tracking
- Inventory management
- Multi-branch support

I'll use State pattern for order/table states, 
Observer for notifications, and Strategy for 
payment methods. Does this align with expectations?"
```

---

## Red Flags to Avoid

| What NOT to do | Why It's Bad |
|----------------|--------------|
| **Jump to coding immediately** | Shows lack of planning |
| **Assume everything is needed** | Over-engineering |
| **Never ask questions** | Shows inexperience |
| **Ask too many trivial questions** | Wastes time |
| **Ignore non-functional requirements** | Incomplete design |

---

## Green Flags That Impress

| What TO do | Why It's Good |
|------------|---------------|
| **Group questions logically** | Shows structured thinking |
| **Ask about edge cases** | Shows real-world awareness |
| **Mention patterns while asking** | Shows design knowledge |
| **Clarify scale early** | Shows performance consciousness |
| **Summarize before designing** | Shows communication skills |

---

## Common Interview Scenarios

### Scenario 1: Interviewer Says "Keep it simple"

**Your Response:**
> "Understood. I'll focus on core dine-in experience: table management, order taking, kitchen coordination, and billing. I'll skip delivery, online ordering, and inventory unless needed."

---

### Scenario 2: Interviewer Says "Think large scale"

**Your Response:**
> "Got it. I'll design for multiple branches with centralized management. I'll consider caching for menu, event-driven architecture for real-time updates, and separate read/write databases for performance."

---

### Scenario 3: Interviewer Says "What would you add if you had more time?"

**Your Response:**
> "I'd add: advanced analytics dashboard, customer loyalty program, predictive inventory management, integration with third-party delivery platforms, and mobile apps for customers and staff."

---
