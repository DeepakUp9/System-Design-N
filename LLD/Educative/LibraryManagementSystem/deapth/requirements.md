# Library Management System - Requirements

## Requirement Collection

For LMS (Library Management System), the requirements have been defined below:

### Data Management Requirements

**R1:** The system should store information about books and members, and maintain a complete log of all book borrowing, return, reservation, and renewal transactions.  
**R2:** Every book must have a unique identification number and detailed information, including rack/location in the library.  
**R3:** Each book should include ISBN, title, author name, subject, and publication date.  
**R4:** A book can have multiple copies; each physical copy is a distinct book item with a unique ID.

---

### User Management Requirements

**R5:** The system must support two types of users: librarian and member, each with a library card and unique card number.  
**R6:** Every user must have a library card with a unique card number.

---

### Borrowing & Lending Requirements

**R7:** Members can borrow a maximum of 10 books at a time.  
**R8:** A member can borrow a book for a maximum of 15 days.  
**R9:** Only one member can reserve each book item at a time.  
**R10:** The system must record who issued, reserved, or renewed a book item and on which date.  
**R11:** The system must allow members to renew borrowed books, according to policy limits. 

---

### Notification & Reservation Requirements

**R12:** The system should notify members if a book is not returned by the due date or when a reserved book becomes available.  
**R13:** If a book is unavailable, a member should be able to reserve it for when it becomes available.

---

### Search Requirements

**R14:** The system should allow users to search for books by title, author, subject, or publication date.

---

## Requirements Summary Table

| ID | Category | Requirement |
|----|----------|-------------|
| R1 | Data Management | Store books, members, and transaction logs |
| R2 | Data Management | Unique book ID with rack/location info |
| R3 | Data Management | Book details: ISBN, title, author, subject, publication date |
| R4 | Data Management | Multiple copies per book with unique IDs |
| R5 | User Management | Two user types: librarian and member |
| R6 | User Management | Unique library card for every user |
| R7 | Borrowing | Max 10 books per member |
| R8 | Borrowing | Max 15 days borrowing period |
| R9 | Reservation | One member per book item reservation |
| R10 | Tracking | Record all transactions with user and date |
| R11 | Renewal | Support book renewal per policy |
| R12 | Notification | Alert for overdue books and available reservations |
| R13 | Reservation | Reserve unavailable books |
| R14 | Search | Search by title, author, subject, or publication date |


---

## 🧭 1. Why "Requirement Gathering" matters in LLD

Before writing even one line of code or class diagram, you must clarify what exactly the system must do.

💡 In an interview, this is your first step to:

- **Set scope** — What features you'll design (and what you'll skip)
- **Identify entities** — Books, Members, Librarians, Transactions
- **Clarify constraints** — Like borrowing limits or unique IDs
- **Plan architecture** — Which modules and services to create

If you skip requirement clarification, you might end up designing unnecessary or incomplete components.

---

## 🧩 2. Breaking down and grouping requirements

Let's take each requirement (R1–R14) and understand what part of the system it influences — with explanation and LLD mapping.

---

### 📘 A. Core Data Requirements (R1–R4)

| ID | Requirement | Explanation | LLD Implication |
|----|-------------|-------------|-----------------|
| R1 | Store information about books, members, and maintain a complete log of all book transactions. | Core persistence layer — must track book, member, issue, return, renew, reserve. | Design entities: Book, Member, Loan, Reservation, Fine. |
| R2 | Every book must have a unique ID and a physical location. | Physical inventory tracking (for library racks). | Add fields: bookId, rackNumber, shelfLocation. |
| R3 | Each book has metadata: ISBN, title, author, subject, publication date. | Logical information about books for search & indexing. | Fields in Book class: isbn, title, author, subject, publishedDate. |
| R4 | A book can have multiple copies (distinct items). | Each copy must be uniquely identifiable even if same title. | Create BookItem entity — maps one-to-many with Book. |

**Example entity model:**
```
Book (1) ─── (M) BookItem
```

---

### 👥 B. User and Role Requirements (R5–R6)

| ID | Requirement | Explanation | LLD Implication |
|----|-------------|-------------|-----------------|
| R5 | Support two user types: Librarian and Member. | Different roles = different permissions. | Create User base class, subclasses Librarian and Member. |
| R6 | Each user must have a unique library card number. | Identification and validation for transactions. | Add libraryCardNumber field in Member. |

**Example class hierarchy:**
```
User
 ├── Librarian
 └── Member
```

---

### 📚 C. Borrowing & Policy Rules (R7–R9)

| ID | Requirement | Explanation | LLD Implication |
|----|-------------|-------------|-----------------|
| R7 | A member can borrow a maximum of 10 books. | Prevent misuse; rule enforcement. | Validation logic before issuing book. |
| R8 | Each book can be borrowed for a max of 15 days. | Policy rule for due date generation. | Loan.issueDate + 15 days = dueDate. |
| R9 | Only one member can reserve a specific book item at a time. | Prevent multiple holds for same copy. | Reservation table must enforce 1 active reservation per book item. |

---

### 🕓 D. Transaction & Logging (R10–R11)

| ID | Requirement | Explanation | LLD Implication |
|----|-------------|-------------|-----------------|
| R10 | Record who issued/reserved/renewed and when. | Audit trail. | Add createdBy, createdDate fields in Loan, Reservation, etc. |
| R11 | Allow renewals within policy limits. | Extend due date, subject to constraints. | Use renewalCount in Loan. |

---

### 🔔 E. Notifications & Availability (R12–R13)

| ID | Requirement | Explanation | LLD Implication |
|----|-------------|-------------|-----------------|
| R12 | Notify members for due-date or reserved-book availability. | Background event processing (Kafka, async event). | Create NotificationService. |
| R13 | Allow member to reserve unavailable books. | Reservation queue mechanism. | Reservation entity + logic to notify next in queue. |

---

### 🔍 F. Search & Retrieval (R14)

| ID | Requirement | Explanation | LLD Implication |
|----|-------------|-------------|-----------------|
| R14 | Allow users to search by title, author, subject, publication date. | Multiple search filters. | Implement SearchService with indexes or full-text search. |

---

## 🧱 3. Mapping Requirements → System Components

| Module | Handles Requirements | Description |
|--------|---------------------|-------------|
| Book Service | R1–R4 | Handles CRUD for books, book items, and metadata. |
| User Service | R5–R6 | Manages members and librarians. |
| Borrowing Service | R7–R8 | Issues and returns books. |
| Reservation Service | R9, R13 | Handles book reservation and queue. |
| Renewal Service | R11 | Handles renewal requests. |
| Fine Service | (implicit) | Calculates fines on late return. |
| Notification Service | R12 | Sends reminders and availability alerts. |
| Search Service | R14 | Provides flexible search options. |

---

## ⚙️ 4. Class Diagram (Simplified View)

```
+-------------------+
|       Book        |
+-------------------+
| isbn              |
| title             |
| author            |
| subject           |
| publishedDate     |
+-------------------+
          |
          | 1..*
          v
+-------------------+
|    BookItem       |
+-------------------+
| bookItemId        |
| rackNumber        |
| status (enum)     |
+-------------------+

+-------------------+          +-------------------+
|      Member       |          |    Librarian      |
+-------------------+          +-------------------+
| cardNumber        |          | employeeId        |
| maxBooksAllowed=10|          | canAddBooks()     |
+-------------------+          +-------------------+

+-------------------+
|       Loan        |
+-------------------+
| issueDate         |
| dueDate           |
| renewalCount      |
| returnDate        |
+-------------------+

+-------------------+
|   Reservation     |
+-------------------+
| reserveDate       |
| status            |
+-------------------+

+-------------------+
|       Fine        |
+-------------------+
| amount            |
| paid              |
+-------------------+
```

---

## 🧠 5. How to explain this in an interview

Here's a short spoken-style answer you can give:

> "Before designing the LMS, I first gathered the functional requirements.
> From R1 to R4, I understood I need entities for Book and BookItem to store both logical and physical data.
> R5 and R6 define user roles — Librarian and Member — each with unique identification.
> R7–R9 establish business rules like borrowing limits and reservation constraints.
> R10–R11 define the need for transaction tracking and renewals, while R12–R13 cover event-driven notifications and reservations for unavailable books.
> Finally, R14 focuses on efficient searching.
>
> From these, I structured my modules: BookService, UserService, LoanService, ReservationService, FineService, and NotificationService.
> Each requirement directly drives a class or feature, ensuring my design covers every functional need clearly."

---

## 🧩 6. Pro tip (for interviews)

You can show that you understand non-functional requirements even if not explicitly mentioned:

| Type | Example |
|------|---------|
| **Performance** | Use indexes or caching for fast search. |
| **Scalability** | Event-driven architecture for notifications. |
| **Reliability** | Transactional consistency when issuing books. |
| **Security** | Role-based access (librarian vs member). |
| **Auditability** | Record all transactions (R10). |

