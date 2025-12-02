# LLD Chapter: ATM System — Expectations From the Interviewee

In an ATM system LLD interview, the real test is **how you think**, not how many classes you write.

The interviewer expects you to ask smart clarifying questions that reduce ambiguity and prove that you understand real-world constraints.

---

## 1. ATM Components — (WHAT / WHY / HOW)

### WHAT is being asked?
Questions about the physical and logical components of an ATM.

### WHY ask these questions?
Because every ATM design depends on its hardware and capabilities.
> Example: An ATM with a deposit slot needs more logic than one without.

### HOW does it matter in LLD?
Each component becomes a class or interface in your design.
> e.g., `CashDispenser`, `CardReader`, `Keypad`, etc.

### Questions & Answers

#### 1. What are the components of an ATM?

| Aspect | Details |
|--------|---------|
| **WHAT** | Card reader, keypad, screen, cash dispenser, cash acceptor, receipt printer, network module |
| **WHY** | To understand hardware-software interaction |
| **HOW** | Each is modeled as a separate class with high cohesion and clear responsibility |

#### 2. Is the ATM necessarily placed inside a room?

| Aspect | Details |
|--------|---------|
| **WHAT** | No. It can be standalone or inside a lobby |
| **WHY** | Environment does not affect core LLD – but it affects security (CCTV, guards, card skimmer prevention) |
| **HOW** | No additional classes needed, but helps in assumptions |

#### 3. Does an ATM have a fingerprint scanner?

| Aspect | Details |
|--------|---------|
| **WHAT** | Most ATMs do not; some modern ones do |
| **WHY** | With biometrics, your authentication module changes |
| **HOW** | If biometric → add `BiometricScanner` class and modify authentication flow |

---

## 2. ATM Features — (WHAT / WHY / HOW)

### WHAT is this section about?
Understanding which operations the ATM supports.

### WHY is this important?
Features directly map to Transaction classes in LLD.

### HOW is it used in design?
You create subclasses: `Withdrawal`, `Deposit`, `Transfer`, `BalanceInquiry`.

### Questions & Answers

#### 1. What is the withdrawal limit of an ATM?

| Aspect | Details |
|--------|---------|
| **WHAT** | Limits vary by bank and by ATM machine |
| **WHY** | LLD must enforce daily transaction limits and per-withdrawal limits |
| **HOW** | • Store limits in ATM or Bank Server<br>• Validate before transaction approval |

#### 2. Can we check our account balance using an ATM?

| Aspect | Details |
|--------|---------|
| **WHAT** | Yes |
| **WHY** | Determines if you need a `BalanceInquiry` transaction |
| **HOW** | ATM → BankServer → account balance → display on screen |

#### 3. Can we set a PIN using an ATM?

| Aspect | Details |
|--------|---------|
| **WHAT** | Depends on bank policy |
| **WHY** | If yes, add `PINChange` transaction |
| **HOW** | User authenticates → enters old & new PIN → bank updates PIN |

---

## 3. ATM Processing — (WHAT / WHY / HOW)

### WHAT is this section?
Understanding transaction behavior and edge cases.

### WHY does this matter?
Edge cases show you understand real banking workflows.

### HOW is it handled in LLD?
Each case must be validated either by ATM or the bank server.

### Questions & Answers

#### 1. What happens when the amount the user enters for withdrawal exceeds the user's account balance?

| Aspect | Details |
|--------|---------|
| **WHAT** | Insufficient funds |
| **WHY** | Bank enforces this rule |
| **HOW** | ATM sends request → Bank checks → Bank responds with FAILURE → ATM shows message |

#### 2. What happens when the amount the user enters exceeds the ATM's cash limit?

| Aspect | Details |
|--------|---------|
| **WHAT** | ATM cannot dispense more than available bills |
| **WHY** | Hardware constraint of cash dispenser |
| **HOW** | ATM checks local cash inventory → returns error → no request sent to bank |

#### 3. What happens when the amount the user enters exceeds the total cash in the ATM?

| Aspect | Details |
|--------|---------|
| **WHAT** | ATM-level insufficient funds |
| **WHY** | Technical limitation |
| **HOW** | `CashManager` checks denominations → shows error → aborts transaction |

#### 4. Can the ATM be used for online transactions?

| Aspect | Details |
|--------|---------|
| **WHAT** | No. ATM is for physical banking operations only |
| **WHY** | Online transactions require card-not-present flows (OTP, CVV, 3DS) |
| **HOW** | ATM is connected to bank network only for ATM operations |

---

## 4. Additional Smart Questions (You can ask in interview)

### ✔ Does the ATM support multi-currency withdrawal?
**Impact:** If yes → need exchange rate, FX fees, currency inventory.

### ✔ Does ATM store logs locally or send them to bank in real-time?
**Impact:** Design impacts: `Logger` module, audit trail class.

### ✔ What happens if the network fails mid-transaction?
**Impact:** Need rollback handling + transaction states.

### ✔ How many denominations does the ATM support?
**Impact:** Impacts the `CashDispenser` algorithm.

---

## 5. Summary (Simple & Interview-Friendly)

### WHAT?
List of questions to clarify ATM components, features, and processing rules.

### WHY?
To avoid assumptions and build a correct LLD.

### HOW?
Answers determine:
- Class responsibilities
- Transaction flows
- Error handling logic
- Hardware-software interactions

---

## Key Takeaways

| Category | Key Points |
|----------|------------|
| **Components** | Each hardware component = separate class |
| **Features** | Each operation = Transaction subclass |
| **Processing** | Edge cases = validation logic in ATM or BankServer |
| **Smart Questions** | Show understanding of real-world constraints |

