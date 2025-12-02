# LLD Chapter: ATM System — Design Approach

Your approach tells the interviewer **how you think** before coding.

A **bottom-up strategy** is perfect for this system because ATM is a hardware-driven system.

---

## 1. Bottom-Up Design — WHAT, WHY, HOW

### WHAT is Bottom-Up?

**Start small → build up.**

Begin with individual components → combine to form a full ATM.

### WHY use Bottom-Up for ATM?

Because ATM consists of physical hardware parts, each with very specific responsibilities.

**Examples:**
- `CardReader` reads card details
- `Keypad` captures PIN
- `Screen` shows messages
- `CashDispenser` gives money
- `Printer` generates receipts

Each hardware module is a separate abstraction.

### HOW does this help?

✅ Clear responsibilities  
✅ Easy to test  
✅ Easy to replace hardware modules  
✅ Aligns with SOLID principles (especially SRP + DIP)

---

## 2. Steps Inside the Bottom-Up Approach

### Step 1 — Identify & Model Hardware Components

Break into small hardware classes:

| Component | Responsibility |
|-----------|----------------|
| `CardReader` | Reads card details |
| `Keypad` | Takes PIN, amount |
| `Screen` | Displays messages |
| `CashDispenser` | Dispense notes |
| `Printer` | Prints receipts |
| `CashDepositSlot` | (if deposit supported) |

Each component has:
- Responsibilities
- Error scenarios
- Interaction methods

### Step 2 — Compose These Components Into ATM Logical Modules

Once small pieces are ready, merge them to build higher-level modules like:

- Authentication module
- Transaction module
- Cash management module
- Session manager

These modules orchestrate hardware + backend communication.

### Step 3 — Model User Interactions & Transaction Flows

Design the flows:

```
Insert card → Authenticate → Select transaction → Execute → Print receipt → Return card
```

You also define:
- State transitions
- Error handling (wrong PIN, insufficient funds, no cash)
- Timeouts
- Card blocking

### Step 4 — Address Edge Cases & Hardware Failures

Examples:
- `CashDispenser` jam
- `CardReader` error
- `Printer` out of paper
- Network down
- User cancels mid-transaction

**All these must be handled gracefully.**

### Step 5 — Keep the Design Modular with SOLID

SOLID fits naturally here:

| Principle | Application |
|-----------|-------------|
| **SRP** | Every component has one job |
| **OCP** | Add new transaction types without modifying existing ones |
| **LSP** | Transaction subclasses (Withdrawal, Deposit) follow base Transaction class |
| **ISP** | Hardware interfaces separated (`IPrint`, `IDispenseCash`) |
| **DIP** | ATM depends on abstractions, not concrete hardware classes |

---

## 3. Required Design Patterns for ATM

Interviewers love when candidates mention patterns.

ATM systems touch multiple classic patterns.

### 1. State Pattern ⭐ (Most Important)

| Aspect | Details |
|--------|---------|
| **WHAT** | ATM changes states (Idle → CardInserted → Authenticated → SelectingTransaction → Processing → Completed) |
| **WHY** | ATM behavior depends on its current state |
| **HOW** | Create a `State` interface → each state is a concrete class |

### 2. Strategy Pattern (For Transactions)

| Aspect | Details |
|--------|---------|
| **WHAT** | Different transaction types share a common interface but have different logic |
| **WHY** | Add new transaction types without modifying existing code |
| **HOW** | `Transaction` is an interface → `Withdrawal`, `Deposit`, `Transfer` are strategies |

### 3. Factory Pattern (For Transaction Creation)

| Aspect | Details |
|--------|---------|
| **WHAT** | Create the right transaction object based on user selection |
| **WHY** | Avoid if-else chains |
| **HOW** | `TransactionFactory.create(TransactionType.WITHDRAW)` |

### 4. Command Pattern

| Aspect | Details |
|--------|---------|
| **WHAT** | Each user action can be represented as a command |
| **WHY** | Encapsulates actions and supports logging / undo (e.g., rollback if transaction failed) |
| **HOW** | Each transaction is a command with `execute()` |

### 5. Observer Pattern (Optional)

| Aspect | Details |
|--------|---------|
| **WHAT** | ATM can notify systems like bank server, audit logger, monitoring system |
| **WHY** | Helps with event-based updates |
| **HOW** | Observers listen to events such as cash low, suspicious activity, failure |

### 6. Singleton Pattern (Limited Use)

For things like `LogManager`, `BankNetworkManager`.

---

## Bonus Patterns (If Interviewer Asks More)

### ✔ Template Method
Reusable flow for common transaction steps (authenticate → deduct → update → notify).

### ✔ Adapter
For different hardware vendors (different `CardReader` or `Printer` implementations).

---

## 4. How This Impresses the Interviewer

| What You Show | Why It Matters |
|---------------|----------------|
| Mentioning hardware components | Shows practical knowledge |
| Breaking into modules | Shows clean architecture |
| Using patterns | Shows design maturity |
| Explaining WHAT/WHY/HOW | Shows clarity of thought |
| Handling edge cases | Shows real-world understanding |

---

## Quick Reference: Design Pattern Mapping

```
State Pattern        → ATM state transitions
Strategy Pattern     → Transaction types
Factory Pattern      → Transaction creation
Command Pattern      → Action encapsulation
Observer Pattern     → Event notifications
Singleton Pattern    → Shared managers
Template Method      → Common transaction flow
Adapter Pattern      → Hardware abstraction
```

---

## Design Flow Summary

```
1. Hardware Components (Bottom)
   ↓
2. Logical Modules (Middle)
   ↓
3. User Flows & States (Top)
   ↓
4. Edge Cases & Error Handling
   ↓
5. Apply SOLID & Design Patterns
```
