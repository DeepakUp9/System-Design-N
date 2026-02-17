# Library Management System - Design Documentation

## Table of Contents
1. [System Overview](#system-overview)
2. [Design Patterns](#design-patterns)
3. [SOLID Principles](#solid-principles)
4. [Entity Relationships](#entity-relationships)
5. [Business Logic](#business-logic)
6. [Workflow Diagrams](#workflow-diagrams)

---

## 1. System Overview

### Purpose
A production-ready library management system that automates library operations including book cataloging, member management, borrowing, reservations, renewals, and fine management.

### Core Components
- **Entities**: Book, BookItem, User, Member, Librarian, LibraryCard, etc.
- **Services**: BorrowingService, ReservationService, RenewalService, FineService
- **Factories**: UserFactory, BookFactory, LibraryFactory
- **Observers**: EmailNotificationObserver, SMSNotificationObserver
- **Strategies**: Various search strategies

---

## 2. Design Patterns

### 2.1 Singleton Pattern - Library Class

**Intent**: Ensure only one Library instance exists globally

**Implementation**:
```java
public class Library {
    private static volatile Library instance;
    
    private Library() {
        // Private constructor
    }
    
    public static Library getInstance() {
        if (instance == null) {
            synchronized (Library.class) {
                if (instance == null) {
                    instance = new Library();
                }
            }
        }
        return instance;
    }
}
```

**Benefits**:
- Single source of truth for library data
- Thread-safe using double-checked locking
- Global access point

---

### 2.2 Factory Pattern - Object Creation

**Intent**: Encapsulate object creation logic

**Implementations**:

#### UserFactory
```java
public class UserFactory {
    public static Member createMember(String name, String email, 
                                     String phone, Address address) {
        validateUserInput(name, email, phone, address);
        return new Member(name, email, phone, address);
    }
    
    public static Librarian createLibrarian(String name, String email, 
                                           String phone, Address address, 
                                           String department) {
        validateUserInput(name, email, phone, address);
        return new Librarian(name, email, phone, address, department);
    }
}
```

#### BookFactory
```java
public class BookFactory {
    public static Book createBook(String ISBN, String title, 
                                 List<Author> authors, String subject) {
        validateBookInput(ISBN, title, authors, subject);
        return new Book(ISBN, title, authors, subject);
    }
    
    public static BookItem createBookItem(Book book, Rack rack, double price) {
        // Validation and creation
        return new BookItem(book, rack, price);
    }
}
```

**Benefits**:
- Centralized validation
- Consistent object creation
- Easy to extend with new types

---

### 2.3 Observer Pattern - Notification System

**Intent**: Define one-to-many dependency for notifications

**Implementation**:

```java
// Observer Interface
public interface NotificationObserver {
    void update(Member member, String subject, String message);
    String getChannelType();
}

// Subject Interface
public interface NotificationSubject {
    void attach(NotificationObserver observer);
    void detach(NotificationObserver observer);
    void notifyObservers(Member member, String subject, String message);
}

// Concrete Subject
public class NotificationService implements NotificationSubject {
    private final List<NotificationObserver> observers;
    
    @Override
    public void notifyObservers(Member member, String subject, String message) {
        for (NotificationObserver observer : observers) {
            observer.update(member, subject, message);
        }
    }
}

// Concrete Observers
public class EmailNotificationObserver implements NotificationObserver {
    @Override
    public void update(Member member, String subject, String message) {
        sendEmail(member.getEmail(), subject, message);
    }
}

public class SMSNotificationObserver implements NotificationObserver {
    @Override
    public void update(Member member, String subject, String message) {
        sendSMS(member.getPhone(), subject, message);
    }
}
```

**Benefits**:
- Loose coupling between notification sources and receivers
- Easy to add new notification channels
- Automatic notifications on events

**Notifications Sent**:
- Overdue book alerts
- Reservation available
- Due date reminders
- Fine payment confirmations

---

### 2.4 Strategy Pattern - Search Algorithms

**Intent**: Define family of algorithms, make them interchangeable

**Implementation**:

```java
// Strategy Interface
public interface SearchStrategy {
    List<Book> search(List<Book> catalog, String searchTerm);
    String getStrategyName();
}

// Concrete Strategies
public class SearchByTitleStrategy implements SearchStrategy {
    @Override
    public List<Book> search(List<Book> catalog, String searchTerm) {
        return catalog.stream()
            .filter(book -> book.getTitle().toLowerCase()
                .contains(searchTerm.toLowerCase()))
            .collect(Collectors.toList());
    }
}

public class SearchByAuthorStrategy implements SearchStrategy {
    @Override
    public List<Book> search(List<Book> catalog, String searchTerm) {
        return catalog.stream()
            .filter(book -> book.getAuthors().stream()
                .anyMatch(author -> author.getName().toLowerCase()
                    .contains(searchTerm.toLowerCase())))
            .collect(Collectors.toList());
    }
}

// Context
public class Catalog {
    private final Map<String, SearchStrategy> searchStrategies;
    
    public List<Book> search(String strategyName, String searchTerm) {
        SearchStrategy strategy = searchStrategies.get(strategyName);
        return strategy.search(books, searchTerm);
    }
}
```

**Available Strategies**:
- SearchByTitleStrategy
- SearchByAuthorStrategy
- SearchBySubjectStrategy
- SearchByPublicationDateStrategy

**Benefits**:
- Runtime algorithm selection
- Easy to add new search criteria
- Open/Closed principle compliance

---

### 2.5 State Pattern - Book Status Management

**Intent**: Allow object to alter behavior when internal state changes

**Implementation**:

```java
public enum BookStatus {
    AVAILABLE, BORROWED, RESERVED, LOST, DAMAGED, UNDER_REPAIR
}

public class BookItem {
    private BookStatus status;
    
    public void checkout(Member member, LocalDate dueDate) {
        if (!isAvailable()) {
            throw new IllegalStateException("Book not available");
        }
        this.status = BookStatus.BORROWED;
        // ... other state changes
    }
    
    public void returnBook() {
        if (!isBorrowed()) {
            throw new IllegalStateException("Book not borrowed");
        }
        
        if (hasReservations()) {
            this.status = BookStatus.RESERVED;
        } else {
            this.status = BookStatus.AVAILABLE;
        }
    }
}
```

**State Transitions**:
```
AVAILABLE → BORROWED → RESERVED → AVAILABLE
         ↓         ↓
        LOST    DAMAGED
```

---

### 2.6 Composition Pattern

**Intent**: Strong ownership relationships

**Implementations**:

#### Book → BookItem
```java
public class Book {
    private final List<BookItem> bookItems; // Composed
    
    public void addBookItem(BookItem bookItem) {
        bookItems.add(bookItem);
    }
}
```

#### User → LibraryCard
```java
public class User {
    private LibraryCard libraryCard; // Composed
    
    public User(...) {
        this.libraryCard = new LibraryCard(LocalDate.now());
    }
}
```

**Characteristic**: If parent is deleted, children are deleted

---

## 3. SOLID Principles

### 3.1 Single Responsibility Principle (SRP)

**Each class has one reason to change**

Examples:
- `BorrowingService`: Only borrowing operations
- `FineService`: Only fine calculations
- `NotificationService`: Only notifications
- `ReservationService`: Only reservations

### 3.2 Open/Closed Principle (OCP)

**Open for extension, closed for modification**

Examples:
- Can add new `SearchStrategy` without modifying `Catalog`
- Can add new `NotificationObserver` without modifying `NotificationService`
- Can create new user types extending `User`

### 3.3 Liskov Substitution Principle (LSP)

**Subclasses must be substitutable for base classes**

Example:
```java
User user = new Member(...);  // ✅ Works
user = new Librarian(...);     // ✅ Works

// All User methods work correctly for both
boolean canBorrow = user.canBorrowBooks();
```

### 3.4 Interface Segregation Principle (ISP)

**Clients shouldn't depend on interfaces they don't use**

Examples:
- `SearchStrategy`: Only search-related methods
- `NotificationObserver`: Only notification methods
- No "fat" interfaces with unnecessary methods

### 3.5 Dependency Inversion Principle (DIP)

**Depend on abstractions, not concretions**

Example:
```java
public class BorrowingService {
    private final NotificationService notificationService; // Abstraction
    
    public BorrowingService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
}
```

---

## 4. Entity Relationships

### 4.1 Composition (Strong)

| Parent | Child | Multiplicity | Description |
|--------|-------|--------------|-------------|
| Library | BookItem | 1 → 1..* | Library owns book items |
| Book | BookItem | 1 → 1..* | Book owns its copies |
| User | LibraryCard | 1 → 1 | User owns library card |

### 4.2 Aggregation (Weak)

| Container | Element | Multiplicity | Description |
|-----------|---------|--------------|-------------|
| Catalog | Book | 1 → 1..* | Catalog contains books |

### 4.3 Association

| Entity A | Entity B | Type | Multiplicity |
|----------|----------|------|--------------|
| Rack | BookItem | Two-way | 1 → 0..* |
| Member | BookItem | One-way | 1 → 0..10 |
| BookReservation | BookItem | One-way | 1 → 1 |
| BookLending | BookItem | One-way | 1 → 1 |

### 4.4 Inheritance

```
User (Abstract)
├── Member
└── Librarian
```

---

## 5. Business Logic

### 5.1 Borrowing Rules

**Requirement R7: Max 10 books per member**
```java
if (member.getBorrowedBooksCount() >= 10) {
    throw new BorrowLimitExceededException();
}
```

**Requirement R8: 15-day borrowing period**
```java
LocalDate dueDate = LocalDate.now().plusDays(15);
```

**Eligibility Checks**:
1. Member account must be active
2. No outstanding fines
3. Under borrowing limit
4. Book must be available

### 5.2 Reservation Rules

**Requirement R9: FIFO queue, one reservation per member per book**

```java
// Queue implementation
Queue<BookReservation> reservationQueue = new LinkedList<>();

// Add to queue
public void reserveBook(Member member, BookItem bookItem) {
    if (hasActiveReservation(member, bookItem)) {
        throw new BookReservedException();
    }
    reservationQueue.offer(new BookReservation(member, bookItem));
}

// Process next in queue
BookReservation next = bookItem.getNextReservation();
next.markAsAvailable();
notificationService.sendReservationAvailableNotification(next.getMember());
```

### 5.3 Renewal Rules

**Requirement R11: Policy-based renewal**

Renewal allowed if:
1. Member is current borrower
2. No active reservations
3. Renewal count < 2
4. No outstanding fines
5. Book not overdue

```java
if (lending.getRenewalCount() >= 2) {
    throw new RenewalLimitExceededException();
}
if (bookItem.hasReservations()) {
    throw new BookReservedException();
}
```

### 5.4 Fine Calculation

**Algorithm**:
```java
long overdueDays = ChronoUnit.DAYS.between(dueDate, returnDate);
double fine = Math.min(overdueDays * 1.0, 50.0); // $1/day, max $50
```

---

## 6. Workflow Diagrams

### 6.1 Borrowing Workflow

```
Member Request
      ↓
Check Member Eligibility
   ├─ Active account?
   ├─ Under limit? (< 10)
   └─ No fines?
      ↓
Check Book Availability
   └─ Status = AVAILABLE?
      ↓
Process Checkout
   ├─ Update BookItem status → BORROWED
   ├─ Set due date (current + 15 days)
   ├─ Update member borrowed count
   └─ Create BookLending record
      ↓
Send Notification (Observer Pattern)
   ├─ Email notification
   └─ SMS notification
      ↓
Success
```

### 6.2 Reservation Workflow

```
Member wants unavailable book
      ↓
Check if book can be reserved
   └─ Status = BORROWED?
      ↓
Check duplicate reservation
   └─ Member already has active reservation?
      ↓
Add to FIFO queue
   └─ BookReservation created
      ↓
When book returned
      ↓
Process reservation queue
   ├─ Get next reservation
   ├─ Mark as PENDING
   ├─ Set 24-hour expiry
   └─ Send notification (Observer Pattern)
      ↓
Member collects within 24 hours
   └─ Complete reservation
```

### 6.3 Return Workflow

```
Member returns book
      ↓
Find lending record
      ↓
Calculate fine (if overdue)
   └─ overdueDays × $1.00
      ↓
Update book status
   ├─ Has reservations? → RESERVED
   └─ No reservations? → AVAILABLE
      ↓
Process reservation queue
   ├─ Notify next member
   └─ Set hold period
      ↓
Update member state
   ├─ Decrement borrowed count
   └─ Add fine (if any)
      ↓
Complete
```

### 6.4 Search Workflow (Strategy Pattern)

```
User initiates search
      ↓
Select search strategy
   ├─ By Title
   ├─ By Author
   ├─ By Subject
   └─ By Publication Date
      ↓
Apply strategy algorithm
      ↓
Filter catalog
      ↓
Return results
```

---

## 7. Key Design Decisions

### 7.1 Why Singleton for Library?
- **Single source of truth**: One catalog, one set of members
- **Resource management**: Centralized service coordination
- **Thread-safe**: Ensures consistency in concurrent access

### 7.2 Why Factory Pattern?
- **Validation**: Centralized input validation
- **Consistency**: Objects created with correct initialization
- **Flexibility**: Easy to add new object types

### 7.3 Why Observer Pattern for Notifications?
- **Decoupling**: Notification logic separate from business logic
- **Extensibility**: Easy to add new notification channels
- **Real-time**: Automatic notifications on events

### 7.4 Why Strategy Pattern for Search?
- **Flexibility**: Runtime algorithm selection
- **Open/Closed**: Add new search criteria without modification
- **Testability**: Each strategy testable independently

### 7.5 Why separate Book and BookItem?
- **Conceptual clarity**: Book = metadata, BookItem = physical copy
- **Multiple copies**: Same book can have many physical copies
- **Independent tracking**: Each copy tracked separately

---

## 8. Testing Strategy

### Unit Tests
- Factory methods validation
- Service business logic
- State transitions
- Fine calculations

### Integration Tests
- Complete borrowing workflow
- Reservation system
- Notification delivery
- Search functionality

### Edge Cases Covered
- Borrowing limit exceeded
- Overdue books
- Expired reservations
- Maximum renewals
- Concurrent access (thread-safety)

---

## 9. Performance Considerations

### Indexing
```java
private final Map<String, List<Book>> titleIndex;
private final Map<String, List<Book>> authorIndex;
```
- O(1) lookups for common searches
- Trade-off: Memory vs Speed

### Lazy Loading
- Book items loaded only when needed
- Transaction history paginated in production

### Thread Safety
- Singleton with double-checked locking
- Immutable value objects (Address)
- Synchronized critical sections

---

## 10. Future Enhancements

### Database Integration
- Replace in-memory lists with JPA/Hibernate
- Transaction management
- Connection pooling

### REST API
- Spring Boot integration
- RESTful endpoints
- JWT authentication

### Async Processing
- Queue-based notifications (RabbitMQ, Kafka)
- Background fine calculation
- Scheduled tasks for overdue checks

### Caching
- Redis for frequently accessed data
- Cache invalidation strategy
- Session management

---

**Document Version**: 1.0  
**Last Updated**: 2026  
**Author**: Library Management System Team
