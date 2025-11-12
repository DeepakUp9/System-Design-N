# Library Management System - Class Diagram Guide

## 🧩 1. Overall Concept

The Class Diagram defines the static structure of your system — it shows:

- The main entities (classes)
- Their attributes and methods
- How these classes relate to each other (association, composition, inheritance, aggregation, etc.)

### Key Entities
- **Entities**: Book, BookItem, User, Librarian, Member, LibraryCard, Rack, etc.
- **Relationships**: "A User has a LibraryCard" or "A Book contains many BookItems"

---

## 📘 2. Classes Breakdown (Bottom-Up Approach)

### 🧱 A. Book & BookItem

#### Book
Represents conceptual information — like "Clean Code by Robert C. Martin".

```java
class Book {
    String ISBN;
    String title;
    String subject;
    Date publicationDate;
    List<Author> authors;
}
```

#### BookItem
Represents a physical copy of a book (like your library's specific copy of "Clean Code").

```java
class BookItem {
    String barcode;
    BookStatus status; // AVAILABLE, RESERVED, LOANED, LOST
    Rack rack;
}
```

**✅ Relationship:**
- A Book can have many BookItems → **Composition**
- If the book is deleted, all its copies (BookItems) must also be deleted

---

### 🧱 B. Rack

Represents where a physical book is kept.

```java
class Rack {
    String number;
    String locationIdentifier;
}
```

**✅ Association:**
- Rack ↔ BookItem → one rack can hold multiple book items (1 — 0..*)

---

### 🧱 C. Person & Author

#### Person
Represents general human info.

```java
class Person {
    String name;
    String email;
    Address address;
}
```

#### Author
Represents a person who writes books.

```java
class Author {
    String name;
    String description;
}
```

**✅ Relationship:**
- A Book has one or more Authors
- An Author can write many Books
- → **Many-to-Many Association** (1..* ↔ 1..*)

---

### 🧱 D. User, Librarian & Member

#### User (Abstract)
Common info for both librarians and members:

```java
abstract class User {
    String userId;
    String password;
    AccountStatus status;
    LibraryCard card;
}
```

#### Librarian
Adds books, manages members, etc.

```java
class Librarian extends User {
    void addBook(Book book) {}
    void blockMember(Member member) {}
}
```

#### Member
Borrows, renews, and reserves books.

```java
class Member extends User {
    int totalBooksCheckedOut;
    void reserveBook(BookItem item) {}
    void returnBook(BookItem item) {}
}
```

**✅ Inheritance:**
- Librarian and Member inherit from User

---

### 🧱 E. LibraryCard

Every user gets a unique card:

```java
class LibraryCard {
    String cardNumber;
    Date issuedAt;
    boolean active;
}
```

**✅ Composition:**
- Each User has exactly one LibraryCard

---

### 🧱 F. BookReservation & BookLending

#### BookReservation
Handles when a user reserves a book.

```java
class BookReservation {
    Date reservedOn;
    ReservationStatus status; // WAITING, PENDING, COMPLETED
    BookItem item;
    Member reservedBy;
}
```

#### BookLending
Handles lending/return process.

```java
class BookLending {
    Date issuedOn;
    Date dueDate;
    Date returnedOn;
    BookItem item;
    Member borrowedBy;
}
```

**✅ Relationship:**
- BookReservation and BookLending → associate with BookItem
- Both also connect to User (who borrowed or reserved the book)

---

### 🧱 G. Notification Hierarchy

Abstract class → specialized for postal or email.

```java
abstract class Notification {
    int notificationId;
    String content;
}

class PostalNotification extends Notification {
    Address address;
}

class EmailNotification extends Notification {
    String email;
}
```

**✅ Inheritance:**
- PostalNotification and EmailNotification extend Notification

**✅ Association:**
- Notifications are triggered by BookLending and BookReservation events

---

### 🧱 H. Search & Catalog

#### Interface: Search
Defines methods to find books:

```java
interface Search {
    List<Book> searchByTitle(String title);
    List<Book> searchByAuthor(String author);
}
```

#### Catalog
Implements the search interface.

```java
class Catalog implements Search {
    List<Book> books;
}
```

**✅ Aggregation:**
- Catalog contains many Book objects (1 — 1..*)

---

### 🧱 I. Library

Represents the entire library.

```java
class Library {
    String name;
    Address address;
    List<BookItem> bookItems;
}
```

**✅ Composition:**
- A library cannot exist without its book items

---

## ⚙️ 3. Relationships Summary (with Multiplicity)

| Source | Target | Multiplicity | Explanation |
|--------|--------|--------------|-------------|
| Library | BookItem | 1 — 1..* | A library must have at least one book item |
| Rack | BookItem | 1 — 0..* | A rack may have zero or many book items |
| Book | BookItem | 1 — 1..* | Each book has many copies |
| Book | Author | 1..* — 1..* | Books have one or more authors; authors can write multiple books |
| User | LibraryCard | 1 — 1 | Every user has exactly one card |
| User | BookLending | 1 — 0..* | A user can have many lendings |
| BookReservation | BookItem | 1 — 0..1 | Each reservation is for one book item |
| Catalog | Book | 1 — 1..* | A catalog contains many books |
| Person | Author | 1 — 1..* | Every author is a person |

---

## 🎨 4. Design Patterns in the System

| Pattern | Where Used | Why Used |
|---------|------------|----------|
| Factory Pattern | BookFactory, UserFactory | Centralized creation, consistent objects |
| Delegation Pattern | Librarian → BookItem | Separation of concerns — each class handles its own logic |
| Observer Pattern | Notification System | Automatic alerts when a book's state changes |

### Example (Observer Pattern):
When a reserved book becomes available:

1. BookItem changes state to "AVAILABLE"
2. BookItem notifies all observers (members who reserved it)
3. EmailNotification or PostalNotification is triggered

---

## 💰 5. Additional Features

### Barcode Reader
- Each LibraryCard and BookItem gets a barcode
- System scans it for faster issuing/returning
- Implemented via a BarcodeReader class

### FineTransaction (Decorator Pattern)
- Each overdue day adds a fine
- Fines can be paid via cash, card, or check
- The decorator allows extending fine calculations dynamically

---

## 🧠 6. Real-World Interview Question Example

**Q: "If two members try to reserve the same book at the same time, who gets it?"**

**Answer:**
- Reservation follows First Come, First Serve (FCFS)
- If both request simultaneously → check `totalBooksCheckedOut`
- If both have reached limit → neither can reserve

---

## 📋 Quick Reference

### Key Relationships to Remember:
- **Composition** (strong): Library ↔ BookItem, User ↔ LibraryCard
- **Aggregation** (weak): Catalog ↔ Book
- **Association**: Rack ↔ BookItem, User ↔ BookLending
- **Inheritance**: User → Librarian/Member, Notification → Email/Postal

### SOLID Principles Applied:
- **Single Responsibility**: Each class has one clear purpose
- **Open/Closed**: Extensible via interfaces (Search) and abstract classes (User, Notification)
- **Liskov Substitution**: Librarian and Member can substitute User
- **Interface Segregation**: Search interface is focused and minimal
- **Dependency Inversion**: High-level modules depend on abstractions (interfaces)

---

*Generated for Library Management System Design*
