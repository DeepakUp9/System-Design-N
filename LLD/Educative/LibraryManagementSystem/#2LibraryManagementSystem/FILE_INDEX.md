# 📑 Library Management System - Complete File Index

## 📄 Documentation Files

| File | Purpose | Lines |
|------|---------|-------|
| `README.md` | Project overview, features, and architecture | ~300 |
| `DESIGN_DOCUMENTATION.md` | Detailed design patterns and principles | ~800 |
| `PROJECT_SUMMARY.md` | Comprehensive project completion summary | ~500 |
| `QUICK_START.md` | Quick start guide and usage instructions | ~400 |
| `FILE_INDEX.md` | This file - complete file listing | - |
| `compile-and-run.sh` | Shell script to compile and run | ~30 |

---

## 💻 Source Code Files (45 Java Files)

### 📦 Package: `com.library.enums` (7 files)

| File | Description |
|------|-------------|
| `AccountStatus.java` | User account status enumeration (ACTIVE, CLOSED, SUSPENDED, etc.) |
| `BookFormat.java` | Book format types (HARDCOVER, PAPERBACK, EBOOK, etc.) |
| `BookStatus.java` | Book item status (AVAILABLE, BORROWED, RESERVED, etc.) |
| `CardStatus.java` | Library card status (ACTIVE, EXPIRED, BLOCKED, etc.) |
| `FineStatus.java` | Fine payment status (PENDING, PAID, WAIVED) |
| `ReservationStatus.java` | Reservation status (WAITING, PENDING, COMPLETED, etc.) |
| `TransactionType.java` | Transaction types (BORROW, RETURN, RESERVE, RENEW, etc.) |

---

### 🚨 Package: `com.library.exceptions` (7 files)

| File | Purpose |
|------|---------|
| `LibraryException.java` | Base exception class for all library exceptions |
| `BookNotAvailableException.java` | Thrown when book is not available for borrowing |
| `BookReservedException.java` | Thrown when trying to operate on reserved book |
| `BorrowLimitExceededException.java` | Thrown when member exceeds 10 books limit (R7) |
| `OutstandingFinesException.java` | Thrown when member has unpaid fines |
| `RenewalLimitExceededException.java` | Thrown when renewal limit (2) is reached |

---

### 🏭 Package: `com.library.factories` (3 files)

**Design Pattern: Factory Pattern**

| File | Creates | Purpose |
|------|---------|---------|
| `UserFactory.java` | Member, Librarian | Creates user objects with validation |
| `BookFactory.java` | Book, BookItem | Creates book objects with ISBN validation |
| `LibraryFactory.java` | Author, Rack, Address | Creates supporting objects |

**Key Methods**:
- `createMember()` - Validates email, creates Member
- `createLibrarian()` - Validates department, creates Librarian
- `createBook()` - Validates ISBN, creates Book
- `createBookItem()` - Creates physical book copy
- `createMultipleBookItems()` - Bulk creation of book copies

---

### 🔌 Package: `com.library.interfaces` (3 files)

**Design Patterns: Observer, Strategy**

| File | Pattern | Purpose |
|------|---------|---------|
| `NotificationObserver.java` | Observer | Observer interface for notifications |
| `NotificationSubject.java` | Observer | Subject interface for notification system |
| `SearchStrategy.java` | Strategy | Strategy interface for search algorithms |

---

### 🏛️ Package: `com.library.models` (14 files)

**Core domain entities with complete business logic**

#### Value Objects
| File | Type | Description |
|------|------|-------------|
| `Address.java` | Value Object | Immutable address representation |

#### Core Entities
| File | Relationships | Key Responsibilities |
|------|---------------|---------------------|
| `Author.java` | Many-to-Many with Book | Author information and bibliography |
| `Book.java` | One-to-Many with BookItem | Book metadata (R3: ISBN, title, author, subject) |
| `BookItem.java` | Belongs to Book | Physical copy with status (R2: unique ID, location) |
| `User.java` | Abstract base class | Common user functionality |
| `Member.java` | Extends User | Member-specific logic (R7: max 10 books) |
| `Librarian.java` | Extends User | Librarian privileges |
| `LibraryCard.java` | Belongs to User | Unique library card (R6) |
| `Rack.java` | Contains BookItems | Physical location tracking (R2) |

#### Transaction Entities
| File | Purpose | Tracks |
|------|---------|--------|
| `BookLending.java` | Lending records | Who borrowed, when, due date (R10) |
| `BookReservation.java` | Reservation records | FIFO queue, notifications (R9, R13) |
| `Fine.java` | Fine records | Overdue fines, payment status |

#### Aggregate Root & Services
| File | Pattern | Purpose |
|------|---------|---------|
| `Library.java` | Singleton | Central coordination point, manages all services |
| `Catalog.java` | Aggregation | Book collection with search capabilities (R14) |

---

### 👀 Package: `com.library.observers` (2 files)

**Design Pattern: Observer Pattern - Concrete Observers**

| File | Channel | Purpose |
|------|---------|---------|
| `EmailNotificationObserver.java` | Email | Send email notifications (R12) |
| `SMSNotificationObserver.java` | SMS | Send SMS notifications (R12) |

**Notifications Sent**:
- Overdue book alerts
- Reservation available
- Reservation cancelled
- Due date reminders
- Fine payment confirmations

---

### ⚙️ Package: `com.library.services` (9 files)

**SOLID Principle: Single Responsibility - Each service handles one concern**

#### Core Services

| File | Responsibility | Requirements |
|------|----------------|--------------|
| `BorrowingService.java` | Book borrowing & return | R7, R8, R10 |
| `ReservationService.java` | Reservation management | R9, R13 |
| `RenewalService.java` | Book renewal | R11 |
| `FineService.java` | Fine calculation & payment | Automatic fine calculation |
| `NotificationService.java` | Multi-channel notifications | R12 |

**Key Features**:
- `BorrowingService`: Validation, state management, transaction logging
- `ReservationService`: FIFO queue, expiry handling, auto-notification
- `RenewalService`: Policy enforcement (max 2 renewals), validation
- `FineService`: $1/day calculation, payment processing
- `NotificationService`: Observer pattern implementation

#### Search Strategies

**Design Pattern: Strategy Pattern - Interchangeable Algorithms**

| File | Searches By | Algorithm |
|------|-------------|-----------|
| `SearchByTitleStrategy.java` | Title | Case-insensitive substring match |
| `SearchByAuthorStrategy.java` | Author name | Searches all book authors |
| `SearchBySubjectStrategy.java` | Subject | Exact subject matching |
| `SearchByPublicationDateStrategy.java` | Date/Year | Supports both formats |

**Requirement R14**: Allow search by title, author, subject, or publication date

---

### 🎯 Main Application (1 file)

| File | Purpose | Demonstrates |
|------|---------|-------------|
| `LibraryManagementSystemDemo.java` | End-to-end demo | All patterns, workflows, and requirements |

**Demo Sections**:
1. Library initialization (Singleton)
2. Infrastructure setup
3. Book catalog population (Factory)
4. User registration
5. Search operations (Strategy)
6. Borrowing workflow
7. Reservation system (FIFO)
8. Renewal system
9. Return and fine management
10. Advanced search
11. Statistics report

---

## 📊 Statistics Summary

```
Total Files: 50
├── Java Files: 45
│   ├── Enums: 7
│   ├── Exceptions: 7
│   ├── Factories: 3
│   ├── Interfaces: 3
│   ├── Models: 14
│   ├── Observers: 2
│   ├── Services: 9
│   └── Main: 1
└── Documentation: 6
    ├── Markdown: 5
    └── Shell Script: 1

Estimated Lines of Code: 6,300+
```

---

## 🎨 Design Patterns by File

### Singleton Pattern
- ✅ `Library.java` - Thread-safe singleton

### Factory Pattern
- ✅ `UserFactory.java` - User creation
- ✅ `BookFactory.java` - Book creation
- ✅ `LibraryFactory.java` - Supporting object creation

### Observer Pattern
- ✅ `NotificationObserver.java` - Observer interface
- ✅ `NotificationSubject.java` - Subject interface
- ✅ `NotificationService.java` - Concrete subject
- ✅ `EmailNotificationObserver.java` - Concrete observer
- ✅ `SMSNotificationObserver.java` - Concrete observer

### Strategy Pattern
- ✅ `SearchStrategy.java` - Strategy interface
- ✅ `Catalog.java` - Context
- ✅ `SearchByTitleStrategy.java` - Concrete strategy
- ✅ `SearchByAuthorStrategy.java` - Concrete strategy
- ✅ `SearchBySubjectStrategy.java` - Concrete strategy
- ✅ `SearchByPublicationDateStrategy.java` - Concrete strategy

### State Pattern
- ✅ `BookStatus.java` - State enumeration
- ✅ `BookItem.java` - State management

### Composition Pattern
- ✅ `Book.java` → `BookItem.java` - Strong ownership
- ✅ `User.java` → `LibraryCard.java` - Strong ownership

---

## 📋 Requirements Coverage by File

### R1: Transaction Logging
- ✅ `BookLending.java`
- ✅ `BookReservation.java`
- ✅ `Fine.java`
- ✅ `BorrowingService.java`

### R2: Unique ID & Location
- ✅ `BookItem.java` - Unique barcode generation
- ✅ `Rack.java` - Physical location

### R3: Book Metadata
- ✅ `Book.java` - ISBN, title, author, subject, date

### R4: Multiple Copies
- ✅ `Book.java` - Contains multiple BookItems
- ✅ `BookItem.java` - Individual copy

### R5-R6: User Types & Cards
- ✅ `User.java` - Abstract base
- ✅ `Member.java` - Member type
- ✅ `Librarian.java` - Librarian type
- ✅ `LibraryCard.java` - Unique card

### R7-R8: Borrowing Limits
- ✅ `Member.java` - Max 10 books constant
- ✅ `BorrowingService.java` - 15-day period

### R9: Single Reservation
- ✅ `BookReservation.java`
- ✅ `BookItem.java` - FIFO queue

### R10: Transaction Recording
- ✅ All transaction entities
- ✅ All service classes

### R11: Renewal
- ✅ `RenewalService.java` - Complete renewal logic

### R12: Notifications
- ✅ `NotificationService.java`
- ✅ All observer classes

### R13: Reserve Unavailable
- ✅ `ReservationService.java`

### R14: Multi-Criteria Search
- ✅ `Catalog.java`
- ✅ All search strategy classes

---

## 🔍 Quick File Lookup

### Need to Understand...

**Design Patterns?**
→ Start with: `Library.java`, `UserFactory.java`, `NotificationService.java`, `Catalog.java`

**Business Logic?**
→ Start with: `BorrowingService.java`, `ReservationService.java`, `Member.java`

**Entity Relationships?**
→ Start with: `Book.java`, `BookItem.java`, `User.java`

**Workflows?**
→ Start with: `LibraryManagementSystemDemo.java`

**Validation & Errors?**
→ Start with: Exception package files

---

## 📚 Reading Order for Learning

### Beginner Path
1. `README.md` - Overview
2. `QUICK_START.md` - Get it running
3. `LibraryManagementSystemDemo.java` - See it in action
4. Enum classes - Understand constants
5. `Book.java` and `BookItem.java` - Core entities

### Intermediate Path
6. `User.java`, `Member.java`, `Librarian.java` - Inheritance
7. Factory classes - Object creation
8. `BorrowingService.java` - Business logic
9. Exception classes - Error handling

### Advanced Path
10. `Library.java` - Singleton implementation
11. `NotificationService.java` + Observers - Observer pattern
12. `Catalog.java` + Search strategies - Strategy pattern
13. `DESIGN_DOCUMENTATION.md` - Deep dive

---

## 🎯 File Purpose Quick Reference

| Need to... | File to Check |
|-----------|---------------|
| Create objects | `UserFactory`, `BookFactory`, `LibraryFactory` |
| Borrow/Return | `BorrowingService.java` |
| Reserve books | `ReservationService.java` |
| Renew books | `RenewalService.java` |
| Calculate fines | `FineService.java` |
| Send notifications | `NotificationService.java` |
| Search books | `Catalog.java` + search strategies |
| Manage library | `Library.java` |
| Handle errors | Exception package |
| See it working | `LibraryManagementSystemDemo.java` |

---

**Last Updated**: February 2026  
**Total Files**: 50  
**Total Java Classes**: 45  
**Status**: ✅ Complete & Production-Ready
