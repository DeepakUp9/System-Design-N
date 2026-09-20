# UML Class Diagram — Short Notes

**Purpose:** Represents a system's **static structure** — the only UML diagram that maps directly to OOP code. Used for both forward and reverse engineering.

**Why use it:**
- Shows system's static structure & responsibilities
- Directly maps to OOP languages
- Forward engineering (design → code) and reverse engineering (code → design)

---

## Class Notation

A class = rectangle, 3 sections:
```
┌─────────────┐
│    Movie    │   ← class name
├─────────────┤
│ - title     │   ← attributes
│ - duration  │
├─────────────┤
│ + play()    │   ← methods
└─────────────┘
```

**Abstract classes, interfaces, enums:**
- Abstract class → name in *italics*
- Use `<<interface>>`, `<<enumeration>>`, `<<annotation>>` stereotype labels

---

## Access Modifiers (symbols)

| Symbol | Meaning |
|:-:|---|
| `+` | Public — visible everywhere |
| `-` | Private — class only |
| `#` | Protected — class + subclasses |

---

## Association (relationships between classes)

Two categories: **Class association** (inheritance) and **Object association**.

### Class association — Inheritance
Solid line, **hollow arrowhead**, points from child → parent.

### Object association
| Type | Symbol | Strength |
|---|---|---|
| **Simple association** | plain line | Weakest — just a reference |
| **Aggregation** | line + **unfilled diamond** at container | Weak — parts can exist independently |
| **Composition** | line + **filled diamond** at composer | Strong — parts die with the whole |

**Aggregation example:** `Department ◇— Employee` (employee survives if department is dissolved)
**Composition example:** `Chair ◆— Arm/Seat/Leg` (parts have no meaning without the Chair)

---

## Navigation & Arity

- **One-way association** — arrow points to the "server" object
- **Two-way association** — plain line, no arrow, navigable both directions
- **Binary** — 2 classes involved
- **Ternary** — 3 classes, shown via a diamond connecting all three
- **N-ary** — more than 3 classes

---

## Dependency

One class depends on another for its implementation (e.g., passed as a method parameter), but not necessarily vice-versa.
Denoted by a **dashed arrow**.

Example: `RegistrationManager ┄┄> Student` (Student object passed into a RegistrationManager method)

---

## Quick Recap

| Relationship | Line style | Meaning |
|---|---|---|
| Inheritance | solid + hollow triangle arrowhead | IS-A |
| Aggregation | solid + unfilled diamond | HAS-A (weak, independent lifecycle) |
| Composition | solid + filled diamond | HAS-A (strong, dependent lifecycle) |
| Simple association | plain solid line | Weak reference |
| Dependency | dashed arrow | Uses-a (temporary reliance) |