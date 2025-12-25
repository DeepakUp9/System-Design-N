# Activity Diagram

> **Study Notes for UML & Process Modeling**

---

## 📖 Definition

An **activity diagram** shows the flow of actions (activities) in a system, similar to a flowchart.

👉 **It represents the dynamic behavior of a system.**

---

## 🎯 Purpose of Activity Diagram

- ✅ Shows step-by-step flow of actions
- ✅ Explains business logic
- ✅ Captures decision making
- ✅ Supports parallel and conditional flows

---

## ⭐ Key Features

**Focuses on activities, not objects**

**Can show:**
- Sequential flow
- Parallel flow
- Conditional flow
- Concurrent flow

---

## 🧩 Main Components

### 1. Initial Node 🟢

**Purpose:** Starting point of the activity

**Symbol:** Filled circle `●`

```
  ●
  │
  ↓
```

---

### 2. Action 📦

**Purpose:** Represents a task or operation

**Symbol:** Rounded rectangle

```
┌─────────────────┐
│  Action Name    │
└─────────────────┘
```

---

### 3. Flow Final 🔚

**Purpose:** Ends one path of the flow

**Behavior:** Other paths may continue

**Symbol:** Circle with X inside `⊗`

```
  │
  ↓
  ⊗
```

---

### 4. Activity Final ⏹️

**Purpose:** Ends entire activity diagram

**Behavior:** No further actions execute

**Symbol:** Filled circle inside another circle `◉`

```
  │
  ↓
  ◉
```

---

### 5. Control Flow ➡️

**Purpose:** Shows direction of execution

**Symbol:** Arrows

```
Action 1 ───▶ Action 2
```

---

### 6. Object Flow 📄

**Purpose:** Shows how data/objects move between activities

**Symbol:** Arrow with object label

```
Action 1 ───▶ [Data] ───▶ Action 2
```

---

### 7. Decision Node 💎

**Purpose:** Represents a condition

**Symbol:** Diamond `◇`

**Behavior:** One input → multiple outputs

```
      │
      ↓
      ◇
     ╱ ╲
    ╱   ╲
[Yes]   [No]
```

---

### 8. Merge Node 🔀

**Purpose:** Combines multiple paths into one

**Symbol:** Same as decision (diamond) `◇`

```
  [Path 1]  [Path 2]
      ╲       ╱
       ╲     ╱
         ◇
         │
         ↓
```

---

### 9. Fork and Join 🍴

#### Fork (Split)
**Purpose:** Splits one flow into parallel flows

**Symbol:** Thick horizontal/vertical bar

```
      │
      ↓
  ═══════
   ║   ║
   ↓   ↓
```

#### Join (Merge)
**Purpose:** Combines parallel flows into one

**Symbol:** Thick horizontal/vertical bar

```
   ║   ║
   ↓   ↓
  ═══════
      │
      ↓
```

---

## 📐 How to Draw an Activity Diagram

### Step 1: Identify Actions

**List all activities in the use case**

**Example:** ATM cash withdrawal

---

### Step 2: Identify Actors & Objects

- Customer
- ATM
- Account
- Transaction
- Cash Dispenser

---

### Step 3: Define Flow

- Decide order of actions
- Identify conditions and parallel steps

---

### Step 4: Draw Diagram

1. Start with initial node `●`
2. Connect actions with control flow
3. End with final node `◉`

---

## 🆚 Sequence Diagram vs Activity Diagram

| Sequence Diagram | Activity Diagram |
|------------------|------------------|
| Object-based | Activity-based |
| Shows message order | Shows action flow |
| Focuses on interactions | Focuses on workflow |
| For single use case | Can cover multiple flows |
| Time-oriented | Process-oriented |

---

## 💡 ATM Example (Simple Flow)

### Text Flow:
1. Start
2. Enter amount
3. Check balance
4. If sufficient → dispense cash
5. Else → show error
6. End

### Visual Diagram:

```
           ●  Start
           │
           ↓
    ┌──────────────┐
    │ Enter Amount │
    └──────────────┘
           │
           ↓
    ┌──────────────┐
    │Check Balance │
    └──────────────┘
           │
           ↓
           ◇  Decision
          ╱ ╲
         ╱   ╲
    [Yes]     [No]
      ↓         ↓
┌───────────┐ ┌───────────┐
│ Dispense  │ │Show Error │
│   Cash    │ └───────────┘
└───────────┘      │
      │            │
      ↓            ↓
      └─────◇──────┘ Merge
            │
            ↓
      ┌──────────┐
      │  Return  │
      │   Card   │
      └──────────┘
            │
            ↓
            ◉  End
```

---

## 🔄 Complete ATM Example with Swimlanes

```
┌─────────────────────────────────────────────────────┐
│                    Customer                          │
├─────────────────────────────────────────────────────┤
│     ●                                                │
│     │                                                │
│     ↓                                                │
│ ┌─────────────┐                                     │
│ │Insert Card  │                                     │
│ └─────────────┘                                     │
│     │                                                │
├─────┼────────────────────────────────────────────────┤
│     │                 ATM                            │
├─────┼────────────────────────────────────────────────┤
│     ↓                                                │
│ ┌─────────────┐                                     │
│ │Request PIN  │                                     │
│ └─────────────┘                                     │
│     │                                                │
├─────┼────────────────────────────────────────────────┤
│     │                Customer                        │
├─────┼────────────────────────────────────────────────┤
│     ↓                                                │
│ ┌─────────────┐                                     │
│ │ Enter PIN   │                                     │
│ └─────────────┘                                     │
│     │                                                │
├─────┼────────────────────────────────────────────────┤
│     │                 System                         │
├─────┼────────────────────────────────────────────────┤
│     ↓                                                │
│ ┌─────────────┐                                     │
│ │Verify PIN   │                                     │
│ └─────────────┘                                     │
│     │                                                │
│     ↓                                                │
│     ◇ Valid?                                         │
│    ╱ ╲                                               │
│[No]   [Yes]                                          │
│  │      │                                            │
│  │      ↓                                            │
│  │  ┌─────────────┐                                 │
│  │  │Check Balance│                                 │
│  │  └─────────────┘                                 │
│  │      │                                            │
│  │      ↓                                            │
│  │      ◇ Sufficient?                               │
│  │     ╱ ╲                                           │
│  │ [Yes] [No]                                        │
│  │   │     │                                         │
│  │   │     ↓                                         │
│  │   │ ┌─────────────┐                              │
│  │   │ │ Show Error  │                              │
│  │   │ └─────────────┘                              │
│  │   │     │                                         │
│  │   ↓     │                                         │
│  │ ┌─────────────┐  │                               │
│  │ │  Dispense   │  │                               │
│  │ │    Cash     │  │                               │
│  │ └─────────────┘  │                               │
│  │   │              │                               │
│  └───┴──────────────┘                               │
│      │                                               │
│      ↓                                               │
│  ┌─────────────┐                                    │
│  │Return Card  │                                    │
│  └─────────────┘                                    │
│      │                                               │
│      ↓                                               │
│      ◉  End                                          │
└─────────────────────────────────────────────────────┘
```

---

## 🔀 Parallel Flow Example: Online Order

```
           ●  Start
           │
           ↓
    ┌──────────────┐
    │ Place Order  │
    └──────────────┘
           │
           ↓
       ═══════  Fork
        ║   ║
        ║   ║
        ↓   ↓
┌───────────┐ ┌───────────┐
│  Process  │ │   Send    │
│  Payment  │ │Confirmation│
└───────────┘ └───────────┘
        ║   ║
        ↓   ↓
       ═══════  Join
           │
           ↓
    ┌──────────────┐
    │ Ship Product │
    └──────────────┘
           │
           ↓
           ◉  End
```

---

## 🎯 One-Line Summary

> **An activity diagram shows how actions flow from start to end, including decisions and parallel steps.**

---

## 📚 Symbol Quick Reference

| Symbol | Name | Purpose |
|--------|------|---------|
| `●` | Initial Node | Start point |
| `◉` | Activity Final | End entire activity |
| `⊗` | Flow Final | End one path |
| `┌───┐` | Action | Task/operation |
| `◇` | Decision/Merge | Condition or merge |
| `═══` | Fork/Join | Split/merge parallel flows |
| `───▶` | Control Flow | Direction of execution |
| `[Object]` | Object Node | Data/object in flow |

---

## 💡 Interview Tips

### Common Questions:

**1. What's the difference between activity and flowchart?**
- Activity diagrams are UML standard and more formal
- Support swimlanes, concurrent flows, and object flows
- Flowcharts are simpler, general-purpose

**2. When to use fork vs decision?**
- **Fork:** Parallel execution (both happen simultaneously)
- **Decision:** Conditional execution (one path chosen)

**3. What are swimlanes?**
- Vertical/horizontal partitions showing which actor/system performs which action
- Helps organize complex diagrams

**4. Flow Final vs Activity Final?**
- **Flow Final (`⊗`):** Ends one branch, others continue
- **Activity Final (`◉`):** Ends entire diagram

### Best Practices:
- ✅ Use swimlanes for multi-actor processes
- ✅ Keep actions concise and clear
- ✅ Show parallel flows with fork/join
- ✅ Use decision nodes for conditional logic
- ✅ Don't mix with sequence diagram concepts
- ✅ Focus on workflow, not object interactions

---

## 🎓 When to Use Activity Diagrams

**Good for:**
- ✅ Business process modeling
- ✅ Workflow systems
- ✅ Algorithm visualization
- ✅ Parallel processing
- ✅ Use case scenarios

**Not ideal for:**
- ❌ Object interactions (use sequence diagram)
- ❌ System structure (use class diagram)
- ❌ User interface flow (use state diagram)

---
