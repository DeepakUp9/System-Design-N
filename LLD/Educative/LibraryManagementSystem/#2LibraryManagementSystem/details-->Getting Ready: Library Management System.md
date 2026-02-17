# Getting Ready: Library Management System

## Understand the library management system problem and learn the questions to simplify this problem

A library management system (LMS) aims to automate all library activities. It is software that helps manage all the primary functions of library management. With the help of a library management system, we can organize, handle, and maintain the records of numerous books and members comprehensively and systematically.

A librarian can use this software to track the number of books in the library and retain several records, including new books, borrowed books with due dates, members who borrowed books, returned books, fines for late returned books, etc. In short, the library management system stores and updates the complete library database.

LMS also supports maintaining the physical library. The user can keep track of a book's position in the library and search for whether or not the specific book is currently available. Therefore, LMS helps organize and retrieve library data efficiently.

## In this LLD interview case study, your focus will be on:

* Efficiently searching and managing books and other resources by various attributes (title, author, category, etc.).
* Handling book borrowing, returning, and reservation workflows.
* Managing user roles (librarian, member) and their permissions.
* Supporting fine calculation and renewal processes for overdue and renewed books.

>  **Note:** This system model can be adapted for academic, public, or private libraries and can support various library policies (such as special collections, inter-library loans, or different user types).

## Expectations from the interviewee

The LMS has multiple components, each with its specific requirements and constraints. Let's look at some of the main expectations the interviewer will want to hear you discuss in more detail.

# Efficient Searching

Searching for books is one of the most crucial functions of LMS. The user must be able to search for any book. Different users may want to search for a book through different methods. Therefore, the interviewer can ask questions like these:

* Would the user be able to search for a book using attributes other than the book name?
* How will the user be able to search for a book by its author name, publication date, etc.?
* How will the user search a specific category of books, like magazines, journals, newspapers, etc.?

## 🔍 Core Idea

When you design a Library Management System, one of the main user actions is searching — finding a book quickly and accurately among possibly thousands of books.

So, "Efficient Searching" means:

Designing the data structures, algorithms, and system flow so that users can find books easily, using different attributes (not just the book title).

## 💡 What the Interviewer Is Testing

They're not just asking if you can code a search function — they want to see:

1. Your understanding of user needs (what users might search for)
2. Your database modeling (how data should be stored for flexible searching)
3. Your system optimization (how to make the search fast even for large data)

## 📘 Possible Search Criteria

The paragraph gives hints that users may want to search by:

* Book name (e.g., "Harry Potter")
* Author name (e.g., "J.K. Rowling")
* Publication date (e.g., "Books published in 2020")
* Category (e.g., "Magazines", "Journals", "Newspapers")

So the interviewer might ask:

* How would you design the database to support all these searches?
* Would you use indexes for faster searching?
* How would you filter results for a specific type of book?

## ⚙️ Example — How You Might Design It

### 🧩 Class Design

You might have a base class:

```java
class Book {
    String title;
    List<String> authors;
    Date publicationDate;
    String category; // e.g., "Book", "Magazine", "Journal"
    String ISBN;
}
```

Then subclasses for specific types:

```java
class Magazine extends Book { /* additional fields */ }
class Journal extends Book { /* additional fields */ }
```

### 🗃️ SearchService

```java
class SearchService {
    List<Book> searchByTitle(String title);
    List<Book> searchByAuthor(String author);
    List<Book> searchByPublicationDate(Date date);
    List<Book> searchByCategory(String category);
}
```

## ⚡ Efficiency Aspect

To make searches fast:

* Use indexes on frequently searched fields (title, author).
* Use hash maps or tries for in-memory search optimization.
* Use full-text search engines (like Elasticsearch) for large-scale systems.

<span style="background-color: yellow; color: blue;">If you want to understand more in depth, <a href="./deapth/EfficiencyAspect.md">click here</a></span>


## 💬 Example Interview Dialogue

**Interviewer:** Can the user search for books using attributes other than the book name?

**You:** Yes, users should be able to search by author, publication date, or category. I'd design the `Book` entity with those attributes and add indexes on them for efficient lookups.

**Interviewer:** How would you handle searching for a specific type of material like journals or magazines?

**You:** I'd use an inheritance hierarchy where `Journal` and `Magazine` extend `Book`, and filtering by category would just mean checking the `type` field or querying the corresponding subclass.

## 🧠 Summary

This part of the LLD article wants you to think beyond "just search by name" and focus on:

* Supporting multiple search filters
* Designing flexible and efficient data models
* Ensuring fast and scalable queries


# Versatility

Before designing the system, it is mandatory to specify the actors of the system. Hence, the interviewer can ask about the actors of the system as follows:

**Can only a librarian or all library members use the software?**

---

## 🎯 What the Interviewer is Really Asking

When they say:

> "Can only a librarian or all library members use the software?"

They're checking if you:

* Understand who interacts with the system (the "actors").
* Can identify what each actor can do.
* Can design your system so that roles are separated and controlled (authorization, access levels).

This is about **versatility** — how flexible your system is for different users.

---

## 🧩 Step 1: Define "Actors"

**Actors = people or external systems that interact with your system.**

In a Library Management System (LMS), the common actors are:

| Actor | Description | Typical Actions |
|-------|-------------|-----------------|
| **Librarian (Admin)** | Manages the entire library's collection and members | Add/update/delete books, register members, issue/return books, manage fines |
| **Library Member (User)** | A student or reader who borrows or searches for books | Search for books, check availability, borrow/return books, view history |
| **System / External Service** | System components that interact (e.g., Payment service, Notification service) | Auto fine calculation, email alerts |
| **Guest / Visitor** | A non-registered user | Browse or search catalog only |

---

## 🧠 Step 2: How to Answer in an Interview

When asked **"Can only the librarian or all members use the software?"**  
— don't just say "Yes, everyone can use it."

Instead, give a **structured and layered answer** like this:

### ✅ Sample Strong Answer

> "The system should be versatile enough to support multiple user roles.
> 
> The **Librarian** will have full administrative privileges — adding new books, registering members, updating inventory, and managing borrow/return transactions.
> 
> The **Library Members** will have limited access — mainly to search for books, check availability, place hold requests, and view their borrowing history.
> 
> If we extend it further, even **guests** could access a read-only catalog without authentication.
> 
> So, yes — both librarians and members can use the system, but their functional access is controlled by role-based permissions."

---

## 🧩 Step 3: Class Design Example

You can describe this to the interviewer or draw it:

```java
abstract class User {
    private String userId;
    private String name;
    private String email;
}

class Librarian extends User {
    void addBook(Book book) {}
    void removeBook(String bookId) {}
    void registerMember(Member member) {}
    void issueBook(Book book, Member member) {}
}

class Member extends User {
    void searchBook(String title) {}
    void borrowBook(Book book) {}
    void returnBook(Book book) {}
    void viewBorrowHistory() {}
}
```

### ✅ Key Idea:

* `User` is a base class.
* Different subclasses (like `Librarian`, `Member`) define role-specific behavior.
* This is called **role-based design** or **polymorphism** in LLD.

---

## ⚙️ Step 4: Example UML Diagram (Conceptually)

```
          +----------------+
          |     User       |
          +----------------+
          | - userId       |
          | - name         |
          | - email        |
          +----------------+
                 /\
                /  \
     +-------------------+        +-------------------+
     |    Librarian      |        |      Member       |
     +-------------------+        +-------------------+
     | +addBook()        |        | +searchBook()     |
     | +removeBook()     |        | +borrowBook()     |
     | +issueBook()      |        | +returnBook()     |
     +-------------------+        +-------------------+
```

This clearly shows how both actors exist in the same system, but their access and operations differ.

---

## 🧩 Step 5: Access Control (Extra Point for Depth)

You can add a small note to sound advanced:

> "In implementation, I'd enforce these roles through **Role-Based Access Control (RBAC)**.  
> For example, using **Spring Security**, we can define roles like `ROLE_LIBRARIAN` and `ROLE_MEMBER`, and map endpoints like `/admin/**` and `/member/**` accordingly.  
> This ensures the system is both versatile and secure."

---

## ✅ Summary Answer Template

When asked **"Can only a librarian or all members use the software?"** —  
say something like this (memorize the flow):

> "The system is designed for both librarians and members — with different access levels.  
> Librarians have administrative rights like adding or issuing books, while members can only search, borrow, or view their history.  
> This makes the system **versatile** and supports role-based operations, ensuring proper control and scalability."

<span style="background-color: yellow; color: blue;">If you want to understand more in depth, <a href="./deapth/securityConfiguration.md">click here</a></span>


# Book Reservation

Another significant feature of LMS is the reservation of books.

**Key Questions:**
* What is the mechanism of book reservation?
* Can a member reserve a book again if it is already reserved?
* How does the status of the book change when a member returns a book?

---

## 🧩 1. What is "Book Reservation" Mechanism?

### Definition

**Book reservation** means a member can place a hold on a book that's currently checked out (unavailable), so they get priority once it becomes available again.

---

## 🔁 Typical Lifecycle of a Book

| Status | Description | Who Can Change It |
|--------|-------------|-------------------|
| **AVAILABLE** | Book is present in library | Librarian or System |
| **ISSUED** | Book is borrowed by a member | Librarian |
| **RESERVED** | Book is not available now, but another member has reserved it | Member |
| **RETURNED** | Book returned by borrower | Librarian/System auto updates |
| **DAMAGED/LOST** | Book is not usable | Librarian |

---

## 📚 Example Scenario

1. Book **"Effective Java"** is borrowed by **Member A** → Status = `ISSUED`

2. **Member B** searches for it, sees it's unavailable → clicks **"Reserve Book"**

3. System creates a reservation record:

```
Reservation:
- bookId: B123
- reservedBy: Member B
- status: ACTIVE
- createdAt: 2025-11-05
```

4. When **Member A** returns the book → system checks if reservations exist

5. If found → automatically updates book status to `RESERVED`, and notifies **Member B** (SMS/Email)

6. **Member B** has X hours (say 24 hrs) to pick up the book

7. If they don't → reservation expires → book becomes `AVAILABLE`

---

## 🧠 2. Can a Member Reserve a Book Again If It Is Already Reserved?

### ✅ Answer:

**No**, generally a member cannot reserve a book that's already reserved by someone else.

However, there are two ways to design it, depending on system requirements:

| Design Type | Description | Example |
|-------------|-------------|---------|
| **Simple Reservation Queue** (1 active at a time) | Only one person can reserve a book at a time. Once available, the reservation expires or completes. | Small library system |
| **Multi-User Reservation Queue** (FIFO) | Many users can reserve the same book; system maintains a queue. When book returns, the first in queue gets notified. | Large university library |

### 🧩 Example Queue:

```
Book ID: B123
Reservations:
  1. Member B (active)
  2. Member C (waiting)
```

When **B** picks it up → queue updates, next waiting member (**C**) becomes active.

---

## ⚙️ 3. How Does the Status of the Book Change When a Member Returns It?

When **Book Return** happens, we trigger state transitions like a small state machine.

| Current State | Condition | Next State | Trigger |
|---------------|-----------|------------|---------|
| **ISSUED** | No active reservation | **AVAILABLE** | Return accepted |
| **ISSUED** | Reservation exists | **RESERVED** | Reservation auto-activated |
| **RESERVED** | Reserved member fails to collect | **AVAILABLE** | After reservation expiry |
| **AVAILABLE** | Book borrowed | **ISSUED** | Checkout action |

### 💡 Flow Example

1. Book **"Design Patterns"** is `ISSUED` to **Member A**

2. **Member B** has already reserved it

3. **Member A** returns the book → system checks for active reservations

4. Finds reservation → changes status → `RESERVED` (not available for general borrowing)

5. **Member B** picks up → changes status → `ISSUED`

---

## 🧱 4. LLD — Entities and Relationships

Here's how you'd model it in classes:

```java
class Book {
    String id;
    String title;
    BookStatus status;
}

enum BookStatus {
    AVAILABLE, ISSUED, RESERVED
}

class Reservation {
    String id;
    Book book;
    Member member;
    ReservationStatus status;
    LocalDateTime createdAt;
}

enum ReservationStatus {
    ACTIVE, COMPLETED, CANCELLED, EXPIRED
}
```

---

## 🧩 5. Reservation Logic Example (Pseudo-code)

```java
public void reserveBook(String bookId, Member member) {
    Book book = bookRepository.findById(bookId);

    if (book.getStatus() == BookStatus.AVAILABLE) {
        throw new IllegalStateException("Book is available, you can borrow it directly.");
    }

    Optional<Reservation> existing = reservationRepository
        .findActiveByBookAndMember(book, member);

    if (existing.isPresent()) {
        throw new IllegalStateException("You already reserved this book.");
    }

    Reservation reservation = new Reservation();
    reservation.setBook(book);
    reservation.setMember(member);
    reservation.setStatus(ReservationStatus.ACTIVE);
    reservationRepository.save(reservation);
}
```

---

## 🔄 6. Book Return Workflow (With Status Change)

```java
public void returnBook(String bookId) {
    Book book = bookRepository.findById(bookId);

    Optional<Reservation> nextReservation =
        reservationRepository.findFirstActiveReservationByBookOrderByDate(book);

    if (nextReservation.isPresent()) {
        book.setStatus(BookStatus.RESERVED);
        notifyMember(nextReservation.get().getMember());
    } else {
        book.setStatus(BookStatus.AVAILABLE);
    }

    bookRepository.save(book);
}
```

---

## 🧠 7. Interview-Level Explanation Summary

When asked:

> **"What is the mechanism of book reservation and how status changes?"**

You can answer like this:

> "When a member tries to borrow a book that's already issued, the system allows them to place a **reservation**.  
> A `Reservation` record is created linking the member and the book.
> 
> Once the book is returned, the system automatically checks if there's an **active reservation**.  
> If yes — the book status changes from `ISSUED` to `RESERVED` and the next member in queue is notified.  
> If not — the status becomes `AVAILABLE`.
> 
> Only one active reservation is allowed per book at a time (or we can use a queue for multi-reservation).  
> This ensures fairness and avoids double-reserving the same book."

---

## 🧭 8. Optional Advanced (If Interviewer Pushes Deeper)

You can mention:

### State Machine Design

Could implement book status flow using **Spring State Machine** (transitions between `AVAILABLE` → `ISSUED` → `RESERVED` → `AVAILABLE`).

### Concurrency Control

Use DB transaction locks (e.g., `@Transactional` + `PESSIMISTIC_WRITE`) to avoid race conditions when multiple users reserve simultaneously.

**Example:**

```java
@Transactional
@Lock(LockModeType.PESSIMISTIC_WRITE)
public void reserveBook(String bookId, Member member) {
    // Lock the book record to prevent concurrent reservations
    Book book = bookRepository.findByIdWithLock(bookId);
    // ... reservation logic
}
```

### Notifications

Use **Kafka** or async events to notify next member when book becomes available.

**Example Architecture:**

```
Book Return → Event Published → Kafka Topic
              ↓
    Notification Service → Email/SMS to Member
```

### Reservation Expiry Handling

Implement a scheduled job to expire reservations:

```java
@Scheduled(fixedRate = 3600000) // every hour
public void expireReservations() {
    List<Reservation> expired = reservationRepository
        .findExpiredReservations(LocalDateTime.now().minus(24, ChronoUnit.HOURS));
    
    expired.forEach(r -> {
        r.setStatus(ReservationStatus.EXPIRED);
        r.getBook().setStatus(BookStatus.AVAILABLE);
    });
}
```

---

## 📊 State Diagram

```
    [AVAILABLE]
         |
         | (Member borrows)
         ↓
     [ISSUED]
         |
         | (Member returns)
         ↓
    Has Reservation? ─── No ───→ [AVAILABLE]
         |
        Yes
         |
         ↓
    [RESERVED]
         |
         | (Reserved member borrows)
         ↓
     [ISSUED]
         |
         | (Reservation expires)
         ↓
    [AVAILABLE]
```

---

## 🎯 Key Takeaways

1. **Reservation ensures fairness** - Members can hold a book that's currently unavailable
2. **Status transitions are automated** - System handles state changes based on conditions
3. **Queue management** - Supports single or multiple reservations per book
4. **Notifications are critical** - Members must be informed when their reserved book is available
5. **Concurrency matters** - Use proper locking to prevent race conditions
6. **Expiry mechanism** - Reservations should timeout to free up books

---

## 🧪 Testing Scenarios

| Scenario | Expected Behavior |
|----------|-------------------|
| Reserve available book | Error: "Book is available, borrow directly" |
| Reserve already-reserved book (own) | Error: "Already reserved by you" |
| Reserve already-reserved book (other) | Success (added to queue) OR Error (depending on design) |
| Return book with no reservations | Status → AVAILABLE |
| Return book with active reservation | Status → RESERVED, notify next member |
| Reservation expires | Status → AVAILABLE, notify next in queue |

---

## 💡 Best Practices

1. **Use enums** for status types to prevent invalid states
2. **Implement auditing** - Track who reserved, when, and for how long
3. **Add reservation limits** - Prevent users from reserving too many books
4. **Implement priority queues** - Faculty might get priority over students
5. **Handle edge cases** - Book damaged/lost during reservation
6. **Add analytics** - Track reservation patterns for inventory planning

<span style="background-color: yellow; color: blue;">If you want to understand more in depth, <a href="./deapth/bookreservation.md">click here</a></span>


# Book Renewal

Similar to the book reservation, the interviewer can ask about the book renewal functionality with a question like this:

* **What is the mechanism of book renewal if a member wants to hold a book for longer?**

## Answer

For book renewal, I model a separate `Loan` entity representing each issued copy, containing fields like `issueDate`, `dueDate`, and `renewalCount`. 

When a member requests renewal, the system checks that:
- The book is still issued to them
- The due date has not passed
- The book isn't reserved by another member
- The renewal count hasn't reached the limit

The renewal simply extends the `dueDate` and increments the `renewalCount`.

I ensure consistency using transactional locking on the `Book` record, and I publish an asynchronous event to notify the member of the new due date. This design integrates cleanly with the reservation and state machine modules, keeping everything consistent and extensible.

<span style="background-color: yellow; color: blue;">If you want to understand more in depth, <a href="./deapth/bookRenewal.md">click here</a></span>


# Fine Management

There is another question that the interviewer may be interested in asking:

* **How is the calculation and deduction of fines handled if the book is returned late?**

## Answer

When a member returns a book, the system compares the return date with the due date. If the return date is later, we calculate the fine as `(daysLate * fineRate)` — say ₹5 per day — and create a `Fine` record linked to that `Loan`. 

The fine is marked unpaid until cleared, and the book's status changes from `ISSUED` to `AVAILABLE`.

We ensure consistency by running this within a single transaction, and we can notify the member automatically. The design also allows variable fine rates by category and integrates cleanly with the state machine transition from `ISSUED` → `AVAILABLE`.

<span style="background-color: yellow; color: blue;">If you want to understand more in depth, <a href="./deapth/fineManagement.md">click here</a></span>


# Library Management System - Design Approach

## Design Approach

We will design this Library Management System using the **bottom-up design approach**. For this purpose, we will follow the steps below:

* **First**, we'll identify simple core entities such as **Book**, **Member**, and **Librarian**, and define their responsibilities.

* **Next**, we'll model book search, borrowing, return, reservation, renewal, and fine calculation workflows.

* We'll ensure the system supports **role-based access control** and maintains accurate transaction and catalog records.

This design approach will address **concurrency** and **scalability** and follow **SOLID principles** to ensure maintainability and extensibility. Later, diagrams and code will illustrate major workflows and class structures.

---

## Key Design Principles

### Bottom-Up Approach
Starting with fundamental building blocks (entities) and progressively building more complex features on top of them.

### Core Focus Areas
- **Entity Design**: Clear definition of responsibilities for each entity
- **Workflow Modeling**: Comprehensive coverage of all library operations
- **Access Control**: Role-based permissions and security
- **Data Integrity**: Accurate transaction and catalog management

### Quality Attributes
- **Concurrency Handling**: Managing simultaneous operations safely
- **Scalability**: Supporting growing users and catalog size
- **Maintainability**: Following SOLID principles for clean, extensible code
- **Extensibility**: Easy addition of new features without breaking existing functionality

---
# Design pattern 
During an interview, it is always a good practice to discuss the design patterns that a library management system falls under. Stating the design patterns gives the interviewer a positive impression and shows that the interviewee is well-versed in the advanced concepts of object-oriented design.