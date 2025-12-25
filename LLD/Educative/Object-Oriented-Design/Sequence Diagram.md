# Sequence Diagram

> **Study Notes for UML & Dynamic System Modeling**

---

## 📖 Definition

A **sequence diagram** shows how actors and objects interact with each other step by step over time using messages.

👉 **It explains the order of events in a use case.**

---

## 🎯 Purpose of Sequence Diagram

- ✅ Shows interaction flow
- ✅ Explains logic of operations
- ✅ Helps understand runtime behavior
- ✅ Focuses on message sequence

---

## 🧩 Main Elements

### 1. Lifeline 📍

**Definition:** Represents an object or actor

**Notation:** Dotted vertical line

**Purpose:** Shows the lifetime of an entity

**Layout:** Entities are placed horizontally

```
Actor/Object
     │
     │ (dotted line)
     │
     ↓
```

---

### 2. Activation Bar ▮

**Definition:** Shows when an object is active

**Notation:** Thin vertical rectangle on lifeline

**Purpose:** Indicates message processing time

```
     │
     ▮  ← Object is active here
     │
```

---

### 3. Messages 📨

**Definition:** Messages show communication between objects.

---

## 📬 Types of Messages

| Message Type | Meaning | Notation |
|--------------|---------|----------|
| **Synchronous** | Sender waits for response | Solid line, filled arrow `────▶` |
| **Asynchronous** | Sender does not wait | Solid line, open arrow `────▷` |
| **Return** | Response to sync call | Dotted line `- - - -▶` |
| **Create** | Creates a new object | Arrow to object `────▶ Object` |
| **Destroy** | Destroys an object | Lifeline ends with `✕` |
| **Lost** | Message not received | Arrow ending with `○` |
| **Found** | Sender unknown | Arrow starting with `○` |

---

## 📐 Message Notations Explained

### Synchronous Message
```
Sender ────▶ Receiver
       (waits for response)
```

### Asynchronous Message
```
Sender ────▷ Receiver
       (doesn't wait)
```

### Return Message
```
Sender ◀- - - - Receiver
       (response)
```

### Create Message
```
Creator ────▶ ┌───────┐
              │ New   │
              │Object │
              └───────┘
```

### Destroy Message
```
     │
     ▮
     │
     ✕  (object destroyed)
```

---

## 🎨 How to Draw a Sequence Diagram

### Step 1: Identify Use Case
**Example:** ATM cash withdrawal

---

### Step 2: Identify Actors & Objects
- Customer (Actor)
- ATM (Object)
- Transaction (Object)
- Account (Object)
- Cash Dispenser (Object)

---

### Step 3: Identify Order of Actions

1. Customer requests withdrawal
2. ATM creates transaction
3. Account verifies balance
4. ATM requests cash
5. Cash is dispensed
6. Customer collects cash

---

### Step 4: Draw the Diagram

**Rules:**
- Place actors/objects horizontally
- Draw lifelines vertically
- Add messages in time order (top to bottom)

**Example Structure:**
```
Customer    ATM    Transaction    Account    CashDispenser
   │         │          │            │             │
   ├────▶    │          │            │             │  1. Request withdrawal
   │         ├─────▶    │            │             │  2. Create transaction
   │         │          ├───────▶    │             │  3. Verify balance
   │         │          ◀────────    │             │  4. Balance OK
   │         ├──────────────────────▶│             │  5. Request cash
   │         │          │            │────────▶    │  6. Dispense
   │◀────────┼──────────┼────────────┼─────────    │  7. Collect cash
```

---

## 🔀 Sequence Fragment (Advanced Flow)

**Purpose:** Used to show conditions, loops, and alternatives.

### Common Fragment Operators

| Operator | Purpose |
|----------|---------|
| **alt** | If–else condition |
| **opt** | If condition (optional) |
| **loop** | Repeated actions |
| **ref** | Reuse another sequence diagram |
| **par** | Parallel execution |
| **break** | Break from loop |

---

## 🔄 Fragment Examples

### Alt (Alternative) - If-Else

```
┌─ alt ────────────────────────────┐
│                                   │
│ [balance >= amount]               │
│   ATM ────▶ CashDispenser         │
│   (Dispense cash)                 │
│                                   │
│ ─────────────────────────────────│
│                                   │
│ [else]                            │
│   ATM ────▶ Customer              │
│   (Show error)                    │
│                                   │
└───────────────────────────────────┘
```

---

### Opt (Optional)

```
┌─ opt ────────────────────────────┐
│                                   │
│ [receipt requested]               │
│   ATM ────▶ Printer               │
│   (Print receipt)                 │
│                                   │
└───────────────────────────────────┘
```

---

### Loop

```
┌─ loop(3 times) ──────────────────┐
│                                   │
│   ATM ────▶ Customer              │
│   (Enter PIN)                     │
│                                   │
└───────────────────────────────────┘
```

---

### Ref (Reference)

```
┌─ ref ────────────────────────────┐
│                                   │
│   Authentication Process          │
│                                   │
└───────────────────────────────────┘
```

---

## ✅ Key Points to Remember

- ⏰ **Time flows top to bottom**
- ➡️ **Messages are horizontal**
- 🚫 **Lifelines never overlap**
- 📦 **Use fragments to avoid clutter**
- 🎯 **Focus on message sequence, not implementation**

---

## 🎯 One-Line Summary

> **A sequence diagram shows who interacts with whom and in what order during a use case.**

---

## 💡 Complete Example: ATM Cash Withdrawal

```
Customer    ATM         Transaction    Account    CashDispenser
   │         │               │            │             │
   │ 1. Insert Card          │            │             │
   ├────▶    │               │            │             │
   │         │               │            │             │
   │ 2. Enter PIN            │            │             │
   ├────▶    │               │            │             │
   │         │               │            │             │
   │         │ 3. Create     │            │             │
   │         ├──────▶        │            │             │
   │         │               │            │             │
   │ 4. Request Amount       │            │             │
   ├────▶    │               │            │             │
   │         │               │            │             │
   │         │ 5. Check Balance           │             │
   │         ├───────────────┼───────▶    │             │
   │         │               │            │             │
   │         │ 6. Balance Verified        │             │
   │         │◀──────────────┼────────    │             │
   │         │               │            │             │
┌──┼─────────┼───────alt─────┼────────────┼─────────────┤
│  │         │               │            │             │
│  │ [balance >= amount]     │            │             │
│  │         │               │            │             │
│  │         │ 7. Dispense Request        │             │
│  │         ├───────────────┼────────────┼────────▶    │
│  │         │               │            │             │
│  │         │ 8. Cash Dispensed          │             │
│  │◀────────┼───────────────┼────────────┼─────────    │
│  │         │               │            │             │
├──┼─────────┼───────────────┼────────────┼─────────────┤
│  │         │               │            │             │
│  │ [else]                  │            │             │
│  │         │               │            │             │
│  │         │ 9. Show Error │            │             │
│  │◀────    │               │            │             │
│  │         │               │            │             │
└──┼─────────┼───────────────┼────────────┼─────────────┘
   │         │               │            │             │
   │         │ 10. Destroy   │            │             │
   │         ├──────▶        ✕            │             │
   │         │               │            │             │
```

---

## 🎓 Interview Tips

### Common Questions:

**1. What's the difference between sequence and collaboration diagrams?**
- **Sequence:** Focus on time ordering (vertical flow)
- **Collaboration:** Focus on object relationships (spatial layout)

**2. When to use synchronous vs asynchronous messages?**
- **Synchronous:** When caller needs immediate response (API call)
- **Asynchronous:** When caller continues without waiting (email send)

**3. What is an activation bar?**
- Shows when an object is processing a message
- Appears as rectangle on lifeline

### Best Practices:
- ✅ Keep diagrams simple and focused
- ✅ Show only relevant interactions
- ✅ Use fragments for complex logic
- ✅ Number messages for clarity
- ✅ Use meaningful object names
- ✅ Don't show too many objects (5-7 max)

---

## 📊 Quick Reference Table

| Element | Symbol | Purpose |
|---------|--------|---------|
| Lifeline | `│` (dotted) | Object existence |
| Activation | `▮` | Object active |
| Sync message | `────▶` | Call with wait |
| Async message | `────▷` | Call without wait |
| Return | `- - -▶` | Response |
| Create | `────▶ [new]` | Object creation |
| Destroy | `✕` | Object deletion |

---
