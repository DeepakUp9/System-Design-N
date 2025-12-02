# LLD Chapter: ATM System — Requirements

Before designing an ATM, we must capture all **functional and operational requirements**.

These requirements define exactly what the system must do and what constraints shape your design.

This is the foundation for:
- Class diagrams
- Sequence diagrams
- State machines
- Transaction flows

**Each requirement below includes:**  
WHAT the requirement means, WHY it exists, HOW it affects the design.

---

## Requirement Collection

### R1: Single Account Access Through Card

| Aspect | Details |
|--------|---------|
| **WHAT** | Each user has one account linked to the card they insert |
| **WHY** | Simplifies the ATM flow — no need to select between multiple accounts per card |
| **HOW in LLD** | • `Card` object stores account ID<br>• No need for multiple account selection screens<br>• ATM directly requests the account from server after authentication |

**Note:** Some ATMs support multiple accounts per card, but since requirement says single account, we follow that.

---

### R2: ATM Components

This requirement lists essential hardware components.

#### WHAT:

The ATM includes:
- `CardReader`
- `Keypad`
- `Screen`
- `CashDispenser`
- `Printer`
- `NetworkModule`

#### WHY:

These components drive the entire user interaction and transaction flow.

#### HOW in LLD:

Each becomes a separate class with responsibilities:

```java
CardReader.readCard()
Keypad.getPIN()
Screen.display()
CashDispenser.dispense(amount)
Printer.printReceipt()
NetworkModule.sendToBank()
```

This requirement ensures:
- ✅ Hardware abstraction
- ✅ Clean separation of concerns
- ✅ Easy simulation during coding

---

### R3: User Authentication (PIN Verification)

| Aspect | Details |
|--------|---------|
| **WHAT** | The system must verify the PIN entered by the user |
| **WHY** | Critical for security. Ensures only account owner gains access |
| **HOW in LLD** | • User inserts card → `ATM.authenticate()`<br>• ATM sends card number + PIN to Bank Server<br>• BankServer returns: SUCCESS / FAILURE<br>• After 3 failed attempts → card gets blocked (industry standard) |

This leads to:
- `AuthenticationService`
- PIN retry handling
- Security checks

---

### R4: Enable Transactions ONLY After Authentication

| Aspect | Details |
|--------|---------|
| **WHAT** | Transaction menu should appear only after authentication is successful |
| **WHY** | Prevents unauthorized access to account actions |
| **HOW in LLD** | • `ATMState` moves from Idle → `AuthenticatedState`<br>• Transaction selection is only enabled in `AuthenticatedState`<br>• This directly fits **State Pattern** |

---

### R5: Supported Account Types

| Aspect | Details |
|--------|---------|
| **WHAT** | User can have:<br>• Current (checking) account<br>• Savings account |
| **WHY** | These are the standard consumer account types in banks |
| **HOW in LLD** | • `User` object stores account type<br>• Different limits apply (e.g., daily withdrawal limits may differ)<br>• Both accounts support the same operations (except overdraft rules) |

---

### R6: Supported Operations

ATM must support:
- **Balance Inquiry**
- **Cash Withdrawal**
- **Funds Transfer**

#### WHAT:
Core functionalities of ATM operations.

#### WHY:
Basic banking tasks that must be accessible.

#### HOW in LLD:

You model a common `Transaction` interface:

```java
Transaction {
    execute()
}
```

Then create specialized classes:
- `BalanceInquiryTransaction`
- `WithdrawalTransaction`
- `FundsTransferTransaction`

This requirement is exactly why we use:
- **Strategy Pattern**
- **Factory Pattern**

**Shared transaction workflow:**

```
1. Validate authentication
2. Validate amount
3. Communicate with bank server
4. Update balances
5. Print receipt
6. Return to menu
```

---

### R7: Continue or End Session After Transaction

| Aspect | Details |
|--------|---------|
| **WHAT** | After a transaction, ATM must ask:<br>"Do you want another transaction?" or "End session & eject card" |
| **WHY** | Enhances user convenience and follows real ATM behavior |
| **HOW in LLD** | • ATM stays in `AuthenticatedState` if user chooses another operation<br>• Moves to `EjectCardState` if user ends session<br>• Card is returned before timeout<br>• Session object is reset |

**Sequence:**

```
Transaction completed 
  → Show menu 
  → If end session 
  → return card 
  → go to IdleState
```

This requirement supports:
- Session management
- State transitions
- Timeout handling (optional extension)

---

## Extra Requirements You Can Add (Very Useful in Interviews)

These are natural real-world requirements interviewers appreciate.

### R8: Daily Withdrawal Limit Enforcement

Banks impose daily withdrawal caps.

### R9: ATM Cash Inventory Management

ATM must ensure it can dispense notes & maintain denomination mix.

### R10: Error Handling

System must gracefully handle:
- Wrong PIN
- Network failure
- Card jam
- Cash jam
- Printer out of paper

### R11: Session Timeout

If user is idle for too long, eject card & reset.

---

## Summary (Clean and Interview-Friendly)

### WHAT?

A set of requirements (R1–R7) that define ATM core functionalities.

### WHY?

Clarifies the scope, avoids assumptions, and guides your class design.

### HOW?

Each requirement maps to:
- Hardware modules
- Software modules
- State transitions
- Transaction classes
- Authentication flows

---

## Requirements Mapping Table

| Requirement | Design Element | Pattern/Concept |
|-------------|----------------|-----------------|
| R1: Single Account | `Card` → `Account` mapping | Simple association |
| R2: Components | Hardware classes | Interface segregation |
| R3: Authentication | `AuthenticationService` | Security layer |
| R4: Post-Auth Transactions | State management | **State Pattern** |
| R5: Account Types | `Account` hierarchy | Inheritance/Polymorphism |
| R6: Operations | Transaction classes | **Strategy + Factory** |
| R7: Session Control | State transitions | **State Pattern** |

---

## Design Impact Checklist

When implementing these requirements:

- [ ] Create hardware abstraction classes
- [ ] Implement authentication flow with retry logic
- [ ] Design state machine for ATM states
- [ ] Create transaction hierarchy with common interface
- [ ] Add session management logic
- [ ] Handle account type differences
- [ ] Implement error handling for all edge cases

