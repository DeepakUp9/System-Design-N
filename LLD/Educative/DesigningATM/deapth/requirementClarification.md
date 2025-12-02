# LLD Chapter: ATM System — Getting Ready

Before designing any system in LLD, you always start with a problem definition and clarifying questions. This helps reduce ambiguity and ensures you design only what is required.

---

## 1. What is the ATM? (Concept Level)

An ATM is a self-service banking machine. It replaces a human teller for common operations.

### Key actions it supports:
- Insert card → Authenticate → Perform banking operations

---

## 2. Core Functionalities We Must Design

Your LLD interview focus stays on these four:

### 1. Card + PIN Authentication
- Detect card insertion
- Read card data (number, bank, expiry)
- Verify PIN securely
- Block card after max failed attempts

### 2. Main Banking Operations
- Withdraw cash
- Deposit cash
- Check balance
- Transfer funds

### 3. ATM Machine Cash Management
- Cash dispenser storage
- Notes inventory (2000, 500, 100, etc.)
- Insufficient cash handling
- Daily withdrawal limits

### 4. User Experience + Hardware Coordination
- Card reader
- Cash dispenser
- Cash deposit slot
- Receipt printer
- Display screen
- Keypad

---

## 3. Why This Problem Needs Clarifying Questions

Every ATM system can vary based on:
- Bank policies
- Hardware types
- Supported transactions
- Security levels

### So in an LLD round, you typically clarify:

**✔ Are we building only software logic, or hardware + software model?**
> Usually both at conceptual level: hardware components + software classes.

**✔ Should the ATM support multiple banks?**
> Typically yes, via backend bank servers.

**✔ Do we design back-end banking system too?**
> No. Only ATM requesting operations from the bank system.

**✔ Are we designing concurrency or load?**
> No. ATM is single-user at a time.

**✔ Deposit includes cash or cheque?**
> Usually cash; cheque optional.

---

## 4. The Scope You Will Design in LLD

When we later draw classes and interactions, the scope includes:

### Hardware Modules
- `CardReader`
- `Keypad`
- `Screen`
- `CashDispenser`
- `CashDepositSlot`
- `ReceiptPrinter`

### Software Modules
- `ATM`
- `Transaction` (Withdraw, Deposit, Transfer, Balance Inquiry)
- `BankServer` (external API)
- `Card`
- `Account`
- `User`
- `SessionManager`
- `CashManager`

---

## 5. Interview Expectation

They want:

| Aspect | Description |
|--------|-------------|
| **Clear use cases** | Well-defined user scenarios |
| **Responsibilities** | Each class has single, clear purpose |
| **Well-separated classes** | Proper encapsulation and modularity |
| **Interaction flows** | Sequence diagrams showing communication |
| **Correct error handling** | Invalid PIN, insufficient cash, card blocked |

### Key Error Scenarios to Handle:
- Invalid PIN
- Insufficient cash in ATM
- Card blocked
- Insufficient account balance
- Network/bank server failure
- Daily withdrawal limit exceeded

---

## Quick Reference: Component Breakdown

### Hardware Layer
```
CardReader → Reads card data
Keypad → Accepts PIN input
Screen → Displays messages
CashDispenser → Dispenses cash
CashDepositSlot → Accepts cash deposits
ReceiptPrinter → Prints transaction receipt
```

### Software Layer
```
ATM → Main controller
SessionManager → Manages user session
CashManager → Manages ATM cash inventory
Transaction → Abstract base for operations
BankServer → External API interface
```

---

*LLD Interview Preparation - ATM System Design*