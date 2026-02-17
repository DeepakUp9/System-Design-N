# 🚀 Quick Start Guide - Library Management System

## ⚡ Get Started in 3 Steps

### Step 1: Navigate to Project
```bash
cd /Users/deepakkumar/Desktop/LibraryManagementSystem
```

### Step 2: Run the Demo
```bash
./compile-and-run.sh
```

### Step 3: Observe the Output
Watch as the system demonstrates:
- Factory pattern creating objects
- Singleton library initialization
- Observer pattern sending notifications
- Strategy pattern executing searches
- Complete borrowing workflow
- Reservation system
- Fine management

---

## 🎯 What You'll See

### Console Output Structure

```
================================================================================
🏛️  LIBRARY MANAGEMENT SYSTEM - PRODUCTION READY IMPLEMENTATION
================================================================================
Demonstrating: Factory, Singleton, Observer, Strategy Design Patterns
Following: SOLID Principles & Clean Code Architecture
================================================================================

📌 STEP 1: Initializing Library (Singleton Pattern)
--------------------------------------------------------------------------------
✅ Singleton verified: true
Library initialized: City Central Library
Location: 123 Main Street, New York, NY 10001, USA

📌 STEP 2: Setting up Library Infrastructure
--------------------------------------------------------------------------------
✅ Added 3 racks to library
   - Rack{rackNumber='A-001', location='Floor 1, Section A', shelf=1}
   - Rack{rackNumber='A-002', location='Floor 1, Section A', shelf=2}
   - Rack{rackNumber='B-001', location='Floor 2, Section B', shelf=1}

📌 STEP 3: Adding Books to Catalog (Factory Pattern)
--------------------------------------------------------------------------------
✅ Added: Clean Code: A Handbook of Agile Software Craftsmanship (3 copies)
✅ Added: Design Patterns: Elements of Reusable Object-Oriented Software (2 copies)
✅ Added: The Pragmatic Programmer: Your Journey to Mastery (2 copies)
✅ Added: Effective Java (2 copies)

Total books in catalog: 4
Total available copies: 9

✅ Member registered: Alice Johnson (Card: LC-...)
✅ Member registered: Bob Smith (Card: LC-...)
✅ Librarian registered: Sarah Williams (Employee ID: EMP-...)

📌 STEP 5: Search Functionality (Strategy Pattern)
--------------------------------------------------------------------------------
🔍 Searching by title 'Clean Code':
   - Clean Code: A Handbook of Agile Software Craftsmanship

🔍 Searching by author 'Martin':
   - Clean Code: A Handbook of Agile Software Craftsmanship by Robert C. Martin

📌 STEP 6: Borrowing Workflow
--------------------------------------------------------------------------------
Alice wants to borrow: Clean Code: A Handbook of Agile Software Craftsmanship
Available copies: 3
Alice's current borrowed books: 0

✅ Book borrowed successfully:
   Member: Alice Johnson
   Book: Clean Code: A Handbook of Agile Software Craftsmanship
   Due Date: 2026-02-18
   Book Item ID: BI-...

================================================================================
📧 EMAIL NOTIFICATION SENT
================================================================================
To: alice@email.com
Subject: Book Due Date Reminder
Message: Your book is due in 3 days...
================================================================================

📌 STEP 7: Reservation System (FIFO Queue)
--------------------------------------------------------------------------------
Bob wants to reserve: Clean Code: A Handbook of Agile Software Craftsmanship
✅ Book reserved successfully

📌 STEP 9: Return and Fine Management
--------------------------------------------------------------------------------
Alice is returning: Clean Code
📢 Notification sent to next member in reservation queue

================================================================================
📊 LIBRARY STATISTICS
================================================================================
Total Books in Catalog: 4
Total Book Copies Available: 9
Total Books Borrowed: 0
Total Members: 2
Total Librarians: 1
================================================================================

✅ DESIGN PATTERNS IMPLEMENTED:
   1. Singleton Pattern
   2. Factory Pattern
   3. Observer Pattern
   4. Strategy Pattern
   5. State Pattern
   6. Composition Pattern

✅ SOLID PRINCIPLES APPLIED:
   S - Single Responsibility
   O - Open/Closed
   L - Liskov Substitution
   I - Interface Segregation
   D - Dependency Inversion
```

---

## 📚 Main Components

### 1. **Library (Singleton)**
- Single instance across application
- Manages all services and entities
- Thread-safe implementation

### 2. **Factories**
- `UserFactory` → Creates Members & Librarians
- `BookFactory` → Creates Books & BookItems
- `LibraryFactory` → Creates Authors, Racks, Addresses

### 3. **Services**
- `BorrowingService` → Handle check-out/return
- `ReservationService` → FIFO queue management
- `RenewalService` → Renewal with policy validation
- `FineService` → Calculate and manage fines
- `NotificationService` → Multi-channel notifications

### 4. **Observers (Notification Channels)**
- `EmailNotificationObserver` → Email notifications
- `SMSNotificationObserver` → SMS notifications
- Easily extensible for new channels

### 5. **Search Strategies**
- `SearchByTitleStrategy`
- `SearchByAuthorStrategy`
- `SearchBySubjectStrategy`
- `SearchByPublicationDateStrategy`

---

## 🎮 Interactive Demo Scenarios

The demo automatically executes these workflows:

### Scenario 1: Member Borrows Book
```
Alice → Wants "Clean Code"
System → Check eligibility (✅ passes)
System → Check availability (✅ available)
System → Checkout book
System → Send notifications (Email + SMS)
Result → Book borrowed, due in 15 days
```

### Scenario 2: Reservation When Unavailable
```
Bob → Wants "Clean Code" (borrowed by Alice)
System → Book not available
Bob → Reserves the book
System → Add to FIFO queue
Result → Bob will be notified when available
```

### Scenario 3: Return with Reservation
```
Alice → Returns "Clean Code"
System → Process reservation queue
System → Notify Bob (next in queue)
Bob → Has 24 hours to collect
Result → Book status changes to RESERVED
```

### Scenario 4: Search Operations
```
User → Search by title "Clean"
Strategy → SearchByTitleStrategy executes
Result → Returns matching books

User → Search by author "Martin"
Strategy → SearchByAuthorStrategy executes
Result → Returns author's books
```

---

## 🔍 Key Features to Observe

### 1. Factory Pattern in Action
Watch objects being created with validation:
```
✅ Member registered: Alice Johnson (Card: LC-1234567890-ABCD1234)
```

### 2. Observer Pattern Notifications
See automatic notifications sent:
```
📧 EMAIL NOTIFICATION SENT
To: alice@email.com
Subject: Book Due Date Reminder
```

### 3. Strategy Pattern Flexibility
Different search algorithms applied:
```
🔍 Searching by title 'Clean Code'
🔍 Searching by author 'Martin'
🔍 Searching by subject 'Software Engineering'
```

### 4. State Management
Book status transitions:
```
AVAILABLE → BORROWED → RESERVED → AVAILABLE
```

### 5. Business Rules Enforcement
```
⚠️  Renewal failed: Cannot renew - book is reserved by another member
💰 Fine issued: $5.00 (5 days × $1.00)
```

---

## 📖 Understanding the Code

### Quick Code Tour

#### 1. Start with Main Demo
```bash
src/main/java/com/library/LibraryManagementSystemDemo.java
```
See complete workflow demonstration

#### 2. Explore Singleton
```bash
src/main/java/com/library/models/Library.java
```
Thread-safe singleton implementation

#### 3. Check Factory Pattern
```bash
src/main/java/com/library/factories/UserFactory.java
```
Object creation with validation

#### 4. Review Observer Pattern
```bash
src/main/java/com/library/services/NotificationService.java
src/main/java/com/library/observers/EmailNotificationObserver.java
```
Multi-channel notification system

#### 5. Examine Strategy Pattern
```bash
src/main/java/com/library/services/SearchByTitleStrategy.java
```
Interchangeable search algorithms

---

## 🎯 Common Questions

### Q: Why is everything in one demo file?
**A**: For easy demonstration. In production, this would be split into controllers, services, and repositories.

### Q: Where's the database?
**A**: This is an in-memory implementation focusing on design patterns and logic. Easy to add JPA/Hibernate later.

### Q: Can I modify the demo?
**A**: Absolutely! The code is clean and extensible. Try adding:
- New search strategies
- Additional notification channels
- Different member types
- More validation rules

### Q: How do I test specific features?
**A**: Comment out sections in `LibraryManagementSystemDemo.main()` to focus on specific workflows.

---

## 🛠️ Troubleshooting

### Compilation Error?
```bash
# Ensure you're in the right directory
pwd
# Should show: /Users/deepakkumar/Desktop/LibraryManagementSystem

# Check Java version
java -version
# Need Java 8 or higher
```

### Permission Denied?
```bash
chmod +x compile-and-run.sh
```

### Want to see specific output?
Edit `LibraryManagementSystemDemo.java` and run specific demo sections.

---

## 📚 Next Steps

### 1. Read Documentation
- `README.md` - Overview
- `DESIGN_DOCUMENTATION.md` - Detailed design
- `PROJECT_SUMMARY.md` - Complete summary

### 2. Explore Code
- Start with interfaces
- Then factories
- Then services
- Finally the models

### 3. Modify & Experiment
- Add new book
- Create new member
- Try invalid operations
- Add new search strategy

### 4. Extend the System
- Add new user type (e.g., Guest)
- Implement new notification channel
- Add more search criteria
- Create persistence layer

---

## 🎉 Success!

If you see the final statistics and design patterns summary, congratulations! You've successfully run a production-ready Library Management System demonstrating:

✅ 6 Design Patterns  
✅ 5 SOLID Principles  
✅ 14 Requirements (R1-R14)  
✅ Clean Code Architecture  
✅ Professional Implementation  

**Happy Coding! 🚀**

---

*For questions or issues, refer to DESIGN_DOCUMENTATION.md for detailed explanations.*
