# ✅ Step 4: Requirements for Amazon Locker Service (R1 – R12)

Requirements define **exactly what the system must do**. These will directly shape your use cases, activity diagrams, and class design.

We assign each requirement an ID: **Rn → Requirement number n**

---

## ⭐ Functional Requirements (What the System Must Do)

### R1 — Customer Chooses Locker Location
Customer can select a preferred locker location during checkout.

### R2 — Package Grouping Rules
An order may contain multiple items. If locker size permits, items are packaged into one package; otherwise, multiple packages.

### R3 — Locker Size Variants
Each location contains multiple lockers in sizes:
- XS
- S
- M
- L
- XL
- XXL

### R4 — Eligibility Based on Size
Only packages that physically fit inside locker dimensions can be delivered via lockers.

### R5 — Unique Pickup Code Generation
Once a package is delivered to the locker, customer receives a unique code to open it.

### R6 — Storage Time Limit
Package can stay in locker **maximum of 3 days**.

### R7 — Operating Hours Constraint
Pickup must happen within:
- 3-day window
- Locker location opening hours

**Both conditions must be satisfied.**

### R8 — Uncollected/Expired Packages
If customer doesn't pick up:
- Package removed by staff
- Locker freed
- Customer refunded

### R9 — Locker Assignment Rule
**Only one package can occupy one locker at a time.**

No overlaps. No sharing.

### R10 — Code Invalidation
After customer pickup:
- Locker closes
- Code expires immediately
- Locker becomes available again

### R11 — Return Flow Support
Customers can return items at lockers:
- System assigns an appropriate locker
- Customer gets a new code
- Customer drops return package

### R12 — Logistics Team Pickup
For returns:
- Logistics team uses a separate unique code
- Takes package
- Customer notified
- Refund per product policy

---

## ⭐ What These Requirements Achieve

They collectively ensure:

✔ Safe package storage  
✔ Reliable pickup & return flow  
✔ Clear rules for locker usage  
✔ Secure code-based access  
✔ Smooth operations even at scale  
✔ Separation of customer flow and logistics flow  
✔ Time-bound storage to avoid locker hoarding  

These requirements now allow us to **properly define use cases**.

---

## 📋 Requirements Summary Table

| ID | Requirement | Type | Priority |
|----|-------------|------|----------|
| R1 | Customer Chooses Locker Location | Functional | High |
| R2 | Package Grouping Rules | Business | Medium |
| R3 | Locker Size Variants | Physical | High |
| R4 | Eligibility Based on Size | Validation | High |
| R5 | Unique Pickup Code Generation | Security | Critical |
| R6 | Storage Time Limit | Business | High |
| R7 | Operating Hours Constraint | Business | High |
| R8 | Uncollected/Expired Packages | Business | High |
| R9 | Locker Assignment Rule | Concurrency | Critical |
| R10 | Code Invalidation | Security | Critical |
| R11 | Return Flow Support | Functional | High |
| R12 | Logistics Team Pickup | Operational | High |

---

## 🔄 Requirements Flow Diagram

```
Customer Orders Product
        ↓
    [R1] Selects Locker Location
        ↓
    [R2] Items Grouped into Package(s)
        ↓
    [R4] Size Validation Check
        ↓
    [R3] Suitable Locker Size Selected
        ↓
    [R9] One Package ↔ One Locker
        ↓
    Package Delivered to Locker
        ↓
    [R5] Unique Code Generated
        ↓
    [R6][R7] Within 3 Days + Operating Hours
        ↓
    Customer Picks Up Package
        ↓
    [R10] Code Invalidated + Locker Freed
```

### Alternative Flow: Return

```
Customer Initiates Return
        ↓
    [R11] System Assigns Return Locker
        ↓
    Customer Receives Return Code
        ↓
    Customer Drops Package
        ↓
    [R12] Logistics Team Picks Up
        ↓
    Customer Refunded
```

### Edge Case Flow: Expiry

```
Package in Locker > 3 Days
        ↓
    [R8] Staff Removes Package
        ↓
    Locker Freed
        ↓
    Customer Refunded
```

---

## 🎯 Design Implications

### Security Requirements
- **R5, R10**: Need secure code generation and validation mechanism
- **R12**: Separate code system for logistics team

### Concurrency Requirements
- **R9**: Atomic locker assignment to prevent double-booking

### Time-Based Requirements
- **R6, R7, R8**: Need expiry tracking and scheduled cleanup jobs

### Size Management Requirements
- **R3, R4**: Size validation and matching algorithm

### Business Logic Requirements
- **R2**: Smart packaging logic
- **R8**: Refund integration

---

## ⭐ What We Do Next

As per your material, the next section is:

### **"Use Cases for the Amazon Locker System"**

Use cases typically include:

- **UC1**: Select Locker Location
- **UC2**: Assign Locker to Order
- **UC3**: Deliver Package to Locker
- **UC4**: Customer Pickup
- **UC5**: Expiry Handling
- **UC6**: Return Assignment
- **UC7**: Return Dropoff
- **UC8**: Logistics Pickup

---

## 🔍 Requirements Traceability

### Delivery Flow (Main Path)
**R1** → **R2** → **R3** → **R4** → **R5** → **R6** → **R7** → **R9** → **R10**

### Return Flow
**R11** → **R3** → **R4** → **R5** → **R12**

### Exception Handling
**R8** (triggers when R6 or R7 violated)

---

## ✅ Validation Checklist

Before moving to use cases, ensure all requirements are:

- [ ] **Clear**: No ambiguity in what needs to be done
- [ ] **Testable**: Can be verified through test cases
- [ ] **Feasible**: Technically possible to implement
- [ ] **Necessary**: Directly supports business goals
- [ ] **Traceable**: Can be mapped to use cases and classes

---

## 💡 Interview Tips

When presenting requirements:

1. **Start with the ID**: "Let me walk through R1..."
2. **Explain the why**: Why this requirement exists
3. **Show impact**: How it affects design
4. **Mention dependencies**: Which requirements depend on each other
5. **Identify conflicts**: Any requirements that might conflict

Example:
> "R9 states one package per locker, which is critical for R10 - code invalidation. If we allowed sharing, we'd need complex code management. This simplifies our design while ensuring security."

---

## 🎓 Key Takeaways

1. **Requirements drive design** - Every class/method should trace back to a requirement
2. **Be specific** - "3 days" not "a few days"
3. **Think flows** - Requirements should cover happy path, alternative path, and exceptions
4. **Consider all actors** - Customer, logistics team, system admin
5. **Balance** - Between flexibility and simplicity

---

## 📌 Next Steps

With these 12 requirements defined, you can now:
- ✅ Design detailed use cases
- ✅ Create activity diagrams
- ✅ Define class responsibilities
- ✅ Identify design patterns
- ✅ Plan database schema