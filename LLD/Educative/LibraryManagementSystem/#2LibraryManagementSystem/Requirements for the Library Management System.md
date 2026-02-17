# Requirements for the Library Management System

Learn about all requirements of the library management system.

In this lesson, we will list the requirements of our library management system. This is a crucial step, as requirements define the scope of a problem. Getting them right from the interviewer and understanding them well will make the system-design process smooth and easy.

We'll use the notational convention to identify each requirement with a unique label "Rn," where "R" is short for Requirement and "n" is a natural number.

## Requirement collection

For LMS (library management system), the requirements have been defined below:

* R1: The system should store information about books and members, and maintain a complete log of all book borrowing, return, reservation, and renewal transactions.
* R2: Every book must have a unique identification number and detailed information, including rack/location in the library.
* R3: Each book should include ISBN, title, author name, subject, and publication date.
* R4: A book can have multiple copies; each physical copy is a distinct book item with a unique ID.
* R5: The system must support two types of users: librarian and member, each with a library card and unique card number.
* R6: Every user must have a library card with a unique card number.
* R7: Members can borrow a maximum of 10 books at a time.
* R8: A member can borrow a book for a maximum of 15 days.
* R9: Only one member can reserve each book item at a time.
* R10: The system must record who issued, reserved, or renewed a book item and on which date.
* R11: The system must allow members to renew borrowed books, according to policy limits.
* R12: The system should notify members if a book is not returned by the due date or when a reserved book becomes available.
* R13: If a book is unavailable, a member should be able to reserve it for when it becomes available.
* R14: The system should allow users to search for books by title, author, subject, or publication date.

<span style="background-color: yellow; color: blue;">If you want to understand more in depth, <a href="./deapth/requirements.md">click here</a></span>


====================== Latest pattern ==================================================================

# Library Management System - Requirements Analysis & Design Approach

> **Comprehensive LLD Requirements Analysis with Design Implications**

---

## 📋 Requirements Overview

### Notational Convention
Each requirement is labeled as **"Rn"** where:
- **R** = Requirement
- **n** = Natural number (unique identifier)

---

## 🎯 Functional & Operational Requirements

---

### R1: Transaction Logging 📝

**Requirement:**
> The system should store information about books and members, and maintain a complete log of all book borrowing, return, reservation, and renewal transactions.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Data Persistence** | Database with proper relationships |
| **Transaction Types** | Borrow, Return, Reserve, Renew |
| **Audit Trail** | Immutable transaction log |
| **Historical Data** | Never delete, only archive |

#### Key Classes Affected:
```java
class TransactionLog {
    private String transactionId;
    private TransactionType type;
    private Member member;
    private BookItem bookItem;
    private LocalDate transactionDate;
    private LocalDate dueDate;
    private TransactionStatus status;
    
    // Immutable once created
    public TransactionLog(TransactionType type, Member member, BookItem bookItem) {
        this.transactionId = UUID.randomUUID().toString();
        this.type = type;
        this.member = member;
        this.bookItem = bookItem;
        this.transactionDate = LocalDate.now();
        this.status = TransactionStatus.ACTIVE;
    }
}

enum TransactionType {
    BORROW,
    RETURN,
    RESERVE,
    RENEW,
    FINE_PAYMENT
}

enum TransactionStatus {
    ACTIVE,
    COMPLETED,
    CANCELLED,
    OVERDUE
}

class Library {
    private List<Book> books;
    private List<Member> members;
    private List<TransactionLog> transactionHistory;
    
    void logTransaction(TransactionLog transaction) {
        transactionHistory.add(transaction);
        // Persist to database
        database.save(transaction);
    }
    
    List<TransactionLog> getTransactionHistory(Member member) {
        return transactionHistory.stream()
            .filter(t -> t.getMember().equals(member))
            .collect(Collectors.toList());
    }
}
```

#### Design Pattern:
- **Repository Pattern** for data access
- **Command Pattern** for transaction logging

---

### R2: Unique Book Identification 🏷️

**Requirement:**
> Every book must have a unique identification number and detailed information, including rack/location in the library.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Unique ID** | UUID or auto-increment |
| **Location Tracking** | Rack number, shelf, section |
| **Physical Management** | Support library layout |
| **Quick Retrieval** | Location-based search |

#### Key Design:
```java
class BookItem {
    private String bookItemId; // Unique barcode
    private Book book; // Reference to book metadata
    private BookLocation location;
    private BookItemStatus status;
    private LocalDate dateOfPurchase;
    private double price;
    
    public BookItem(Book book, String rackNumber, int shelfNumber) {
        this.bookItemId = generateUniqueId();
        this.book = book;
        this.location = new BookLocation(rackNumber, shelfNumber);
        this.status = BookItemStatus.AVAILABLE;
        this.dateOfPurchase = LocalDate.now();
    }
    
    private String generateUniqueId() {
        return "BK" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8);
    }
}

class BookLocation {
    private String rackNumber;
    private int shelfNumber;
    private String section; // e.g., "Science", "Fiction"
    
    public BookLocation(String rackNumber, int shelfNumber) {
        this.rackNumber = rackNumber;
        this.shelfNumber = shelfNumber;
    }
    
    @Override
    public String toString() {
        return "Rack: " + rackNumber + ", Shelf: " + shelfNumber;
    }
}

enum BookItemStatus {
    AVAILABLE,
    BORROWED,
    RESERVED,
    LOST,
    DAMAGED,
    UNDER_REPAIR
}
```

#### Location System Example:
```
Section: Science Fiction
Rack: SF-03
Shelf: 2
Position: A5

Full Location: SF-03-2-A5
```

---

### R3: Book Metadata 📚

**Requirement:**
> Each book should include the ISBN, title, author name, subject, and publication date.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **ISBN** | Unique identifier (10 or 13 digits) |
| **Multiple Authors** | List of authors |
| **Subject/Category** | Hierarchical classification |
| **Search Indexing** | All fields searchable |

#### Key Design:
```java
class Book {
    private String ISBN; // International Standard Book Number
    private String title;
    private List<String> authors;
    private String subject;
    private LocalDate publicationDate;
    private String publisher;
    private int numberOfPages;
    private String language;
    
    public Book(String ISBN, String title, List<String> authors, String subject) {
        validateISBN(ISBN);
        this.ISBN = ISBN;
        this.title = title;
        this.authors = authors;
        this.subject = subject;
    }
    
    private void validateISBN(String ISBN) {
        // ISBN-10: 10 digits
        // ISBN-13: 13 digits
        String cleaned = ISBN.replaceAll("-", "");
        if (cleaned.length() != 10 && cleaned.length() != 13) {
            throw new InvalidISBNException("ISBN must be 10 or 13 digits");
        }
    }
    
    public String getISBN() {
        return ISBN;
    }
    
    public String getTitle() {
        return title;
    }
    
    public List<String> getAuthors() {
        return new ArrayList<>(authors); // Defensive copy
    }
}
```

#### Example:
```java
Book book = new Book(
    "978-0-13-468599-1",
    "Clean Code",
    Arrays.asList("Robert C. Martin"),
    "Software Engineering"
);
book.setPublicationDate(LocalDate.of(2008, 8, 1));
book.setPublisher("Prentice Hall");
book.setNumberOfPages(464);
book.setLanguage("English");
```

---

### R4: Multiple Book Copies 📖

**Requirement:**
> A book can have multiple copies; each physical copy is a distinct book item with a unique ID.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Book vs BookItem** | Separate entities |
| **One-to-Many** | One Book → Many BookItems |
| **Individual Tracking** | Each copy tracked independently |
| **Availability** | Aggregate from all copies |

#### Key Design:
```java
class Book {
    private String ISBN;
    private String title;
    private List<String> authors;
    private String subject;
    private List<BookItem> copies; // Multiple physical copies
    
    void addCopy(BookItem bookItem) {
        copies.add(bookItem);
    }
    
    int getTotalCopies() {
        return copies.size();
    }
    
    int getAvailableCopies() {
        return (int) copies.stream()
            .filter(item -> item.getStatus() == BookItemStatus.AVAILABLE)
            .count();
    }
    
    BookItem getAvailableCopy() {
        return copies.stream()
            .filter(item -> item.getStatus() == BookItemStatus.AVAILABLE)
            .findFirst()
            .orElse(null);
    }
}

class BookItem {
    private String bookItemId; // Unique for each copy
    private Book book; // Reference to book metadata
    private BookLocation location;
    private BookItemStatus status;
    
    // Each copy is independent
}
```

#### Relationship:
```
Book: "Clean Code" (ISBN: 978-0-13-468599-1)
    ├─ BookItem #1 (BK001) - Available, Rack A-5
    ├─ BookItem #2 (BK002) - Borrowed, Rack A-5
    └─ BookItem #3 (BK003) - Available, Rack A-6

Total Copies: 3
Available Copies: 2
```

---

### R5: User Types 👥

**Requirement:**
> The system must support two types of users: librarian and member, each with a library card and a unique card number.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Inheritance** | User as base class |
| **Polymorphism** | Different behaviors |
| **Library Card** | Common to all users |
| **Permissions** | Role-based access |

#### Key Design:
```java
abstract class User {
    protected String userId;
    protected String name;
    protected String email;
    protected String phone;
    protected LibraryCard libraryCard;
    
    public User(String name, String email) {
        this.userId = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.libraryCard = new LibraryCard(this);
    }
    
    abstract Role getRole();
    abstract boolean canBorrowBooks();
    abstract boolean canAddBooks();
}

class Librarian extends User {
    private String employeeId;
    private double salary;
    
    @Override
    Role getRole() {
        return Role.LIBRARIAN;
    }
    
    @Override
    boolean canBorrowBooks() {
        return true;
    }
    
    @Override
    boolean canAddBooks() {
        return true;
    }
    
    // Librarian-specific operations
    void addBook(Book book) { }
    void removeBook(String bookId) { }
    void issueFine(Member member, double amount) { }
    void addMember(Member member) { }
}

class Member extends User {
    private String membershipId;
    private LocalDate membershipDate;
    private MembershipType membershipType;
    private int borrowedBooksCount;
    private double outstandingFines;
    
    @Override
    Role getRole() {
        return Role.MEMBER;
    }
    
    @Override
    boolean canBorrowBooks() {
        return borrowedBooksCount < getMaxBooksAllowed() 
            && outstandingFines == 0;
    }
    
    @Override
    boolean canAddBooks() {
        return false;
    }
    
    int getMaxBooksAllowed() {
        return 10; // From R7
    }
}

enum Role {
    LIBRARIAN,
    MEMBER,
    ADMIN
}

enum MembershipType {
    STUDENT,
    FACULTY,
    PUBLIC
}
```

---

### R6: Library Card System 💳

**Requirement:**
> Every user must have a library card with a unique card number.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Unique Card Number** | Auto-generated |
| **Card Status** | Active, Suspended, Expired |
| **Barcode** | For quick scanning |
| **Issue/Expiry Date** | Validity period |

#### Key Design:
```java
class LibraryCard {
    private String cardNumber;
    private User user;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private CardStatus status;
    private String barcode;
    
    public LibraryCard(User user) {
        this.cardNumber = generateCardNumber();
        this.user = user;
        this.issueDate = LocalDate.now();
        this.expiryDate = issueDate.plusYears(1);
        this.status = CardStatus.ACTIVE;
        this.barcode = generateBarcode();
    }
    
    private String generateCardNumber() {
        return "LC" + System.currentTimeMillis();
    }
    
    private String generateBarcode() {
        return "BARCODE-" + cardNumber;
    }
    
    boolean isValid() {
        return status == CardStatus.ACTIVE 
            && LocalDate.now().isBefore(expiryDate);
    }
    
    void renew() {
        if (!isExpired()) {
            this.expiryDate = LocalDate.now().plusYears(1);
        }
    }
    
    boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }
}

enum CardStatus {
    ACTIVE,
    SUSPENDED,
    EXPIRED,
    LOST,
    BLOCKED
}
```

---

### R7: Borrowing Limit 📊

**Requirement:**
> Members can borrow a maximum of 10 books at a time.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Limit Enforcement** | Check before borrowing |
| **Counter Management** | Track borrowed count |
| **Validation** | Prevent over-borrowing |
| **Configurable** | Different limits per member type |

#### Key Design:
```java
class Member extends User {
    private int borrowedBooksCount;
    private final int MAX_BOOKS_ALLOWED = 10;
    private List<BookItem> borrowedBooks;
    
    boolean canBorrowMoreBooks() {
        return borrowedBooksCount < MAX_BOOKS_ALLOWED;
    }
    
    void incrementBorrowedCount() {
        if (borrowedBooksCount >= MAX_BOOKS_ALLOWED) {
            throw new BorrowLimitExceededException(
                "Cannot borrow more than " + MAX_BOOKS_ALLOWED + " books"
            );
        }
        borrowedBooksCount++;
    }
    
    void decrementBorrowedCount() {
        if (borrowedBooksCount > 0) {
            borrowedBooksCount--;
        }
    }
    
    int getAvailableBorrowSlots() {
        return MAX_BOOKS_ALLOWED - borrowedBooksCount;
    }
}

class BorrowingService {
    void borrowBook(Member member, BookItem bookItem) {
        // Validation
        if (!member.canBorrowMoreBooks()) {
            throw new BorrowLimitExceededException();
        }
        
        if (!member.canBorrowBooks()) {
            throw new MemberNotEligibleException("Clear fines or reduce books");
        }
        
        if (bookItem.getStatus() != BookItemStatus.AVAILABLE) {
            throw new BookNotAvailableException();
        }
        
        // Borrow operation
        bookItem.setStatus(BookItemStatus.BORROWED);
        bookItem.setBorrowedBy(member);
        bookItem.setBorrowedDate(LocalDate.now());
        bookItem.setDueDate(LocalDate.now().plusDays(15)); // R8
        
        member.incrementBorrowedCount();
        member.addBorrowedBook(bookItem);
        
        // Log transaction
        logTransaction(new TransactionLog(TransactionType.BORROW, member, bookItem));
    }
}
```

---

### R8: Borrowing Period ⏰

**Requirement:**
> A member can borrow a book for 15 days.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Due Date Calculation** | Borrow date + 15 days |
| **Overdue Detection** | Compare with current date |
| **Fine Calculation** | Based on overdue days |
| **Configurable** | Different periods for different item types |

#### Key Design:
```java
class BorrowingService {
    private final int BORROWING_PERIOD_DAYS = 15;
    
    void borrowBook(Member member, BookItem bookItem) {
        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(BORROWING_PERIOD_DAYS);
        
        bookItem.setBorrowedDate(borrowDate);
        bookItem.setDueDate(dueDate);
        
        System.out.println("Book due on: " + dueDate);
    }
}

class BookItem {
    private LocalDate borrowedDate;
    private LocalDate dueDate;
    private Member borrowedBy;
    
    boolean isOverdue() {
        return dueDate != null && LocalDate.now().isAfter(dueDate);
    }
    
    long getOverdueDays() {
        if (!isOverdue()) {
            return 0;
        }
        return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }
}

class FineCalculationService {
    private final double DAILY_FINE_RATE = 1.0;
    
    double calculateFine(BookItem bookItem) {
        if (!bookItem.isOverdue()) {
            return 0.0;
        }
        
        long overdueDays = bookItem.getOverdueDays();
        return overdueDays * DAILY_FINE_RATE;
    }
}
```

---

### R9: Single Reservation 🔖

**Requirement:**
> Only one member can reserve each book item at a time.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Queue Structure** | FIFO for fairness |
| **One Active Reservation** | Per book item |
| **Notification** | Alert when available |
| **Expiry** | Reservation timeout |

#### Key Design:
```java
class BookItem {
    private Member reservedBy;
    private LocalDate reservationDate;
    private LocalDate reservationExpiry;
    
    boolean isReserved() {
        return reservedBy != null;
    }
    
    boolean canBeReserved() {
        return status == BookItemStatus.BORROWED && !isReserved();
    }
}

class ReservationService {
    void reserveBook(Member member, BookItem bookItem) {
        // Validate
        if (!bookItem.canBeReserved()) {
            throw new BookNotReservableException(
                "Book is already reserved or not borrowed"
            );
        }
        
        // Check if member already reserved this book
        if (bookItem.getReservedBy() != null && 
            bookItem.getReservedBy().equals(member)) {
            throw new DuplicateReservationException();
        }
        
        // Reserve
        bookItem.setReservedBy(member);
        bookItem.setReservationDate(LocalDate.now());
        bookItem.setStatus(BookItemStatus.RESERVED);
        
        // Log
        logTransaction(new TransactionLog(TransactionType.RESERVE, member, bookItem));
    }
    
    void cancelReservation(BookItem bookItem) {
        bookItem.setReservedBy(null);
        bookItem.setReservationDate(null);
        bookItem.setStatus(BookItemStatus.BORROWED); // Return to previous state
    }
}
```

---

### R10: Transaction Tracking 📋

**Requirement:**
> The system must record who issued, reserved, or renewed a book item and on which date.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Audit Trail** | Complete transaction history |
| **Timestamp** | All operations logged |
| **Actor Tracking** | Who performed action |
| **Immutability** | Historical data never changed |

#### Key Design:
```java
class TransactionLog {
    private String transactionId;
    private TransactionType type;
    private Member member;
    private Librarian processedBy; // Who performed the action
    private BookItem bookItem;
    private LocalDate transactionDate;
    private LocalDate dueDate;
    private TransactionStatus status;
    private String notes;
    
    // Complete audit information
    public TransactionLog(TransactionType type, Member member, 
                         BookItem bookItem, Librarian processedBy) {
        this.transactionId = UUID.randomUUID().toString();
        this.type = type;
        this.member = member;
        this.bookItem = bookItem;
        this.processedBy = processedBy;
        this.transactionDate = LocalDate.now();
    }
    
    // Getters only - immutable once created
    public String getTransactionId() {
        return transactionId;
    }
    
    public Member getMember() {
        return member;
    }
    
    public Librarian getProcessedBy() {
        return processedBy;
    }
    
    public LocalDate getTransactionDate() {
        return transactionDate;
    }
}

class Library {
    private List<TransactionLog> transactionHistory;
    
    List<TransactionLog> getTransactionsByMember(Member member) {
        return transactionHistory.stream()
            .filter(t -> t.getMember().equals(member))
            .sorted(Comparator.comparing(TransactionLog::getTransactionDate).reversed())
            .collect(Collectors.toList());
    }
    
    List<TransactionLog> getTransactionsByBook(BookItem bookItem) {
        return transactionHistory.stream()
            .filter(t -> t.getBookItem().equals(bookItem))
            .sorted(Comparator.comparing(TransactionLog::getTransactionDate).reversed())
            .collect(Collectors.toList());
    }
    
    List<TransactionLog> getTransactionsByType(TransactionType type) {
        return transactionHistory.stream()
            .filter(t -> t.getType() == type)
            .collect(Collectors.toList());
    }
}
```

---

### R11: Book Renewal 🔄

**Requirement:**
> According to policy limits, the system must allow members to renew borrowed books.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Renewal Limit** | Maximum renewals allowed |
| **Eligibility** | No reservations, no fines |
| **Due Date Extension** | Add renewal period |
| **Transaction Logging** | Record renewal |

#### Key Design:
```java
class RenewalService {
    private final int MAX_RENEWALS = 2;
    private final int RENEWAL_PERIOD_DAYS = 15;
    
    void renewBook(Member member, BookItem bookItem) {
        validateRenewal(member, bookItem);
        
        // Extend due date
        LocalDate currentDueDate = bookItem.getDueDate();
        LocalDate newDueDate = currentDueDate.plusDays(RENEWAL_PERIOD_DAYS);
        bookItem.setDueDate(newDueDate);
        
        // Increment renewal count
        bookItem.incrementRenewalCount();
        
        // Log transaction
        logTransaction(new TransactionLog(TransactionType.RENEW, member, bookItem));
        
        System.out.println("Book renewed. New due date: " + newDueDate);
    }
    
    private void validateRenewal(Member member, BookItem bookItem) {
        // Check if member is borrower
        if (!bookItem.getBorrowedBy().equals(member)) {
            throw new UnauthorizedRenewalException();
        }
        
        // Check if book is reserved
        if (bookItem.isReserved()) {
            throw new BookReservedException("Cannot renew - book is reserved");
        }
        
        // Check renewal limit
        if (bookItem.getRenewalCount() >= MAX_RENEWALS) {
            throw new RenewalLimitExceededException(
                "Maximum " + MAX_RENEWALS + " renewals allowed"
            );
        }
        
        // Check outstanding fines
        if (member.getOutstandingFines() > 0) {
            throw new OutstandingFinesException("Clear fines before renewal");
        }
        
        // Check if overdue
        if (bookItem.isOverdue()) {
            throw new OverdueBookException("Cannot renew overdue book");
        }
    }
}

class BookItem {
    private int renewalCount;
    
    void incrementRenewalCount() {
        this.renewalCount++;
    }
    
    void resetRenewalCount() {
        this.renewalCount = 0;
    }
    
    int getRenewalCount() {
        return renewalCount;
    }
}
```

---

### R12: Notifications 📧

**Requirement:**
> The system should notify members if a book is not returned by the due date or when a reserved book becomes available.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Notification Types** | Overdue, Reservation |
| **Channels** | Email, SMS, App |
| **Scheduling** | Daily checks |
| **Observer Pattern** | Event-driven notifications |

#### Key Design:
```java
interface NotificationService {
    void sendNotification(Notification notification);
}

class EmailNotificationService implements NotificationService {
    @Override
    public void sendNotification(Notification notification) {
        // Send email
        System.out.println("Sending email to: " + notification.getRecipient().getEmail());
        System.out.println("Subject: " + notification.getSubject());
        System.out.println("Message: " + notification.getMessage());
    }
}

class Notification {
    private String notificationId;
    private Member recipient;
    private NotificationType type;
    private String subject;
    private String message;
    private LocalDate sentDate;
    private NotificationStatus status;
    
    public Notification(Member recipient, NotificationType type, String message) {
        this.notificationId = UUID.randomUUID().toString();
        this.recipient = recipient;
        this.type = type;
        this.message = message;
        this.sentDate = LocalDate.now();
        this.status = NotificationStatus.PENDING;
    }
}

enum NotificationType {
    OVERDUE_REMINDER,
    RESERVATION_AVAILABLE,
    DUE_DATE_REMINDER,
    FINE_NOTIFICATION
}

enum NotificationStatus {
    PENDING,
    SENT,
    FAILED,
    READ
}

class NotificationManager {
    private NotificationService emailService;
    private NotificationService smsService;
    
    void sendOverdueReminder(Member member, BookItem bookItem) {
        String message = String.format(
            "Your book '%s' was due on %s. Please return it immediately. " +
            "Fine: $%.2f",
            bookItem.getBook().getTitle(),
            bookItem.getDueDate(),
            calculateFine(bookItem)
        );
        
        Notification notification = new Notification(
            member,
            NotificationType.OVERDUE_REMINDER,
            message
        );
        
        emailService.sendNotification(notification);
    }
    
    void sendReservationAvailableNotification(Member member, BookItem bookItem) {
        String message = String.format(
            "Good news! The book '%s' you reserved is now available. " +
            "Please collect it within 24 hours.",
            bookItem.getBook().getTitle()
        );
        
        Notification notification = new Notification(
            member,
            NotificationType.RESERVATION_AVAILABLE,
            message
        );
        
        emailService.sendNotification(notification);
    }
    
    // Scheduled task - runs daily
    void checkAndSendOverdueNotifications() {
        List<BookItem> overdueBooks = library.getOverdueBooks();
        
        for (BookItem book : overdueBooks) {
            Member member = book.getBorrowedBy();
            sendOverdueReminder(member, book);
        }
    }
}
```

---

### R13: Book Reservation When Unavailable 📌

**Requirement:**
> If a book is unavailable, a member should be able to reserve it for when it becomes available.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Availability Check** | Before borrowing |
| **Queue Management** | FIFO reservation queue |
| **Auto-notification** | When book returned |
| **Hold Period** | Time limit to collect |

#### Key Design:
```java
class BorrowingService {
    void borrowOrReserve(Member member, Book book) {
        // Try to get available copy
        BookItem availableCopy = book.getAvailableCopy();
        
        if (availableCopy != null) {
            // Book available - borrow immediately
            borrowBook(member, availableCopy);
        } else {
            // Book unavailable - reserve
            reservationService.reserveAnyAvailableCopy(member, book);
            System.out.println("All copies are currently borrowed. " +
                "You have been added to the reservation queue.");
        }
    }
}

class ReservationService {
    void reserveAnyAvailableCopy(Member member, Book book) {
        // Find first borrowed copy
        BookItem borrowedCopy = book.getCopies().stream()
            .filter(item -> item.getStatus() == BookItemStatus.BORROWED)
            .findFirst()
            .orElseThrow(() -> new NoBookItemsException());
        
        Reservation reservation = new Reservation(member, borrowedCopy);
        borrowedCopy.addToReservationQueue(reservation);
        
        logTransaction(new TransactionLog(TransactionType.RESERVE, member, borrowedCopy));
    }
}

class BookItem {
    private Queue<Reservation> reservationQueue = new LinkedList<>();
    
    void addToReservationQueue(Reservation reservation) {
        reservationQueue.offer(reservation);
    }
    
    Reservation getNextReservation() {
        return reservationQueue.poll();
    }
    
    boolean hasReservations() {
        return !reservationQueue.isEmpty();
    }
}

class ReturnService {
    void returnBook(Member member, BookItem bookItem) {
        // ... return logic ...
        
        // Check for reservations
        if (bookItem.hasReservations()) {
            Reservation nextReservation = bookItem.getNextReservation();
            nextReservation.setStatus(ReservationStatus.AVAILABLE);
            
            // Notify member
            notificationManager.sendReservationAvailableNotification(
                nextReservation.getMember(),
                bookItem
            );
            
            // Hold for 24 hours
            bookItem.setStatus(BookItemStatus.RESERVED);
            nextReservation.setExpiryDate(LocalDate.now().plusDays(1));
        } else {
            bookItem.setStatus(BookItemStatus.AVAILABLE);
        }
    }
}
```

---

### R14: Book Search 🔍

**Requirement:**
> The system should allow users to search for books by title, author, subject, or publication date.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Multiple Criteria** | Flexible search builder |
| **Search Strategy** | Strategy pattern |
| **Indexing** | Pre-built indexes |
| **Performance** | Optimized queries |

#### Key Design:
```java
interface SearchStrategy {
    List<Book> search(SearchCriteria criteria);
}

class SearchCriteria {
    private String title;
    private String author;
    private String subject;
    private LocalDate publicationDate;
    private LocalDate publicationStartDate;
    private LocalDate publicationEndDate;
    
    // Builder pattern
    public static class Builder {
        private SearchCriteria criteria = new SearchCriteria();
        
        public Builder withTitle(String title) {
            criteria.title = title;
            return this;
        }
        
        public Builder withAuthor(String author) {
            criteria.author = author;
            return this;
        }
        
        public Builder withSubject(String subject) {
            criteria.subject = subject;
            return this;
        }
        
        public Builder withPublicationDate(LocalDate date) {
            criteria.publicationDate = date;
            return this;
        }
        
        public Builder withPublicationDateRange(LocalDate start, LocalDate end) {
            criteria.publicationStartDate = start;
            criteria.publicationEndDate = end;
            return this;
        }
        
        public SearchCriteria build() {
            return criteria;
        }
    }
}

class BookSearchService implements SearchStrategy {
    private Catalog catalog;
    
    @Override
    public List<Book> search(SearchCriteria criteria) {
        List<Book> results = catalog.getAllBooks();
        
        // Apply filters based on criteria
        if (criteria.getTitle() != null) {
            results = filterByTitle(results, criteria.getTitle());
        }
        
        if (criteria.getAuthor() != null) {
            results = filterByAuthor(results, criteria.getAuthor());
        }
        
        if (criteria.getSubject() != null) {
            results = filterBySubject(results, criteria.getSubject());
        }
        
        if (criteria.getPublicationDate() != null) {
            results = filterByPublicationDate(results, criteria.getPublicationDate());
        }
        
        if (criteria.getPublicationStartDate() != null && 
            criteria.getPublicationEndDate() != null) {
            results = filterByPublicationDateRange(
                results, 
                criteria.getPublicationStartDate(), 
                criteria.getPublicationEndDate()
            );
        }
        
        return results;
    }
    
    private List<Book> filterByTitle(List<Book> books, String title) {
        return books.stream()
            .filter(book -> book.getTitle().toLowerCase()
                .contains(title.toLowerCase()))
            .collect(Collectors.toList());
    }
    
    private List<Book> filterByAuthor(List<Book> books, String author) {
        return books.stream()
            .filter(book -> book.getAuthors().stream()
                .anyMatch(a -> a.toLowerCase().contains(author.toLowerCase())))
            .collect(Collectors.toList());
    }
    
    private List<Book> filterBySubject(List<Book> books, String subject) {
        return books.stream()
            .filter(book -> book.getSubject().equalsIgnoreCase(subject))
            .collect(Collectors.toList());
    }
    
    private List<Book> filterByPublicationDate(List<Book> books, LocalDate date) {
        return books.stream()
            .filter(book -> book.getPublicationDate().equals(date))
            .collect(Collectors.toList());
    }
    
    private List<Book> filterByPublicationDateRange(List<Book> books, 
                                                     LocalDate start, 
                                                     LocalDate end) {
        return books.stream()
            .filter(book -> {
                LocalDate pubDate = book.getPublicationDate();
                return !pubDate.isBefore(start) && !pubDate.isAfter(end);
            })
            .collect(Collectors.toList());
    }
}

// Optimized with indexes
class Catalog {
    private Map<String, List<Book>> titleIndex;
    private Map<String, List<Book>> authorIndex;
    private Map<String, List<Book>> subjectIndex;
    private TreeMap<LocalDate, List<Book>> publicationDateIndex;
    
    List<Book> searchByTitle(String title) {
        return titleIndex.getOrDefault(title.toLowerCase(), Collections.emptyList());
    }
    
    List<Book> searchByAuthor(String author) {
        return authorIndex.getOrDefault(author.toLowerCase(), Collections.emptyList());
    }
    
    List<Book> searchBySubject(String subject) {
        return subjectIndex.getOrDefault(subject, Collections.emptyList());
    }
    
    List<Book> searchByPublicationDateRange(LocalDate start, LocalDate end) {
        return publicationDateIndex.subMap(start, true, end, true)
            .values()
            .stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }
}
```

### Search Examples:

```java
// Example 1: Search by title
SearchCriteria criteria1 = new SearchCriteria.Builder()
    .withTitle("Clean Code")
    .build();
List<Book> results1 = searchService.search(criteria1);

// Example 2: Search by author
SearchCriteria criteria2 = new SearchCriteria.Builder()
    .withAuthor("Robert Martin")
    .build();
List<Book> results2 = searchService.search(criteria2);

// Example 3: Search by subject
SearchCriteria criteria3 = new SearchCriteria.Builder()
    .withSubject("Software Engineering")
    .build();
List<Book> results3 = searchService.search(criteria3);

// Example 4: Search by publication date range
SearchCriteria criteria4 = new SearchCriteria.Builder()
    .withPublicationDateRange(
        LocalDate.of(2000, 1, 1),
        LocalDate.of(2010, 12, 31)
    )
    .build();
List<Book> results4 = searchService.search(criteria4);

// Example 5: Combined search
SearchCriteria criteria5 = new SearchCriteria.Builder()
    .withAuthor("Martin")
    .withSubject("Software Engineering")
    .withPublicationDateRange(
        LocalDate.of(2005, 1, 1),
        LocalDate.of(2015, 12, 31)
    )
    .build();
List<Book> results5 = searchService.search(criteria5);
```

---

## 🏗️ High-Level Architecture

### Core Entity Model:

```
Library
  ├─ Catalog
  │   ├─ List<Book>
  │   └─ Search Indexes
  ├─ List<User>
  │   ├─ Librarian
  │   └─ Member
  ├─ List<TransactionLog>
  └─ Services
      ├─ BorrowingService
      ├─ ReturnService
      ├─ ReservationService
      ├─ RenewalService
      ├─ FineCalculationService
      ├─ SearchService
      └─ NotificationService

Book (Metadata)
  ├─ ISBN
  ├─ Title
  ├─ Authors
  ├─ Subject
  ├─ PublicationDate
  └─ List<BookItem> (Physical Copies)

BookItem (Physical Copy)
  ├─ BookItemId
  ├─ Book Reference
  ├─ Location
  ├─ Status
  ├─ BorrowedBy
  ├─ DueDate
  └─ ReservationQueue

User
  ├─ Librarian
  │   ├─ Can add/remove books
  │   ├─ Can issue fines
  │   └─ Full system access
  └─ Member
      ├─ Can borrow books (max 10)
      ├─ Can reserve books
      ├─ Can renew books
      └─ Limited access

TransactionLog
  ├─ TransactionId
  ├─ Type (Borrow/Return/Reserve/Renew)
  ├─ Member
  ├─ BookItem
  ├─ Date
  └─ Status
```

---

## 🎨 Design Patterns Summary

| Pattern | Usage | Requirements Addressed |
|---------|-------|------------------------|
| **Strategy** | Search algorithms | R14 - Flexible search |
| **Observer** | Notifications | R12 - Event notifications |
| **Factory** | Creating items | R2, R4 - Item creation |
| **Singleton** | Catalog, Library | R1 - Single instance |
| **State** | Book status | R4, R9 - Status management |
| **Command** | Transaction logging | R10 - Audit trail |
| **Repository** | Data access | R1 - Data persistence |
| **Builder** | Search criteria | R14 - Complex object creation |

---

## ✅ SOLID Principles Applied

| Principle | Application |
|-----------|-------------|
| **SRP** | Each service has one responsibility |
| **OCP** | Extensible via strategies (search, notification) |
| **LSP** | Member and Librarian substitutable as User |
| **ISP** | Focused interfaces (SearchStrategy, NotificationService) |
| **DIP** | Services depend on abstractions |

---

## 📊 Requirements Summary Table

| Req ID | Category | Description | Key Classes |
|--------|----------|-------------|-------------|
| **R1** | Logging | Transaction history | TransactionLog, Library |
| **R2** | Identification | Unique book ID + location | BookItem, BookLocation |
| **R3** | Metadata | ISBN, title, author, etc. | Book |
| **R4** | Copies | Multiple physical copies | Book, BookItem |
| **R5** | Users | Librarian and Member | User, Librarian, Member |
| **R6** | Library Card | Unique card per user | LibraryCard |
| **R7** | Limit | Max 10 books per member | Member |
| **R8** | Period | 15 days borrowing | BorrowingService |
| **R9** | Reservation | One reservation per item | Reservation |
| **R10** | Tracking | Who, what, when | TransactionLog |
| **R11** | Renewal | Policy-based renewal | RenewalService |
| **R12** | Notifications | Overdue & availability | NotificationService |
| **R13** | Reserve Unavailable | Queue when borrowed | ReservationService |
| **R14** | Search | Multi-criteria search | SearchService, Catalog |

---

## 💡 Interview Tips

### What to Emphasize:

**1. Book vs BookItem Distinction:**
> ✅ *"I separate Book metadata from BookItem physical copies. One Book can have multiple BookItems, each with unique tracking."*

**2. Transaction Logging:**
> ✅ *"All operations are logged immutably for audit purposes. This ensures we can track the complete history of any book or member."*

**3. Notification System:**
> ✅ *"I use the Observer pattern for notifications. Events trigger alerts without tight coupling between components."*

**4. Search Flexibility:**
> ✅ *"The Builder pattern for search criteria allows combining multiple filters. Indexes optimize performance."*

**5. State Management:**
> ✅ *"Book status follows a state machine: AVAILABLE → BORROWED → RESERVED → AVAILABLE. State transitions are validated."*

---

## 🎯 Common Interview Questions

### Q1: "How do you handle multiple copies of the same book?"

**Answer:**
> *"I use a Book entity for metadata (ISBN, title, author) and separate BookItem entities for each physical copy. Each BookItem has a unique ID and independent status. This allows tracking each copy's location and borrowing status separately."*

---

### Q2: "What if a member tries to borrow more than 10 books?"

**Answer:**
> *"The system validates against the limit before borrowing. Member.canBorrowMoreBooks() checks if borrowedBooksCount < 10. If exceeded, a BorrowLimitExceededException is thrown with a clear message."*

---

### Q3: "How do you calculate fines for overdue books?"

**Answer:**
> *"When a book is returned, the system compares the return date with the due date. If overdue, it calculates: overdueDays × dailyFineRate. The fine is added to the member's account and must be cleared before borrowing again."*

---

### Q4: "How does the reservation queue work?"

**Answer:**
> *"Each BookItem maintains a FIFO queue of reservations. When returned, the system checks if there are reservations. If yes, the first person in the queue is notified and the book status changes to RESERVED. They have 24 hours to collect it."*

---

### Q5: "Can a book be renewed if it's reserved?"

**Answer:**
> *"No. The renewal validation checks if the book is reserved. If yes, renewal is denied because someone else is waiting. This ensures fairness in the system."*

---

## 🔍 Edge Cases to Consider

| Edge Case | Handling |
|-----------|----------|
| **Book lost** | Mark as LOST, charge replacement fee |
| **Member leaves** | Return all books, settle fines, deactivate account |
| **Reservation expires** | Auto-cancel after timeout, notify next in queue |
| **Concurrent borrowing** | Use transactions, lock book item |
| **Fine waiver** | Librarian can waive with authorization |
| **Damaged book** | Mark as DAMAGED, assess repair cost |
| **Multiple reservations** | Prevent duplicate reservations per member |

---

