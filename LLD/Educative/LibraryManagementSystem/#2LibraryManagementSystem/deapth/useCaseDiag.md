# Library Management System - Use Case Diagram Guide

Perfect — this is one of the most crucial parts of designing a Library Management System (LMS) because the use case diagram defines how every actor (user/system) interacts with the system. Let's break this entire section down in a structured, interview-ready manner with clear explanations and visuals.  

A Use Case Diagram shows who (actors) interacts with the system and what they do (use cases).
It’s like a map of how users use the system.
---

## 🧭 1. Overview

The **Library Management System (LMS)** manages books, members, and transactions such as issuing, returning, renewing, and reserving books.

The **Use Case Diagram** helps visualize the interactions between:

- **Actors** (who uses the system)
- **Use Cases** (what actions they perform)
- **Relationships** (how those use cases connect to each other)

---

## 🎭 2. Actors

### Primary Actors

| Actor | Description |
|-------|-------------|
| **Member** | Can search, reserve, borrow, renew, return books, pay fines, and cancel membership. |
| **Librarian** | Manages books, handles member accounts, and oversees transactions and fines. |

### Secondary Actor

| Actor | Description |
|-------|-------------|
| **System** | Sends automated notifications and calculates fines. |

---

## ⚙️ 3. Use Cases by Actor

### Member

- Login / Logout
- Register / Update Account
- Cancel Membership
- View Account
- Search Catalog
- Reserve Book
- Checkout Book
- Renew Book
- Return Book
- Remove Reservation
- Pay Fine

### Librarian

- Login / Logout
- Register / Update Account
- Cancel Membership
- View Account
- Issue Library Card
- Add / Edit / Remove Book
- Add / Edit / Remove Book Item
- Issue Book
- Renew Book
- Update Catalog
- Remove Reservation

### System

- Calculate Fine
- Send Overdue Notification
- Send Reservation Available Notification
- Send Reservation Canceled Notification

---

## 🔁 4. Relationships Between Use Cases

### 🧩 Include Relationships

These indicate mandatory dependencies (A always includes B).

| Primary Use Case | Includes | Description |
|------------------|----------|-------------|
| Add/Edit/Remove Book | Add/Edit/Remove Book Item | Managing book entries involves managing their copies. |
| Add/Edit/Remove Book Item | Update Catalog | Catalog must always stay consistent. |
| Register New Account | Issue Library Card | A library card must be issued when a new member registers. |
| Issue Book | Checkout Book | Borrowing a book involves completing checkout. |
| Checkout Book | Remove Reservation | If the book was reserved, remove it once checked out. |
| Return Book | Calculate Fine | Fine must be calculated upon return. |

### ✨ Extend Relationships

These represent optional or conditional actions.

| Base Use Case | Extends | Condition |
|---------------|---------|-----------|
| Return Book | Pay Fine | If the book is returned late. |
| Reserve Book | Send Reservation Available Notification | When the reserved book becomes available. |
| Remove Reservation | Send Reservation Canceled Notification | When reservation is canceled. |

---

## 🧱 5. Generalization Relationship

**"Search Catalog"** is a general use case that can be specialized into:

- Search by Title
- Search by Author
- Search by Subject
- Search by Publication Date

So, **Search Catalog** → generalizes → **Search by Title/Author/Subject/Date**

---

## 🧩 6. Use Case Diagram (Conceptual Representation)

Here's a textual form of how the diagram would look:

```
          ┌───────────────────────────────────────────────┐
          │           Library Management System           │
          │                                               │
          │   ┌──────────────┐         ┌──────────────┐   │
          │   │  Member      │         │  Librarian   │   │
          │   └─────┬────────┘         └─────┬────────┘   │
          │         │                        │            │
          │   [Search Catalog]     [Add/Edit/Remove Book] │
          │   [Reserve Book]       [Issue Book]           │
          │   [Checkout Book]      [Renew Book]           │
          │   [Return Book]        [Update Catalog]       │
          │   [Pay Fine]           [Cancel Membership]    │
          │         │                        │            │
          │         └────────────┬────────────┘           │
          │                      │                        │
          │              [Calculate Fine]                 │
          │              [Send Notification]              │
          └───────────────────────────────────────────────┘
```

---

## 📡 7. System Notification Flow

- When a book is **overdue**, the system automatically triggers → **Send Overdue Notification**.
- When a **reserved book becomes available**, → **Send Reservation Available Notification**.
- When a **reservation is canceled**, → **Send Reservation Canceled Notification**.

---

## ⚡ 8. Interview Summary Points

When describing the Use Case Diagram in an interview:

1. **Start with the Actors.** ("We have Member, Librarian, and the System.")
2. **List 2–3 major use cases per actor.**
3. **Describe key relationships** (Include vs Extend).
4. **Mention generalization** ("Search Catalog can be specialized.")
5. **Optionally mention state machine flow** for book lifecycle (AVAILABLE → RESERVED → ISSUED → RETURNED).

---

# Use Case Diagram Relationships - Complete Guide

The "relationships" part of a Use Case Diagram confuses many people at first, but once you visualize it step by step, it becomes very easy.

Let's go slowly and deeply — with real examples from your Library Management System (LMS).

---

## 🧭 Step 1: What are "relationships" in a Use Case Diagram?

In UML (Unified Modeling Language), relationships show how actors and use cases are connected or depend on each other.

There are four main types:

- **Association**
- **Include**
- **Extend**
- **Generalization**

We'll go one by one with examples from your LMS.

---

## 1️⃣ Association (→ "who does what")

### Definition:
A simple line connecting an actor and a use case.
It means **this actor can perform this action**.

### Example:

- A **Member** can borrow a book.
- A **Librarian** can add a new book.

### Diagram (text view):

```
Member --------> Borrow Book
Librarian -----> Add Book
```

### Meaning:
These are direct actions the actor performs.
So every time you draw a line between an actor and a use case, that's an **association relationship**.

---

## 2️⃣ Include (→ "must also do this")

### Definition:
**"Include"** means — every time one use case happens, another use case **must also occur** as part of it.
So it's like a **mandatory sub-task**.

You draw it with a dashed arrow labeled **«include»**.

### ✅ Example 1 — Registering a Member

When a librarian registers a new member, they **must also** issue a library card.
So "Issue Library Card" is a part of "Register Member".

```
Register Member  ---- «include» ---->  Issue Library Card
```

### ✅ Meaning:
You **can't** register a new member without issuing their card.

### ✅ Example 2 — Return Book includes Calculate Fine

When a member returns a book, the system **must always** check if it's late → that means calculating the fine is a mandatory step.

```
Return Book  ---- «include» ---->  Calculate Fine
```

```java
public void returnBook(BookItem bookItem, Member member) {
    calculateFine(bookItem, member);
    updateInventory(bookItem);
}
```
✅ Include = Mandatory sub-process

### ✅ Meaning:
Every time you "Return Book", "Calculate Fine" happens automatically.

---

## 3️⃣ Extend (→ "sometimes, if condition true")

### Definition:
**"Extend"** means — a use case happens **only under special conditions**.
So it's an **optional or conditional sub-task**.

You draw it with a dashed arrow labeled **«extend»** (the arrow goes from the optional case → to the main case).

### ⚡ Example 1 — Pay Fine extends Return Book

If a book is returned late, the member must pay a fine.
But **not always** — only if it's overdue.

```
Pay Fine  ---- «extend» ---->  Return Book
```

```java
public void returnBook(BookItem bookItem, Member member) {
    if (bookItem.isLate()) {
        payFine(member);
    }
}
```
✅ Extend = Optional or conditional sub-process

### ✅ Meaning:
"Pay Fine" **extends** "Return Book" — it only triggers when the book is returned after due date.

### ⚡ Example 2 — Send Notification extends Reserve Book

If a reserved book becomes available → the system sends a notification.

```
Send Reservation Available Notification  ---- «extend» ---->  Reserve Book
```

### ✅ Meaning:
Notification happens **only after** the reserved book becomes available — it's conditional.

---

## 4️⃣ Generalization (→ "is-a relationship")

### Definition:
Used when one use case is a **specialized version** of another.
It's like **inheritance** in OOP.

You draw a solid line with a hollow triangle arrowhead (pointing to the parent).

### 📘 Example — Search Catalog generalization

The main use case is **"Search Catalog"**,
but it can be done in several specific ways:

```
Search by Title  ─┐
Search by Author ├──▷ Search Catalog
Search by Subject┘
```

```java 
abstract class SearchCatalog {
    abstract List<Book> search(String query);
}

class SearchByTitle extends SearchCatalog { ... }
class SearchByAuthor extends SearchCatalog { ... }
```
✅ Generalization = Reusable behavior inheritance


### ✅ Meaning:
All are specialized versions of "Search Catalog".
Each one behaves slightly differently but belongs to the same group.

---

## 🧩 Summary Table

| Type | Description | Example | Always Happens? |
|------|-------------|---------|-----------------|
| **Association** | Actor performs a use case | Member → Borrow Book | Yes |
| **Include** | One use case always includes another | Return Book → includes → Calculate Fine | Always |
| **Extend** | Optional use case triggered by condition | Pay Fine → extends → Return Book | Conditional |
| **Generalization** | Specialized version of a use case | Search by Title → Search Catalog | Depends on user action |

---

## 🧠 Visual Summary (in words)

```
Member ---> Return Book
Return Book -- «include» --> Calculate Fine
Pay Fine -- «extend» --> Return Book

Librarian ---> Register Member
Register Member -- «include» --> Issue Library Card

Search by Title
Search by Author
Search by Subject
     \_________  Generalization
         ↓
     Search Catalog
```
