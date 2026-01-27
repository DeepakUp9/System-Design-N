# Class Diagram - Multiplicity and Relationships Guide

Perfect — this is a big topic and it's one of the most important in LLD interviews. Let's break it down slowly and deeply so that you'll fully understand the "Multiplicity" and Relationships part of the Library Management System (LMS) class diagram.

---

## 🧩 What is Multiplicity in UML?

**Multiplicity** defines how many instances of one class are associated with another in a relationship.
It's basically a "quantity" constraint on both sides of an association.

Let's take a simple real-world example:

| Real-world example | Multiplicity meaning |
|--------------------|---------------------|
| One Library can have many Books. | Library (1) → Book (1..*) |
| Each Book can have multiple BookItems (copies). | Book (1) → BookItem (1..*) |
| Each User has exactly one LibraryCard. | User (1) → LibraryCard (1) |
| One Rack can hold zero or many BookItems. | Rack (1) → BookItem (0..*) |

---

## 🔍 Step-by-Step: Understanding Each Multiplicity in LMS

We'll go through each one in the order given in your notes.

---

### 1️⃣ Library → BookItem

**Multiplicity:** `1 -- 1..*`

**Meaning:**
- Every library must have at least one book item.
- A book item cannot exist without a library (composition).

**Example:**
```
Central Library → owns → [BookItem1, BookItem2, BookItem3]
```

If the library is deleted, all its book items are deleted too (composition relationship).

---

### 2️⃣ Rack → BookItem

**Multiplicity:** `1 -- 0..*`

**Meaning:**
- A rack can contain zero or many book items.
- A book item belongs to exactly one rack.

**Example:**
```
Rack #12 → [BookItem(A), BookItem(B), BookItem(C)]
```

If a rack is removed (say the section is gone), the physical mapping of book items needs to be updated.

---

### 3️⃣ Book → BookItem

**Multiplicity:** `1 -- 1..*`

**Meaning:**
- A single Book (like "Clean Code") can have many physical copies (BookItems).
- Each BookItem is always tied to exactly one Book.

**Example:**
```
Book: Clean Code
→ BookItems: #B101, #B102, #B103
```

So:
- **Book** = logical concept (metadata: title, ISBN, author)
- **BookItem** = physical copy (with barcode, rack location, and status)

---

### 4️⃣ Book ↔ Author

**Multiplicity:** `1..* -- 1..*`

**Meaning:**
- A Book can have one or many Authors (e.g., co-authored books).
- An Author can write one or many Books.

**Example:**
```
Book: Clean Architecture → Author: Robert C. Martin  
Book: Agile Software Development → Author: Robert C. Martin  
```

This is a **many-to-many** relationship.

---

### 5️⃣ User → LibraryCard

**Multiplicity:** `1 -- 1`

**Meaning:**
- Every User has exactly one LibraryCard.
- Each LibraryCard belongs to exactly one User.

This is a **one-to-one** relationship.

**Example:**
```
User: Deepak → LibraryCard: LC987654
```

---

### 6️⃣ User → BookLending

**Multiplicity:** `1 -- 0..*`

**Meaning:**
- A user can have zero or many book lending records.
- Each lending record belongs to exactly one user.

**Example:**
```
User: Deepak
→ BookLending: [Lending #L1001, Lending #L1002]
```

If the user hasn't borrowed any books yet, count = 0.

---

### 7️⃣ BookReservation → BookItem

**Multiplicity:** `1 -- 0..1`

**Meaning:**
- A reservation is made for a specific book item (one physical copy).
- A book item can have at most one active reservation at a time.

**Example:**
```
BookItem: #B103 → Reserved by Member Deepak
```

If another user tries to reserve it, the system checks the existing reservation first (FCFS rule).

---

### 8️⃣ Catalog → Book

**Multiplicity:** `1 -- 1..*`

**Meaning:**
- Each catalog (like "Programming", "Science", "History") contains one or more Books.
- A Book appears in exactly one catalog category.

**Example:**
```
Catalog: "Programming" → Books: [Clean Code, Design Patterns, Refactoring]
```

---

### 9️⃣ Person → Author

**Multiplicity:** `1 -- 1..*` (Inheritance)

**Meaning:**
- Every Author is a type of Person.
- A Person may have one or many Author roles (in advanced modeling, e.g., pseudonyms).

This uses **inheritance**, not association.

---

## 🧭 Visual Summary Table

| Source | Target | Multiplicity | Meaning |
|--------|--------|--------------|---------|
| Library | BookItem | 1 -- 1..* | Library must have at least one book item |
| Rack | BookItem | 1 -- 0..* | Rack can have multiple book items |
| Book | BookItem | 1 -- 1..* | Each book can have multiple physical copies |
| Book | Author | 1..* -- 1..* | Books can have multiple authors and vice versa |
| User | LibraryCard | 1 -- 1 | One-to-one relationship |
| User | BookLending | 1 -- 0..* | User can borrow many books |
| BookReservation | BookItem | 1 -- 0..1 | Book item can be reserved by one member at a time |
| Catalog | Book | 1 -- 1..* | Each catalog contains multiple books |
| Person | Author | 1 -- 1..* | Author inherits from Person |

---

## ⚙️ Implementation Glimpse (Example in Java)

Here's how one of these multiplicities (Book → BookItem) would look in Spring Boot JPA terms:

```java
@Entity
class Book {
    @Id
    private String isbn;
    private String title;
    private String author;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BookItem> bookItems;
}

@Entity
class BookItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String barcode;
    private String rackNumber;
    private String status; // AVAILABLE, RESERVED, ISSUED

    @ManyToOne
    @JoinColumn(name = "book_isbn")
    private Book book;
}
```

👉 This code enforces:
- One book → many book items
- Each BookItem knows exactly which Book it belongs to.
- If a Book is deleted, all its BookItems are automatically deleted (composition).
