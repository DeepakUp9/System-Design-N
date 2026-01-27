# Getting Ready: Library Management System

> **Comprehensive Guide for Low-Level Design Interviews**

---

## 📖 Problem Definition

### What is a Library Management System (LMS)?

A **library management system** aims to automate all library activities. It is software that helps manage all the primary functions of library management.

**Purpose:**
With the help of a library management system, we can organize, handle, and maintain the records of numerous books and members comprehensively and systematically.

---

### What Can a Librarian Do with LMS?

A librarian can use this software to:
- ✅ Track the number of books in the library
- ✅ Maintain records of new books
- ✅ Track borrowed books with due dates
- ✅ Record members who borrowed books
- ✅ Manage returned books
- ✅ Calculate fines for late returned books
- ✅ Store and update complete library database

---

### Physical Library Support

LMS also supports maintaining the physical library:
- 📍 Track a book's position in the library
- 🔍 Search for book availability
- 📚 Organize library data efficiently
- 🗂️ Retrieve library records quickly

---

## 🎯 In This LLD Interview Case Study, You'll Focus On:

| Focus Area | Description |
|------------|-------------|
| **Efficient searching** | Search and manage books by various attributes (title, author, category) |
| **Borrowing workflow** | Handle book borrowing, returning, and reservation processes |
| **User roles management** | Manage librarian and member roles with permissions |
| **Fine calculation** | Support fine calculation and renewal processes |
| **Resource tracking** | Monitor book status and availability |

---

### 📌 Note:
> This system model can be adapted for **academic, public, or private libraries** and can support various library policies (such as special collections, inter-library loans, or different user types).

---

## 🔍 Expectations from the Interviewee

The LMS has multiple components, each with its specific requirements and constraints. Let's look at some of the main expectations the interviewer will want to hear you discuss in more detail.

---

## 1. Efficient Searching 🔎

### Context

Searching for books is one of the **most crucial functions of LMS**. The user must be able to search for any book. Different users may want to search for a book through different methods.

### Key Interview Questions:

#### Q1: Can the user search for a book using attributes other than the book name?

**What the Interviewer is Testing:**
- Understanding of multiple search criteria
- Flexibility in search implementation
- Database query optimization

**Expected Answer:**
> *"Yes, the system should support multiple search criteria including title, author, ISBN, publication date, publisher, and subject. I would implement a flexible search interface that allows combining these criteria."*

**Design Approach:**
```java
interface SearchStrategy {
    List<Book> search(SearchCriteria criteria);
}

class SearchCriteria {
    private String title;
    private String author;
    private String ISBN;
    private String publisher;
    private LocalDate publicationDate;
    private Category category;
    
    // Builder pattern for flexible search
    public static class Builder {
        public Builder withTitle(String title) { }
        public Builder withAuthor(String author) { }
        public Builder withISBN(String ISBN) { }
        public SearchCriteria build() { }
    }
}

class BookSearchService implements SearchStrategy {
    @Override
    public List<Book> search(SearchCriteria criteria) {
        // Combine multiple criteria with AND logic
        return catalog.findBooks()
            .filter(book -> matchesCriteria(book, criteria))
            .collect(Collectors.toList());
    }
}
```

---

#### Q2: How can the user search for a book by its author name, publication date, etc.?

**Design Implications:**

| Search Type | Implementation | Index Strategy |
|-------------|----------------|----------------|
| **By Author** | Name matching (exact/fuzzy) | Author index |
| **By Publication Date** | Date range queries | Date index |
| **By Publisher** | Publisher name matching | Publisher index |
| **By Category** | Category filtering | Category index |

**Implementation:**
```java
class Catalog {
    private Map<String, List<Book>> authorIndex;
    private Map<String, List<Book>> titleIndex;
    private Map<Category, List<Book>> categoryIndex;
    private TreeMap<LocalDate, List<Book>> publicationDateIndex;
    
    List<Book> searchByAuthor(String authorName) {
        return authorIndex.getOrDefault(authorName, Collections.emptyList());
    }
    
    List<Book> searchByDateRange(LocalDate start, LocalDate end) {
        return publicationDateIndex.subMap(start, true, end, true)
            .values()
            .stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }
    
    List<Book> searchByCategory(Category category) {
        return categoryIndex.getOrDefault(category, Collections.emptyList());
    }
}
```

---

#### Q3: How will the user search a specific category of books, like magazines, journals, newspapers, etc.?

**Design Approach:**

```java
enum Category {
    BOOK,
    MAGAZINE,
    JOURNAL,
    NEWSPAPER,
    REFERENCE,
    THESIS,
    EBOOK
}

abstract class LibraryItem {
    protected String itemId;
    protected String title;
    protected Category category;
    protected ItemStatus status;
    
    abstract Category getCategory();
}

class Book extends LibraryItem {
    private String ISBN;
    private String author;
    private String publisher;
    
    @Override
    Category getCategory() {
        return Category.BOOK;
    }
}

class Magazine extends LibraryItem {
    private String issueNumber;
    private LocalDate issueDate;
    
    @Override
    Category getCategory() {
        return Category.MAGAZINE;
    }
}

class SearchService {
    List<LibraryItem> searchByCategory(Category category) {
        return catalog.getItemsByCategory(category);
    }
}
```

**💬 What to Say:**
> *"I'd use inheritance to model different item types (Book, Magazine, Journal) and implement category-based filtering with indexed searches for efficiency."*

---

## 2. Versatility 👥

### Context

Before designing the system, it is mandatory to specify the **actors** of the system.

### Key Interview Questions:

#### Q: Can only a librarian or all library members use the software?

**What the Interviewer is Testing:**
- Understanding of role-based access control (RBAC)
- Permission management
- Security considerations

**Expected Answer:**
> *"The system should support multiple user roles with different permissions. Both librarians and members can use the software, but with different access levels."*

**Design Approach:**

```java
enum Role {
    LIBRARIAN,
    MEMBER,
    ADMIN
}

abstract class User {
    protected String userId;
    protected String name;
    protected String email;
    protected Role role;
    
    abstract boolean canBorrowBooks();
    abstract boolean canAddBooks();
    abstract boolean canDeleteBooks();
}

class Librarian extends User {
    private String employeeId;
    
    @Override
    boolean canBorrowBooks() {
        return true;
    }
    
    @Override
    boolean canAddBooks() {
        return true; // Full catalog management
    }
    
    @Override
    boolean canDeleteBooks() {
        return true;
    }
    
    // Librarian-specific operations
    void addBook(Book book) { }
    void removeBook(String bookId) { }
    void issueFine(Member member, double amount) { }
}

class Member extends User {
    private String membershipId;
    private LocalDate membershipDate;
    private int borrowedBooksCount;
    private double outstandingFines;
    private final int MAX_BOOKS_ALLOWED = 5;
    
    @Override
    boolean canBorrowBooks() {
        return borrowedBooksCount < MAX_BOOKS_ALLOWED 
            && outstandingFines == 0;
    }
    
    @Override
    boolean canAddBooks() {
        return false; // Members cannot add books
    }
    
    @Override
    boolean canDeleteBooks() {
        return false; // Members cannot delete books
    }
    
    // Member-specific operations
    void borrowBook(Book book) { }
    void returnBook(Book book) { }
    void reserveBook(Book book) { }
}
```

### Permission Matrix:

| Operation | Librarian | Member | Admin |
|-----------|-----------|--------|-------|
| **Search books** | ✅ | ✅ | ✅ |
| **Borrow books** | ✅ | ✅ | ✅ |
| **Return books** | ✅ | ✅ | ✅ |
| **Add books** | ✅ | ❌ | ✅ |
| **Remove books** | ✅ | ❌ | ✅ |
| **Issue fines** | ✅ | ❌ | ✅ |
| **Add members** | ✅ | ❌ | ✅ |
| **System config** | ❌ | ❌ | ✅ |

---

## 3. Book Reservation 📖

### Context

Another significant feature of LMS is the **reservation of the book**.

### Key Interview Questions:

#### Q1: What is the mechanism of book reservation?

**Expected Answer:**
> *"When a book is currently borrowed, a member can reserve it. The system maintains a reservation queue. When the book is returned, the first person in the queue is notified."*

**Design Approach:**

```java
class Book {
    private String bookId;
    private BookStatus status;
    private Member currentBorrower;
    private Queue<Reservation> reservationQueue;
    
    boolean isAvailable() {
        return status == BookStatus.AVAILABLE;
    }
    
    boolean canBeReserved() {
        return status == BookStatus.BORROWED 
            || status == BookStatus.RESERVED;
    }
}

class Reservation {
    private String reservationId;
    private Member member;
    private Book book;
    private LocalDate reservationDate;
    private ReservationStatus status;
    
    void notifyAvailability() {
        // Notify member that book is available
    }
}

enum BookStatus {
    AVAILABLE,
    BORROWED,
    RESERVED,
    LOST,
    DAMAGED
}

enum ReservationStatus {
    ACTIVE,
    FULFILLED,
    CANCELLED,
    EXPIRED
}

class ReservationService {
    Reservation reserveBook(Member member, Book book) {
        if (!book.canBeReserved()) {
            throw new BookNotReservableException();
        }
        
        Reservation reservation = new Reservation(member, book);
        book.getReservationQueue().offer(reservation);
        
        return reservation;
    }
    
    void processReturn(Book book) {
        // When book is returned
        Reservation nextReservation = book.getReservationQueue().poll();
        
        if (nextReservation != null) {
            book.setStatus(BookStatus.RESERVED);
            nextReservation.notifyAvailability();
        } else {
            book.setStatus(BookStatus.AVAILABLE);
        }
    }
}
```

---

#### Q2: Can a member reserve a book again if it is already reserved?

**Design Implications:**

```java
class ReservationService {
    Reservation reserveBook(Member member, Book book) {
        // Check if member already has active reservation
        if (hasActiveReservation(member, book)) {
            throw new DuplicateReservationException(
                "You already have an active reservation for this book"
            );
        }
        
        // Check reservation limit per member
        if (member.getActiveReservationsCount() >= MAX_RESERVATIONS) {
            throw new ReservationLimitExceededException();
        }
        
        Reservation reservation = new Reservation(member, book);
        book.getReservationQueue().offer(reservation);
        
        return reservation;
    }
    
    boolean hasActiveReservation(Member member, Book book) {
        return book.getReservationQueue().stream()
            .anyMatch(r -> r.getMember().equals(member) 
                && r.getStatus() == ReservationStatus.ACTIVE);
    }
}
```

---

#### Q3: How does the status of the book change when a member returns a book?

**State Transition Flow:**

```
BORROWED
    ↓
  Return
    ↓
    ├─ Has reservations? → RESERVED (notify first in queue)
    └─ No reservations? → AVAILABLE
```

**Implementation:**
```java
class BookReturnService {
    void returnBook(Member member, Book book) {
        // Validate return
        if (!book.getCurrentBorrower().equals(member)) {
            throw new InvalidReturnException();
        }
        
        // Calculate fine if overdue
        LocalDate dueDate = book.getDueDate();
        if (LocalDate.now().isAfter(dueDate)) {
            double fine = calculateFine(dueDate, LocalDate.now());
            member.addFine(fine);
        }
        
        // Clear current borrower
        book.setCurrentBorrower(null);
        book.setDueDate(null);
        
        // Process reservations
        processReservations(book);
        
        // Update member's borrowed count
        member.decrementBorrowedCount();
    }
    
    private void processReservations(Book book) {
        Reservation next = book.getReservationQueue().poll();
        
        if (next != null) {
            book.setStatus(BookStatus.RESERVED);
            next.setStatus(ReservationStatus.FULFILLED);
            next.notifyAvailability();
            
            // Set hold expiry (e.g., 24 hours)
            next.setExpiryDate(LocalDate.now().plusDays(1));
        } else {
            book.setStatus(BookStatus.AVAILABLE);
        }
    }
}
```

---

## 4. Book Renewal 🔄

### Context

Similar to book reservation, the interviewer can ask about the **book renewal** functionality.

### Key Interview Question:

#### Q: What is the mechanism of book renewal if a member wants to hold a book for longer?

**Expected Answer:**
> *"A member can renew a borrowed book if it's not reserved by someone else. The system extends the due date by the standard borrowing period. There's typically a limit on renewals (e.g., 2 times)."*

**Design Approach:**

```java
class BookRenewalService {
    private final int MAX_RENEWALS = 2;
    private final int RENEWAL_DAYS = 14;
    
    void renewBook(Member member, Book book) {
        // Validation checks
        validateRenewal(member, book);
        
        // Extend due date
        LocalDate currentDueDate = book.getDueDate();
        LocalDate newDueDate = currentDueDate.plusDays(RENEWAL_DAYS);
        book.setDueDate(newDueDate);
        
        // Increment renewal count
        book.incrementRenewalCount();
        
        // Log transaction
        logRenewal(member, book, newDueDate);
    }
    
    private void validateRenewal(Member member, Book book) {
        // Check if member is current borrower
        if (!book.getCurrentBorrower().equals(member)) {
            throw new UnauthorizedRenewalException();
        }
        
        // Check if book has reservations
        if (!book.getReservationQueue().isEmpty()) {
            throw new BookReservedException(
                "Cannot renew - book is reserved by another member"
            );
        }
        
        // Check renewal limit
        if (book.getRenewalCount() >= MAX_RENEWALS) {
            throw new RenewalLimitExceededException(
                "Maximum renewals reached"
            );
        }
        
        // Check if member has outstanding fines
        if (member.getOutstandingFines() > 0) {
            throw new OutstandingFinesException(
                "Please clear fines before renewal"
            );
        }
    }
}

class Book {
    private LocalDate dueDate;
    private int renewalCount;
    
    void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    
    void incrementRenewalCount() {
        this.renewalCount++;
    }
    
    void resetRenewalCount() {
        this.renewalCount = 0;
    }
}
```

### Renewal Rules:

| Condition | Can Renew? | Reason |
|-----------|------------|--------|
| **No reservations** | ✅ Yes | No one waiting |
| **Has reservations** | ❌ No | Someone waiting |
| **< 2 renewals** | ✅ Yes | Within limit |
| **≥ 2 renewals** | ❌ No | Limit reached |
| **No fines** | ✅ Yes | Account clear |
| **Has fines** | ❌ No | Must pay first |

---

## 5. Fine Management 💰

### Context

There is another question that the interviewer may be interested in asking.

### Key Interview Question:

#### Q: How is the calculation and deduction of the fine handled if the book is returned late?

**Expected Answer:**
> *"The system calculates fines based on the number of overdue days multiplied by a daily fine rate. Fines are added to the member's account and must be cleared before borrowing new books."*

**Design Approach:**

```java
class FineCalculationService {
    private final double DAILY_FINE_RATE = 1.0; // $1 per day
    private final double MAX_FINE_PER_BOOK = 50.0;
    
    double calculateFine(LocalDate dueDate, LocalDate returnDate) {
        if (!returnDate.isAfter(dueDate)) {
            return 0.0; // No fine if returned on time
        }
        
        long overdueDays = ChronoUnit.DAYS.between(dueDate, returnDate);
        double calculatedFine = overdueDays * DAILY_FINE_RATE;
        
        // Cap at maximum fine
        return Math.min(calculatedFine, MAX_FINE_PER_BOOK);
    }
}

class Member {
    private double outstandingFines;
    private List<Fine> fineHistory;
    
    void addFine(double amount) {
        Fine fine = new Fine(amount, LocalDate.now());
        fineHistory.add(fine);
        outstandingFines += amount;
    }
    
    void payFine(double amount) {
        if (amount > outstandingFines) {
            throw new InvalidPaymentException("Amount exceeds outstanding fines");
        }
        
        outstandingFines -= amount;
        recordPayment(amount);
    }
    
    boolean canBorrowBooks() {
        return borrowedBooksCount < MAX_BOOKS_ALLOWED 
            && outstandingFines == 0;
    }
}

class Fine {
    private String fineId;
    private double amount;
    private LocalDate issueDate;
    private FineStatus status;
    private LocalDate paymentDate;
    
    void markAsPaid() {
        status = FineStatus.PAID;
        paymentDate = LocalDate.now();
    }
}

enum FineStatus {
    PENDING,
    PAID,
    WAIVED
}
```

### Fine Calculation Example:

```
Book borrowed: Jan 1, 2025
Due date: Jan 15, 2025
Returned: Jan 20, 2025

Overdue days = 5 days
Fine = 5 × $1.00 = $5.00
```

---

## 🏗️ Design Approach

We will design this library management system using the **bottom-up design approach**. For this purpose, we will follow the steps below:

### Step 1: Identify Core Entities

**Simple core entities:**
- 📚 Book
- 👤 Member
- 👨‍💼 Librarian
- 📋 Reservation
- 💵 Fine
- 📖 Transaction

### Step 2: Model Workflows

**Key workflows:**
- Book search
- Book borrowing
- Book return
- Reservation
- Renewal
- Fine calculation

### Step 3: Access Control

**Ensure the system supports:**
- Role-based access control (RBAC)
- Permission validation
- Secure operations

### Step 4: Data Integrity

**Maintain accurate records:**
- Transaction history
- Catalog consistency
- Member accounts

### Step 5: Scalability & Concurrency

**Address:**
- Concurrent borrowing/returning
- Large catalog searches
- Multiple user operations

### Step 6: SOLID Principles

**Ensure:**
- Maintainability
- Extensibility
- Clean design

---

## 🎨 Design Patterns

During an interview, discussing the design pattern a library management system falls under is always a good practice. Stating the design pattern gives the interviewer a positive impression and shows that the interviewee is well-versed in the advanced concepts of object-oriented design.

### Applicable Design Patterns:

| Pattern | Usage | Benefit |
|---------|-------|---------|
| **Strategy** | Search algorithms | Flexible search criteria |
| **Observer** | Reservation notifications | Real-time alerts |
| **Factory** | Creating library items | Centralized creation |
| **Singleton** | Catalog management | Single source of truth |
| **State** | Book status transitions | Clean state management |
| **Command** | Transaction logging | Undo/audit trail |

---

## 💬 Interview-Winning Statements

### On Search:
> *"I'd implement the Strategy pattern for search, allowing multiple criteria with indexed lookups for performance."*

### On Roles:
> *"I'd use role-based access control with clear permission matrices to separate librarian and member capabilities."*

### On Reservations:
> *"I'd maintain a queue per book using the Observer pattern to notify the next person when a book becomes available."*

### On Fines:
> *"I'd calculate fines on return using a configurable rate strategy, capping at a maximum to prevent excessive charges."*

### On Renewals:
> *"I'd validate renewals against reservation status and limit count to ensure fairness."*

---

