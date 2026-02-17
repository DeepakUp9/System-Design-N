# Library Management System - Production Ready Implementation

## 📋 Overview

A comprehensive, production-ready Library Management System implementation in Java that demonstrates professional software engineering practices, design patterns, and SOLID principles. This system was built following Low-Level Design (LLD) specifications with complete functional implementations—no stub methods or empty implementations.

## 🎯 Key Features

### Core Functionality
- **Book Management**: Complete catalog with multiple copies per book
- **User Management**: Members and Librarians with role-based access
- **Borrowing System**: Check-out/return with due date management
- **Reservation System**: FIFO queue for unavailable books
- **Renewal System**: Policy-based renewal with limits
- **Fine Management**: Automatic calculation and payment processing
- **Search Functionality**: Multi-criteria search (title, author, subject, date)
- **Notification System**: Multi-channel alerts (Email, SMS)

### Requirements Fulfilled (R1-R14)
✅ **R1**: Transaction logging and history  
✅ **R2**: Unique book IDs with physical location tracking  
✅ **R3**: Complete book metadata (ISBN, title, author, subject, date)  
✅ **R4**: Multiple physical copies per book  
✅ **R5**: User types (Librarian, Member)  
✅ **R6**: Unique library cards for all users  
✅ **R7**: Maximum 10 books per member  
✅ **R8**: 15-day borrowing period  
✅ **R9**: One reservation per book item  
✅ **R10**: Complete transaction tracking  
✅ **R11**: Renewal with policy limits  
✅ **R12**: Automated notifications  
✅ **R13**: Reserve unavailable books  
✅ **R14**: Multi-criteria search  

## 🏗️ Architecture & Design Patterns

### Design Patterns Implemented

#### 1. **Singleton Pattern** - Library Class
```java
// Thread-safe singleton using double-checked locking
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
```

#### 2. **Factory Pattern** - Object Creation
- `UserFactory`: Creates Member and Librarian objects
- `BookFactory`: Creates Book and BookItem objects
- `LibraryFactory`: Creates supporting objects (Author, Rack, Address)

#### 3. **Observer Pattern** - Notification System
```java
interface NotificationObserver {
    void update(Member member, String subject, String message);
}

// Concrete observers: EmailNotificationObserver, SMSNotificationObserver
// Subject: NotificationService manages and notifies all observers
```

#### 4. **Strategy Pattern** - Search Algorithms
```java
interface SearchStrategy {
    List<Book> search(List<Book> catalog, String searchTerm);
}

// Concrete strategies: SearchByTitleStrategy, SearchByAuthorStrategy,
// SearchBySubjectStrategy, SearchByPublicationDateStrategy
```

#### 5. **State Pattern** - Book Status Management
- BookStatus enum with transitions
- State validation in BookItem methods

#### 6. **Composition Pattern**
- `Book` → `BookItem` (strong)
- `User` → `LibraryCard` (strong)

## 🔨 SOLID Principles

### Single Responsibility Principle (SRP)
Each class has one well-defined responsibility:
- `BorrowingService`: Handles only borrowing operations
- `ReservationService`: Handles only reservation operations
- `FineService`: Handles only fine calculations

### Open/Closed Principle (OCP)
- Open for extension through interfaces
- Closed for modification in core functionality
- New search strategies can be added without modifying Catalog

### Liskov Substitution Principle (LSP)
- `Member` and `Librarian` can substitute `User` anywhere
- All subclasses honor the contracts of their parent classes

### Interface Segregation Principle (ISP)
- Focused interfaces: `SearchStrategy`, `NotificationObserver`
- Clients depend only on methods they use

### Dependency Inversion Principle (DIP)
- Services depend on abstractions (interfaces)
- High-level modules don't depend on low-level modules

## 📦 Project Structure

```
src/main/java/com/library/
├── enums/
│   ├── AccountStatus.java
│   ├── BookFormat.java
│   ├── BookStatus.java
│   ├── CardStatus.java
│   ├── FineStatus.java
│   ├── ReservationStatus.java
│   └── TransactionType.java
├── exceptions/
│   ├── BookNotAvailableException.java
│   ├── BookReservedException.java
│   ├── BorrowLimitExceededException.java
│   ├── LibraryException.java
│   ├── OutstandingFinesException.java
│   └── RenewalLimitExceededException.java
├── factories/
│   ├── BookFactory.java
│   ├── LibraryFactory.java
│   └── UserFactory.java
├── interfaces/
│   ├── NotificationObserver.java
│   ├── NotificationSubject.java
│   └── SearchStrategy.java
├── models/
│   ├── Address.java
│   ├── Author.java
│   ├── Book.java
│   ├── BookItem.java
│   ├── BookLending.java
│   ├── BookReservation.java
│   ├── Catalog.java
│   ├── Fine.java
│   ├── Librarian.java
│   ├── Library.java
│   ├── LibraryCard.java
│   ├── Member.java
│   ├── Rack.java
│   └── User.java
├── observers/
│   ├── EmailNotificationObserver.java
│   └── SMSNotificationObserver.java
├── services/
│   ├── BorrowingService.java
│   ├── FineService.java
│   ├── NotificationService.java
│   ├── RenewalService.java
│   ├── ReservationService.java
│   ├── SearchByAuthorStrategy.java
│   ├── SearchByPublicationDateStrategy.java
│   ├── SearchBySubjectStrategy.java
│   └── SearchByTitleStrategy.java
└── LibraryManagementSystemDemo.java
```

## 🚀 Running the Application

### Prerequisites
- Java 8 or higher
- No external dependencies required (pure Java implementation)

### Compilation
```bash
cd /Users/deepakkumar/Desktop/LibraryManagementSystem/src/main/java
javac com/library/LibraryManagementSystemDemo.java
```

### Execution
```bash
java com.library.LibraryManagementSystemDemo
```

### Expected Output
The demo will execute the following scenarios:
1. Library initialization (Singleton)
2. Infrastructure setup (Racks)
3. Book catalog population (Factory Pattern)
4. User registration (Members & Librarians)
5. Search operations (Strategy Pattern)
6. Borrowing workflow
7. Reservation system (FIFO queue)
8. Renewal attempts
9. Return and fine management
10. Advanced search demonstrations
11. Final statistics report

## 💡 Key Implementation Highlights

### No Stub Methods
Every method has complete, functional implementation:
- ✅ Full borrowing logic with validation
- ✅ Complete reservation queue management
- ✅ Actual fine calculation and payment processing
- ✅ Real notification sending (console-based simulation)
- ✅ Comprehensive search algorithms

### Thread Safety
- Singleton Library instance is thread-safe
- Uses volatile keyword and double-checked locking
- Immutable value objects where appropriate

### Error Handling
- Custom exception hierarchy
- Comprehensive validation at every step
- Clear error messages

### Business Logic
- Policy enforcement (max books, borrowing period)
- State management with validation
- Transaction history maintenance

## 📊 Class Relationships

### Composition (Strong)
- `Library` ⬥→ `BookItem`
- `Book` ⬥→ `BookItem`
- `User` ⬥→ `LibraryCard`

### Aggregation (Weak)
- `Catalog` ◇→ `Book`

### Association
- `Rack` ←→ `BookItem` (Two-way)
- `Member` → `BookItem` (One-way)
- `BookReservation` → `BookItem` (One-way)

### Inheritance
- `User` ← `Member`
- `User` ← `Librarian`

## 🎓 Learning Resources

This implementation demonstrates concepts suitable for:
- Software Engineering interviews
- Low-Level Design (LLD) preparation
- Design Pattern practice
- SOLID principles application
- Clean Code architecture

## 📝 Code Quality

### Principles Followed
- **Clean Code**: Meaningful names, small methods, clear structure
- **DRY**: Don't Repeat Yourself
- **KISS**: Keep It Simple, Stupid
- **YAGNI**: You Aren't Gonna Need It
- **Separation of Concerns**: Clear boundaries between layers

### Documentation
- Comprehensive JavaDoc comments
- Design pattern annotations
- SOLID principle markers
- Requirement mapping (R1-R14)

## 🔄 Future Enhancements

Possible extensions (demonstrating OCP):
- Add new notification channels (Push, In-app)
- Implement new search strategies (ISBN, Publisher)
- Add persistence layer (Database integration)
- Implement authentication/authorization
- Add REST API endpoints
- Implement async notification processing

## 👥 Author

**Library Management System Team**
- Version: 1.0
- Date: 2026
- License: Educational Use

## 📜 License

This is an educational project demonstrating professional software engineering practices. Free to use for learning purposes.

---

**Built with ❤️ following Clean Code, SOLID Principles, and Design Patterns**
