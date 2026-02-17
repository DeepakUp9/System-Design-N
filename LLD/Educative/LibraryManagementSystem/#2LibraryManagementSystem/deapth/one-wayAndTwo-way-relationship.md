# UML Associations Explained - One-Way vs Two-Way

> **Complete Guide to Understanding Object References and Associations in LLD**

---

## 📖 Table of Contents

1. [What is Association?](#what-is-association)
2. [One-Way Association Explained](#one-way-association-explained)
3. [Two-Way Association Explained](#two-way-association-explained)
4. [Code Examples](#code-examples)
5. [Comparison Table](#comparison-table)
6. [Real-World Analogies](#real-world-analogies)
7. [How to Identify in UML](#how-to-identify-in-uml)

---

## 🎯 What is Association?

### Definition
**Association** represents a relationship between two classes where one class uses or interacts with another class.

### Key Question
> **"Which class holds a reference (instance variable) to which other class?"**

---

## 📍 One-Way Association Explained

### Definition
**One-way association** means:
- ✅ **Class A knows about Class B** (can interact with it)
- ❌ **Class B does NOT know about Class A** (no reference back)

### Visual Representation
```
┌──────────────┐          ┌──────────────┐
│   Class A    │─────────►│   Class B    │
│              │          │              │
│ knows about  │          │ doesn't know │
│   Class B    │          │   Class A    │
└──────────────┘          └──────────────┘

Direction of knowledge: A → B (one-way only)
```

---

## 📚 Example 1: User ────► BookItem

### UML Diagram
```
┌──────────────┐          ┌──────────────┐
│     User     │─────────►│   BookItem   │
│              │          │              │
│ can borrow   │          │ has status   │
│  BookItem    │          │              │
└──────────────┘          └──────────────┘

One-way association: User knows BookItem exists
                     BookItem doesn't track User
```

---

### Code Implementation

#### User Class (Source - Knows About BookItem)
```java
class User {
    protected String userId;
    protected String password;
    protected AccountStatus status;
    protected Person person;
    protected LibraryCard card;
    
    // ❌ NO instance variable for BookItem!
    // User doesn't STORE BookItem references
    
    // ✅ But User can INTERACT with BookItem via parameters
    public boolean borrowBook(BookItem bookItem) {
        // User knows BookItem exists and can call its methods
        if (bookItem.isAvailable()) {
            bookItem.setStatus(BookStatus.LOANED);
            return true;
        }
        return false;
    }
    
    public void returnBook(BookItem bookItem) {
        // User can interact with BookItem as method parameter
        bookItem.setStatus(BookStatus.AVAILABLE);
    }
}
```

**Key Points:**
- 📌 User does NOT store BookItem as instance variable
- 📌 User receives BookItem as **method parameter**
- 📌 User can call methods on BookItem
- 📌 Temporary interaction, not permanent storage

---

#### BookItem Class (Target - Doesn't Know User)
```java
class BookItem {
    private String barcode;
    private boolean isReferenceOnly;
    private Date borrowed;
    private Date dueDate;
    private double price;
    private BookFormat format;
    private BookStatus status;
    private Date dateOfPurchase;
    private Rack placedAt;
    private Book book;
    
    // ❌ NO reference to User!
    // ❌ NO borrowedBy field
    // BookItem doesn't know WHO is borrowing it
    
    public boolean isAvailable() {
        return status == BookStatus.AVAILABLE;
    }
    
    public void setStatus(BookStatus status) {
        this.status = status;
    }
    
    public BookStatus getStatus() {
        return status;
    }
}
```

**Key Points:**
- 📌 BookItem has NO User reference
- 📌 BookItem doesn't know who's interacting with it
- 📌 BookItem is independent of User
- 📌 Only knows its own state (status, location, etc.)

---

### How It Works

```java
// Usage Example
User user = new User("U001", "password");
BookItem book = new BookItem("ISBN123", BookStatus.AVAILABLE);

// User can interact with BookItem
user.borrowBook(book);  // ✅ Works - User knows BookItem

// BookItem CANNOT interact with User
// book.getBorrower();  // ❌ No such method - BookItem doesn't know User
```

---

## 📚 Example 2: User ────► BookReservation

### UML Diagram
```
┌──────────────┐          ┌───────────────────┐
│     User     │─────────►│ BookReservation   │
│              │          │                   │
│  can create  │          │ doesn't reference │
│ reservations │          │      User         │
└──────────────┘          └───────────────────┘
```

---

### Code Implementation

#### User Class (Creates BookReservation)
```java
class User {
    protected String userId;
    protected String password;
    
    // ❌ NO List<BookReservation> stored here!
    // User doesn't store reservation references
    
    // ✅ But can CREATE and INTERACT with BookReservation
    public BookReservation reserveBook(BookItem bookItem) {
        // User creates BookReservation object
        BookReservation reservation = new BookReservation(
            bookItem.getBarcode(), 
            new Date()
        );
        
        // Reservation stored in external system (database/manager)
        // NOT stored in User object itself
        ReservationManager.addReservation(this.userId, reservation);
        
        return reservation;
    }
    
    public void cancelReservation(BookReservation reservation) {
        // User can interact with reservation as parameter
        reservation.setStatus(ReservationStatus.CANCELED);
    }
}
```

---

#### BookReservation Class (Doesn't Know User)
```java
class BookReservation {
    private String itemId;
    private Date creationDate;
    private ReservationStatus status;
    
    // ❌ NO reference to User!
    // ❌ NO reservedBy field
    // Reservation doesn't know WHO made it
    
    public BookReservation(String itemId, Date creationDate) {
        this.itemId = itemId;
        this.creationDate = creationDate;
        this.status = ReservationStatus.WAITING;
    }
    
    public ReservationStatus getStatus() {
        return status;
    }
    
    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}
```

---

## 🔄 Two-Way Association Explained

### Definition
**Two-way association** means:
- ✅ **Class A knows about Class B** (has reference to B)
- ✅ **Class B knows about Class A** (has reference to A)
- ✅ **Bidirectional navigation** possible

### Visual Representation
```
┌──────────────┐          ┌──────────────┐
│   Class A    │◄────────►│   Class B    │
│              │          │              │
│ holds ref    │          │ holds ref    │
│  to B        │          │  to A        │
└──────────────┘          └──────────────┘

Both directions: A ↔ B (bidirectional)
```

---

## 📚 Example 3: BookLending ◄────► User (Member)

### UML Diagram
```
┌──────────────┐          ┌──────────────┐
│ BookLending  │◄────────►│    Member    │
│              │          │   (User)     │
│ - borrower:  │          │ - lendings:  │
│   Member     │          │   List       │
└──────────────┘          └──────────────┘

Two-way association: Both hold references to each other
```

---

### Code Implementation

#### BookLending Class (Holds Reference to Member)
```java
class BookLending {
    private String lendingId;
    private Date creationDate;
    private Date dueDate;
    private Date returnDate;
    private String bookItemBarcode;
    
    // ✅ HOLDS direct reference to Member
    private Member borrower;  // <--- Instance variable!
    
    private BookItem bookItem;
    
    public BookLending(Member borrower, BookItem bookItem) {
        this.lendingId = UUID.randomUUID().toString();
        this.borrower = borrower;  // Store the member
        this.bookItem = bookItem;
        this.creationDate = new Date();
        this.dueDate = calculateDueDate();
    }
    
    // ✅ Can navigate to Member
    public Member getBorrower() {
        return borrower;  // Return the stored member
    }
    
    public String getBorrowerName() {
        return borrower.getPerson().getName();  // Navigate through reference
    }
    
    private Date calculateDueDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 15);
        return cal.getTime();
    }
}
```

**Key Points:**
- 📌 BookLending STORES Member as instance variable
- 📌 Can navigate from BookLending to Member
- 📌 Permanent reference (not just method parameter)

---

#### Member Class (Holds References to BookLending)
```java
class Member extends User {
    private Date dateOfMembership;
    private int totalBooksCheckedout;
    
    // ✅ HOLDS list of lending records
    private List<BookLending> activeLendings;  // <--- Instance variable!
    
    private final int MAX_BOOKS_ALLOWED = 10;
    
    public Member(String userId, String password, Person person) {
        super(userId, password, person);
        this.activeLendings = new ArrayList<>();
        this.totalBooksCheckedout = 0;
        this.dateOfMembership = new Date();
    }
    
    public boolean checkoutBook(BookItem bookItem) {
        if (totalBooksCheckedout >= MAX_BOOKS_ALLOWED) {
            return false;
        }
        
        // Create lending record
        BookLending lending = new BookLending(this, bookItem);
        
        // ✅ Both sides store references
        activeLendings.add(lending);      // Member stores lending
        // lending.borrower = this;        // Lending stores member
        
        totalBooksCheckedout++;
        return true;
    }
    
    // ✅ Can navigate to BookLending
    public List<BookLending> getActiveLendings() {
        return new ArrayList<>(activeLendings);  // Return copy
    }
    
    public BookLending getLendingByBookItem(BookItem bookItem) {
        // Navigate through stored references
        return activeLendings.stream()
            .filter(lending -> lending.getBookItem().equals(bookItem))
            .findFirst()
            .orElse(null);
    }
}
```

**Key Points:**
- 📌 Member STORES List<BookLending> as instance variable
- 📌 Can navigate from Member to BookLending records
- 📌 Both classes know about each other

---

### How It Works

```java
// Usage Example
Member member = new Member("M001", "password", person);
BookItem book = new BookItem("ISBN123", BookStatus.AVAILABLE);

// Create two-way association
member.checkoutBook(book);

// ✅ Navigate from Member to BookLending
List<BookLending> lendings = member.getActiveLendings();
BookLending lending = lendings.get(0);

// ✅ Navigate from BookLending to Member
Member borrower = lending.getBorrower();
System.out.println(borrower.getPerson().getName());  // Can access member info

// Both directions work!
```

---

## 📊 Comparison Table: One-Way vs Two-Way

| Aspect | One-Way Association | Two-Way Association |
|--------|---------------------|---------------------|
| **Example** | User → BookItem | BookLending ↔ Member |
| **Class A holds Class B?** | ❌ No instance variable | ✅ Yes, as instance variable |
| **Class B holds Class A?** | ❌ No reference | ✅ Yes, as instance variable |
| **Navigation** | A → B only | A → B AND B → A |
| **Coupling** | Loose (single direction) | Tighter (both directions) |
| **Storage** | Method parameter only | Instance variables on both sides |
| **Example Code** | `void method(BookItem item)` | `private Member borrower;` |
| **UML Symbol** | `────►` | `◄────►` |
| **When to Use** | Temporary interaction | Permanent relationship |

---

## 🎨 Real-World Analogies

### One-Way Association (User → BookItem)

**Analogy: Customer in a Store**
```
🛒 CUSTOMER picking up PRODUCT:
- Customer can PICK UP products (knows they exist)
- Customer can PUT DOWN products
- Products DON'T know who picked them up
- Products just sit on shelf

Code equivalent:
user.borrowBook(bookItem);  // User interacts with item
// bookItem doesn't track user
```

**Another Analogy: Remote Control and TV**
```
📺 REMOTE → TV:
- Remote can control TV (knows about TV)
- TV doesn't know which remote is controlling it
- TV just responds to signals

Code equivalent:
remote.changeChannel(tv, 5);  // Remote knows TV
// TV doesn't store which remote
```

---

### Two-Way Association (BookLending ↔ Member)

**Analogy: Marriage Certificate**
```
💑 MARRIAGE CERTIFICATE:
- Certificate has husband's name (knows husband)
- Certificate has wife's name (knows wife)
- Husband has copy of certificate (knows certificate)
- Wife has copy of certificate (knows certificate)
- Both can reference each other

Code equivalent:
lending.borrower = member;        // Lending knows member
member.lendings.add(lending);     // Member knows lending
```

**Another Analogy: Employee and Department**
```
🏢 EMPLOYEE ↔ DEPARTMENT:
- Employee knows which department (has department reference)
- Department knows its employees (has employee list)
- Bidirectional lookup possible

Code equivalent:
employee.department = dept;         // Employee knows dept
dept.employees.add(employee);       // Dept knows employee
```

---

## 🔍 How to Identify in UML

### One-Way Arrow (────►)

```
┌──────────┐          ┌──────────┐
│ Source   │─────────►│ Target   │
└──────────┘          └──────────┘
```

**Means:**
- Source can call methods on Target
- Source does NOT store Target as instance variable (usually)
- Target has NO reference back to Source
- Interaction via method parameters

**Code Pattern:**
```java
class Source {
    // NO Target field
    
    void method(Target t) {  // Parameter only
        t.doSomething();
    }
}
```

---

### Two-Way Line (◄────►)

```
┌──────────┐          ┌──────────┐
│ Class A  │◄────────►│ Class B  │
└──────────┘          └──────────┘
```

**Means:**
- Class A stores Class B as instance variable
- Class B stores Class A as instance variable
- Bidirectional navigation possible
- Both classes know about each other

**Code Pattern:**
```java
class ClassA {
    private ClassB b;  // Holds reference
}

class ClassB {
    private ClassA a;  // Holds reference
}
```

---

## 💡 Key Differences Summary

### One-Way Association
```java
// ❌ NO instance variable
class User {
    // No BookItem field here
    
    void borrowBook(BookItem item) {  // ✅ Parameter only
        item.setStatus(BookStatus.LOANED);
    }
}

class BookItem {
    // No User field
}
```

**Characteristics:**
- Method parameter relationship
- Temporary interaction
- Loose coupling
- Source knows target exists
- Target is independent

---

### Two-Way Association
```java
// ✅ Both have instance variables
class BookLending {
    private Member borrower;  // ✅ Holds reference
}

class Member {
    private List<BookLending> lendings;  // ✅ Holds references
}
```

**Characteristics:**
- Instance variable relationship
- Permanent connection
- Tighter coupling
- Both sides know each other
- Bidirectional navigation

---

## 🎯 When to Use Which?

### Use One-Way When:
- ✅ Temporary interaction needed
- ✅ Don't need to navigate back
- ✅ Want loose coupling
- ✅ Target should be independent
- ✅ Just calling methods, not storing

**Example:** User borrowing a book (temporary action)

---

### Use Two-Way When:
- ✅ Need bidirectional navigation
- ✅ Permanent relationship exists
- ✅ Both sides need to know each other
- ✅ Complex queries required
- ✅ Strong business relationship

**Example:** Lending record and borrower (permanent record)

---

## 📋 Quick Reference Checklist

### Identifying One-Way Association:
- [ ] Source class has NO instance variable for target
- [ ] Target appears only as method parameter
- [ ] Target class has NO reference to source
- [ ] Can navigate Source → Target only
- [ ] UML shows single arrow (────►)

### Identifying Two-Way Association:
- [ ] Both classes have instance variables
- [ ] Can navigate in both directions
- [ ] Both classes store references
- [ ] Can call methods on both sides
- [ ] UML shows double arrow (◄────►)

---

## 🎓 Interview Tips

### What to Say:

**For One-Way:**
> *"User has a one-way association with BookItem, meaning User can interact with BookItem through method parameters, but doesn't store BookItem references. BookItem is independent and doesn't know about User."*

**For Two-Way:**
> *"BookLending and Member have a two-way association. BookLending stores a reference to the Member who borrowed, and Member maintains a list of active BookLending records. This enables bidirectional navigation."*

---

## 📥 Download Instructions

To save these notes:
1. Click the **download button (⬇️)** above
2. Or copy and save as `uml-associations-explained.md`
3. Open in any Markdown viewer

---

**Complete Guide Ready! 🚀**

*Master UML associations for your LLD interviews!*