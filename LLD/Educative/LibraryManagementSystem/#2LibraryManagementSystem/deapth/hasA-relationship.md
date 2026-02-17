# Aggregation vs Composition - Has-A Relationships Explained

> **Complete Guide to Understanding Aggregation and Composition in LLD**

---

## 📖 Table of Contents

1. [What is Has-A Relationship?](#what-is-has-a-relationship)
2. [Aggregation Explained](#aggregation-explained)
3. [Composition Explained](#composition-explained)
4. [Code Examples with Memory Management](#code-examples-with-memory-management)
5. [Comparison Table](#comparison-table)
6. [Real-World Analogies](#real-world-analogies)
7. [How to Identify in UML](#how-to-identify-in-uml)
8. [Interview Questions](#interview-questions)

---

## 🎯 What is Has-A Relationship?

### Definition
**Has-A relationship** means one class **contains** or **owns** another class as a member.

Both **Aggregation** and **Composition** are "Has-A" relationships, but they differ in:
- **Ownership strength**
- **Lifecycle dependency**
- **Memory management**

### Key Question
> **"If the container is destroyed, does the contained object survive?"**

---

## 🔷 Aggregation Explained (Weak Has-A)

### Definition
**Aggregation** is a **weak "Has-A" relationship** where:
- ✅ Container **HAS** the contained object
- ✅ Contained object **CAN EXIST independently**
- ✅ If container is destroyed, contained object **SURVIVES**
- ✅ Shared ownership possible

### Visual Representation
```
┌──────────────┐          ┌──────────────┐
│  Container   │◇─────────│  Contained   │
│              │          │              │
│ has/uses     │          │ can exist    │
│              │          │ independently│
└──────────────┘          └──────────────┘

Symbol: ◇─────  (Hollow diamond at container side)
```

### Key Characteristics
| Characteristic | Value |
|----------------|-------|
| **Relationship Type** | Weak Has-A |
| **Lifecycle** | Independent |
| **Ownership** | Shared or weak |
| **Destruction** | Contained survives |
| **UML Symbol** | Hollow diamond (◇) |

---

## 📚 Example 1: Catalog ◇──── Book (Aggregation)

### UML Diagram
```
┌──────────────────────┐          ┌──────────────┐
│      Catalog         │◇─────────│     Book     │
│                      │  1..*    │              │
│ - books: List<Book>  │          │ - ISBN       │
│                      │          │ - title      │
└──────────────────────┘          └──────────────┘

Aggregation: Catalog HAS Books
             Books can exist without Catalog
```

### Real-World Meaning
```
📚 Library Catalog vs Books:
- Catalog CONTAINS list of books
- Books exist independently (on shelves)
- If you destroy catalog, books still exist
- Books can be in multiple catalogs
- Weak ownership
```

---

### Code Implementation

#### Book Class (Can Exist Independently)
```java
class Book {
    private String ISBN;
    private String title;
    private List<String> authors;
    private String subject;
    private String publisher;
    private Date publicationDate;
    
    // Book has its own lifecycle
    public Book(String ISBN, String title, List<String> authors) {
        this.ISBN = ISBN;
        this.title = title;
        this.authors = authors;
    }
    
    // Book can exist without Catalog
    public String getISBN() {
        return ISBN;
    }
    
    public String getTitle() {
        return title;
    }
    
    @Override
    public String toString() {
        return "Book{ISBN='" + ISBN + "', title='" + title + "'}";
    }
}
```

---

#### Catalog Class (Aggregates Books)
```java
class Catalog {
    private List<Book> books;  // ◇ Aggregation: holds references
    
    // Catalog receives already-created books
    public Catalog() {
        this.books = new ArrayList<>();
    }
    
    // ✅ Add existing book (not creating new one)
    public void addBook(Book book) {
        books.add(book);  // Just storing reference
    }
    
    // ✅ Remove book (but book still exists elsewhere)
    public void removeBook(Book book) {
        books.remove(book);  // Just removing reference
    }
    
    public List<Book> searchByTitle(String title) {
        return books.stream()
            .filter(b -> b.getTitle().contains(title))
            .collect(Collectors.toList());
    }
    
    // When Catalog is destroyed, books survive
    @Override
    protected void finalize() {
        System.out.println("Catalog destroyed, but books still exist!");
        // Books are NOT destroyed
        // They exist independently
    }
}
```

---

### Memory and Lifecycle Demonstration

```java
public class AggregationDemo {
    public static void main(String[] args) {
        // Step 1: Create books independently
        Book book1 = new Book("ISBN001", "Clean Code", 
            Arrays.asList("Robert Martin"));
        Book book2 = new Book("ISBN002", "Design Patterns", 
            Arrays.asList("Gang of Four"));
        
        System.out.println("Books created: " + book1 + ", " + book2);
        
        // Step 2: Create catalog and add books
        Catalog catalog = new Catalog();
        catalog.addBook(book1);
        catalog.addBook(book2);
        
        System.out.println("Books added to catalog");
        
        // Step 3: Destroy catalog
        catalog = null;  // Catalog destroyed
        System.gc();     // Request garbage collection
        
        // Step 4: Books still exist!
        System.out.println("After catalog destroyed:");
        System.out.println("Book 1 still exists: " + book1);
        System.out.println("Book 2 still exists: " + book2);
        
        // ✅ Books survive catalog destruction
        // This is AGGREGATION
    }
}
```

**Output:**
```
Books created: Book{ISBN='ISBN001', title='Clean Code'}, Book{ISBN='ISBN002', title='Design Patterns'}
Books added to catalog
Catalog destroyed, but books still exist!
After catalog destroyed:
Book 1 still exists: Book{ISBN='ISBN001', title='Clean Code'}
Book 2 still exists: Book{ISBN='ISBN002', title='Design Patterns'}
```

---

### Key Points for Aggregation
```java
// ✅ Books created BEFORE catalog
Book book = new Book("ISBN001", "Title");

// ✅ Catalog just holds references
Catalog catalog = new Catalog();
catalog.addBook(book);  // Adding existing book

// ✅ Book can be in multiple catalogs
Catalog catalog2 = new Catalog();
catalog2.addBook(book);  // Same book, different catalog

// ✅ Destroy catalog, book survives
catalog = null;  // Catalog gone
// book still exists! ✅
```

---

## 🔶 Composition Explained (Strong Has-A)

### Definition
**Composition** is a **strong "Has-A" relationship** where:
- ✅ Container **OWNS** the contained object
- ✅ Contained object **CANNOT EXIST independently**
- ✅ If container is destroyed, contained object is **DESTROYED**
- ✅ Exclusive ownership (no sharing)

### Visual Representation
```
┌──────────────┐          ┌──────────────┐
│  Container   │◆─────────│  Contained   │
│              │          │              │
│ owns         │          │ cannot exist │
│              │          │ independently│
└──────────────┘          └──────────────┘

Symbol: ◆─────  (Filled diamond at container side)
```

### Key Characteristics
| Characteristic | Value |
|----------------|-------|
| **Relationship Type** | Strong Has-A |
| **Lifecycle** | Dependent |
| **Ownership** | Exclusive |
| **Destruction** | Contained destroyed |
| **UML Symbol** | Filled diamond (◆) |

---

## 📚 Example 2: Book ◆──── BookItem (Composition)

### UML Diagram
```
┌──────────────────────────┐          ┌─────────────────────┐
│         Book             │◆─────────│      BookItem       │
│                          │  1..*    │                     │
│ - ISBN: String           │          │ - barcode: String   │
│ - title: String          │          │ - status: Status    │
│ - bookItems: List        │          │ - book: Book        │
└──────────────────────────┘          └─────────────────────┘

Composition: Book OWNS BookItems
             BookItems CANNOT exist without Book
```

### Real-World Meaning
```
📖 Book Metadata vs Physical Copies:
- Book OWNS its physical copies (BookItems)
- BookItems are PARTS OF the book
- If you delete book, all copies are deleted
- BookItems cannot exist without parent Book
- Strong ownership
```

---

### Code Implementation

#### BookItem Class (Dependent on Book)
```java
class BookItem {
    private String barcode;
    private BookStatus status;
    private Date borrowed;
    private Date dueDate;
    private Book book;  // Reference to parent Book
    
    // ❌ BookItem CANNOT be created without Book
    // Constructor requires Book
    public BookItem(Book book, String barcode) {
        if (book == null) {
            throw new IllegalArgumentException("BookItem must belong to a Book");
        }
        this.book = book;
        this.barcode = barcode;
        this.status = BookStatus.AVAILABLE;
    }
    
    // BookItem's lifecycle tied to Book
    public Book getBook() {
        return book;
    }
    
    public String getBarcode() {
        return barcode;
    }
    
    @Override
    public String toString() {
        return "BookItem{barcode='" + barcode + "', status=" + status + "}";
    }
}
```

---

#### Book Class (Owns BookItems)
```java
class Book {
    private String ISBN;
    private String title;
    private List<String> authors;
    private List<BookItem> bookItems;  // ◆ Composition: owns items
    
    public Book(String ISBN, String title, List<String> authors) {
        this.ISBN = ISBN;
        this.title = title;
        this.authors = authors;
        this.bookItems = new ArrayList<>();
    }
    
    // ✅ Book CREATES BookItems (not receiving external ones)
    public BookItem addBookItem(String barcode) {
        BookItem item = new BookItem(this, barcode);  // Creating new
        bookItems.add(item);
        return item;
    }
    
    // ✅ Book OWNS and manages BookItems
    public void removeBookItem(BookItem item) {
        bookItems.remove(item);
        // BookItem is destroyed (no longer accessible)
    }
    
    public List<BookItem> getBookItems() {
        return new ArrayList<>(bookItems);  // Defensive copy
    }
    
    // When Book is destroyed, ALL BookItems are destroyed
    @Override
    protected void finalize() {
        System.out.println("Book destroyed: " + title);
        System.out.println("All " + bookItems.size() + " BookItems also destroyed!");
        // BookItems are automatically garbage collected
        // They cannot exist without Book
    }
}
```

---

### Memory and Lifecycle Demonstration

```java
public class CompositionDemo {
    public static void main(String[] args) {
        // Step 1: Create book
        Book book = new Book("ISBN001", "Clean Code", 
            Arrays.asList("Robert Martin"));
        
        System.out.println("Book created: " + book.getTitle());
        
        // Step 2: Book creates its own BookItems
        BookItem item1 = book.addBookItem("BARCODE001");
        BookItem item2 = book.addBookItem("BARCODE002");
        
        System.out.println("BookItems created by Book: " + item1 + ", " + item2);
        
        // Step 3: BookItems CANNOT exist without Book
        // You CANNOT do this:
        // BookItem item3 = new BookItem(null, "BARCODE003");  // ❌ Error!
        
        // Step 4: Destroy book
        book = null;  // Book destroyed
        System.gc();  // Request garbage collection
        
        // Step 5: BookItems are AUTOMATICALLY destroyed!
        // item1 and item2 are now invalid
        // They don't exist anymore
        
        System.out.println("After book destroyed:");
        System.out.println("BookItems are also destroyed (garbage collected)");
        
        // ❌ BookItems CANNOT survive book destruction
        // This is COMPOSITION
    }
}
```

**Output:**
```
Book created: Clean Code
BookItems created by Book: BookItem{barcode='BARCODE001', status=AVAILABLE}, BookItem{barcode='BARCODE002', status=AVAILABLE}
Book destroyed: Clean Code
All 2 BookItems also destroyed!
After book destroyed:
BookItems are also destroyed (garbage collected)
```

---

### Key Points for Composition
```java
// ❌ BookItems CANNOT be created independently
// BookItem item = new BookItem(null, "BARCODE001");  // Error!

// ✅ Book creates and owns BookItems
Book book = new Book("ISBN001", "Title", authors);
BookItem item = book.addBookItem("BARCODE001");  // Book creates it

// ❌ BookItem cannot be in multiple Books
// Book book2 = new Book("ISBN002", "Title2", authors);
// book2.addBookItem(item);  // ❌ Wrong! Item already owned

// ❌ Destroy book, BookItems destroyed too
book = null;  // Book gone
// item is now garbage (will be collected) ❌
```

---

## 📚 Example 3: User ◆──── LibraryCard (Composition)

### UML Diagram
```
┌──────────────────────┐          ┌─────────────────────┐
│        User          │◆─────────│    LibraryCard      │
│                      │    1     │                     │
│ - userId: String     │          │ - cardNumber: String│
│ - card: LibraryCard  │          │ - user: User        │
└──────────────────────┘          └─────────────────────┘

Composition: User OWNS LibraryCard
             LibraryCard CANNOT exist without User
```

---

### Code Implementation

#### LibraryCard Class (Dependent on User)
```java
class LibraryCard {
    private String cardNumber;
    private String barcode;
    private Date issuedAt;
    private boolean active;
    private User user;  // Reference to owner
    
    // ❌ LibraryCard CANNOT exist without User
    protected LibraryCard(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Card must belong to a User");
        }
        this.user = user;
        this.cardNumber = generateCardNumber();
        this.barcode = generateBarcode();
        this.issuedAt = new Date();
        this.active = true;
    }
    
    private String generateCardNumber() {
        return "CARD-" + System.currentTimeMillis();
    }
    
    private String generateBarcode() {
        return "BC-" + cardNumber;
    }
    
    public String getCardNumber() {
        return cardNumber;
    }
    
    public User getUser() {
        return user;
    }
    
    @Override
    protected void finalize() {
        System.out.println("LibraryCard destroyed: " + cardNumber);
    }
}
```

---

#### User Class (Owns LibraryCard)
```java
abstract class User {
    protected String userId;
    protected String password;
    protected Person person;
    protected LibraryCard card;  // ◆ Composition: owns card
    
    public User(String userId, String password, Person person) {
        this.userId = userId;
        this.password = password;
        this.person = person;
        
        // ✅ User CREATES its own LibraryCard
        this.card = new LibraryCard(this);  // Creating owned object
    }
    
    public LibraryCard getCard() {
        return card;
    }
    
    // When User is destroyed, LibraryCard is destroyed
    @Override
    protected void finalize() {
        System.out.println("User destroyed: " + userId);
        System.out.println("LibraryCard also destroyed!");
        // Card is automatically garbage collected
    }
}
```

---

### Memory and Lifecycle Demonstration

```java
public class CompositionUserDemo {
    public static void main(String[] args) {
        // Step 1: Create person
        Person person = new Person("John Doe", "john@example.com");
        
        // Step 2: Create user (automatically creates LibraryCard)
        Member member = new Member("USER001", "password", person);
        LibraryCard card = member.getCard();
        
        System.out.println("User created: " + member.getUserId());
        System.out.println("Card automatically created: " + card.getCardNumber());
        
        // Step 3: Card CANNOT exist without User
        // You CANNOT do this:
        // LibraryCard card2 = new LibraryCard(null);  // ❌ Error!
        
        // Step 4: Destroy user
        member = null;  // User destroyed
        System.gc();
        
        // Step 5: Card is AUTOMATICALLY destroyed
        System.out.println("After user destroyed:");
        System.out.println("Card is also destroyed (garbage collected)");
        
        // ❌ Card CANNOT survive user destruction
    }
}
```

**Output:**
```
User created: USER001
Card automatically created: CARD-1234567890
User destroyed: USER001
LibraryCard also destroyed!
After user destroyed:
Card is also destroyed (garbage collected)
```

---

## 📚 Example 4: Library ◆──── BookItem (Composition)

### UML Diagram
```
┌──────────────────────┐          ┌─────────────────────┐
│       Library        │◆─────────│      BookItem       │
│                      │  1..*    │                     │
│ - name: String       │          │ - barcode: String   │
│ - items: List        │          │ - library: Library  │
└──────────────────────┘          └─────────────────────┘

Composition: Library OWNS all BookItems
             BookItems exist only within this Library
```

---

### Code Implementation

```java
class Library {
    private String name;
    private Address address;
    private List<BookItem> bookItems;  // ◆ Composition: owns items
    
    public Library(String name, Address address) {
        this.name = name;
        this.address = address;
        this.bookItems = new ArrayList<>();
    }
    
    // ✅ Library CREATES and OWNS BookItems
    public BookItem addBookItemToLibrary(Book book, String barcode) {
        BookItem item = new BookItem(book, barcode);
        bookItems.add(item);
        // Library owns this item exclusively
        return item;
    }
    
    public void removeBookItem(BookItem item) {
        bookItems.remove(item);
        // Item is destroyed (no longer exists)
    }
    
    // When Library is destroyed, ALL BookItems destroyed
    @Override
    protected void finalize() {
        System.out.println("Library destroyed: " + name);
        System.out.println("All " + bookItems.size() + " BookItems destroyed!");
    }
}
```

---

## 📊 Comparison Table: Aggregation vs Composition

| Aspect | Aggregation (◇) | Composition (◆) |
|--------|-----------------|-----------------|
| **Relationship** | Weak "Has-A" | Strong "Has-A" |
| **Example** | Catalog ◇ Book | Book ◆ BookItem |
| **Lifecycle** | Independent | Dependent |
| **Ownership** | Shared/Weak | Exclusive/Strong |
| **Container destroyed** | Contained SURVIVES | Contained DESTROYED |
| **Creation** | External creation | Internal creation |
| **Sharing** | Can be shared | Cannot be shared |
| **UML Symbol** | Hollow diamond (◇) | Filled diamond (◆) |
| **Code pattern** | `list.add(existing)` | `new Child(this)` |
| **Memory** | Independent references | Parent owns memory |
| **Real-world** | Department-Employee | House-Room |

---

## 📊 Side-by-Side Code Comparison

### Aggregation Pattern
```java
// ✅ Object created BEFORE container
Book book = new Book("ISBN001", "Title");

// ✅ Container receives existing object
Catalog catalog = new Catalog();
catalog.addBook(book);  // Adding reference

// ✅ Object can be in multiple containers
Catalog catalog2 = new Catalog();
catalog2.addBook(book);  // Same book, different catalog

// ✅ Destroy container, object survives
catalog = null;
// book still exists! ✅
```

### Composition Pattern
```java
// ✅ Container creates object
Book book = new Book("ISBN001", "Title");

// ✅ Container creates owned object internally
BookItem item = book.addBookItem("BARCODE001");

// ❌ Object CANNOT be in multiple containers
Book book2 = new Book("ISBN002", "Title2");
// book2.addBookItem(item);  // ❌ Wrong!

// ❌ Destroy container, object destroyed too
book = null;
// item is destroyed! ❌
```

---

## 🎨 Real-World Analogies

### Aggregation (◇) - Weak Ownership

#### Analogy 1: Department and Employees
```
🏢 DEPARTMENT ◇──── EMPLOYEE

- Department HAS employees
- Employees can exist without department
- Employee leaves, still exists
- Employee can work in multiple departments
- Weak relationship

Code equivalent:
department.addEmployee(employee);    // Adding existing
department = null;                   // Dept closed
// employee still exists ✅
```

---

#### Analogy 2: Playlist and Songs
```
🎵 PLAYLIST ◇──── SONG

- Playlist HAS songs
- Songs exist independently
- Delete playlist, songs survive
- Same song in multiple playlists
- Weak relationship

Code equivalent:
playlist.addSong(song);    // Adding existing song
playlist = null;           // Playlist deleted
// song still exists ✅
```

---

#### Analogy 3: Library and Books
```
📚 LIBRARY ◇──── BOOK (as entities, not physical items)

- Library catalog HAS books (metadata)
- Books exist as concepts
- Destroy catalog, book info survives
- Book can be in multiple library systems
- Weak relationship
```

---

### Composition (◆) - Strong Ownership

#### Analogy 1: House and Rooms
```
🏠 HOUSE ◆──── ROOM

- House OWNS rooms
- Rooms cannot exist without house
- Destroy house, rooms destroyed
- Room belongs to ONE house only
- Strong relationship

Code equivalent:
house.addRoom("Bedroom");    // House creates room
house = null;                // House demolished
// room is destroyed ❌
```

---

#### Analogy 2: Car and Engine
```
🚗 CAR ◆──── ENGINE

- Car OWNS its engine
- Engine is PART OF the car
- Destroy car, engine destroyed
- Engine belongs to ONE car
- Strong relationship

Code equivalent:
Car car = new Car();         // Car creates engine internally
car = null;                  // Car scrapped
// engine is destroyed ❌
```

---

#### Analogy 3: Book and Chapters
```
📖 BOOK ◆──── CHAPTER

- Book OWNS chapters
- Chapters cannot exist without book
- Delete book, chapters deleted
- Chapter belongs to ONE book
- Strong relationship

Code equivalent:
book.addChapter("Chapter 1");    // Book creates chapter
book = null;                     // Book deleted
// chapters destroyed ❌
```

---

## 🔍 How to Identify in UML

### Aggregation Symbol (◇────)

```
┌──────────────┐          ┌──────────────┐
│   Catalog    │◇─────────│     Book     │
└──────────────┘          └──────────────┘

Hollow diamond at container side
```

**Identifies:**
- Weak ownership
- Shared relationship
- Independent lifecycle
- Part can exist alone

---

### Composition Symbol (◆────)

```
┌──────────────┐          ┌──────────────┐
│     Book     │◆─────────│   BookItem   │
└──────────────┘          └──────────────┘

Filled diamond at container side
```

**Identifies:**
- Strong ownership
- Exclusive relationship
- Dependent lifecycle
- Part cannot exist alone

---

## 💡 Key Decision Factors

### Choose Aggregation When:
- ✅ Objects exist independently
- ✅ Objects can be shared
- ✅ Lifecycle is separate
- ✅ Objects created externally
- ✅ Want loose coupling

**Example:** Department with Employees

---

### Choose Composition When:
- ✅ Objects are parts of whole
- ✅ Objects cannot be shared
- ✅ Lifecycle is dependent
- ✅ Objects created internally
- ✅ Want strong ownership

**Example:** House with Rooms

---

## 🎓 Interview Questions & Answers

### Q1: What's the difference between Aggregation and Composition?

**Answer:**
> *"Both are 'Has-A' relationships. Aggregation is weak - the contained object can exist independently and survive if the container is destroyed. Composition is strong - the contained object is owned exclusively and is destroyed when the container is destroyed. For example, Catalog has Books (aggregation) - books survive if catalog is deleted. But Book has BookItems (composition) - if you delete a book, all its physical copies are deleted too."*

---

### Q2: Give a real-world example of Aggregation.

**Answer:**
> *"A Department and its Employees. The department has employees, but if the department is closed, employees still exist - they can join other departments. Employees are independent entities that are just associated with the department."*

---

### Q3: Give a real-world example of Composition.

**Answer:**
> *"A House and its Rooms. The house owns the rooms - they're part of the house structure. If you demolish the house, the rooms no longer exist. A room cannot exist without the house it's part of."*

---

### Q4: How do you implement Composition in code?

**Answer:**
> *"In composition, the container creates and owns the contained object. For example:"*

```java
class User {
    private LibraryCard card;  // Composition
    
    public User() {
        this.card = new LibraryCard(this);  // User creates card
    }
}
```

> *"The card is created inside User's constructor and cannot exist without the User."*

---

### Q5: Can a composed object be shared between containers?

**Answer:**
> *"No, that's the key difference. In composition, ownership is exclusive. A BookItem belongs to exactly one Book. In aggregation, objects can be shared - a Book can be in multiple Catalogs."*

---

## 📋 Quick Reference Checklist

### Identifying Aggregation:
- [ ] Hollow diamond (◇) in UML
- [ ] Object created externally
- [ ] `container.add(existingObject)`
- [ ] Object can be in multiple containers
- [ ] Destroy container, object survives
- [ ] Weak/shared ownership

### Identifying Composition:
- [ ] Filled diamond (◆) in UML
- [ ] Object created internally
- [ ] `new ContainedObject(this)`
- [ ] Object in ONE container only
- [ ] Destroy container, object destroyed
- [ ] Strong/exclusive ownership

---

## 🎯 Memory Management Summary

### Aggregation Memory Model
```
Heap Memory:

┌─────────────┐         ┌─────────────┐
│   Catalog   │────────►│    Book 1   │◄────┐
│   Object    │         │   Object    │     │
└─────────────┘         └─────────────┘     │
                                            │
┌─────────────┐                             │
│  Catalog 2  │─────────────────────────────┘
│   Object    │         (Same Book shared)
└─────────────┘

Destroy Catalog 1:
Book 1 still referenced by Catalog 2 ✅
Book 1 survives ✅
```

### Composition Memory Model
```
Heap Memory:

┌─────────────┐         ┌─────────────┐
│    Book     │────────►│  BookItem 1 │
│   Object    │────────►│  BookItem 2 │
└─────────────┘         └─────────────┘
      │ owns                   ▲
      │                        │ belongs to
      └────────────────────────┘

Destroy Book:
BookItem 1 destroyed ❌
BookItem 2 destroyed ❌
(No other references exist)
```

---

