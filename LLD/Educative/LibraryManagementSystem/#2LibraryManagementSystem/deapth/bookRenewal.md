# Book Renewal System Design - Complete Guide

## 🧭 1. Understanding "Book Renewal" Conceptually

Book renewal allows a member to extend the due date of a book they already have issued, provided that:  
✅ The member has not exceeded the maximum number of renewals (say, 2 times)  
✅ The book is not reserved by another member  
✅ The renewal request is made before the due date (or within a grace period)  
✅ The member has no outstanding fines or suspensions  
 
**Interviewer's Question:**
> "How would you design and implement this renewal mechanism efficiently and consistently?"

---

## 🧱 2. Entities Involved

You can model book issuance as a separate entity — **Loan** (or **BookIssue**).

**Entity Relationship:**
```
Member (1) —— (M) Loan —— (1) Book
```

Each **Loan** record represents a book issued to a member. Renewing a book means: updating that Loan's `dueDate` and incrementing `renewalCount`.

---

## 🧩 3. Basic Design Flow (LLD Perspective)

| Step | Description | System Action |
|------|-------------|---------------|
| 1 | Member requests to renew a book | API → `/books/{bookId}/renew` |
| 2 | System checks:<br>- Book status = ISSUED<br>- Member = borrower<br>- No reservation exists<br>- Not exceeded renewal limit | Validate |
| 3 | Extend dueDate (e.g., +14 days)<br>Increment renewalCount | Update DB |
| 4 | Publish event (email/SMS: "Book renewed till X date") | Async notification |

---

## ⚙️ 4. Example Entity — Loan.java

```java
package com.example.lms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "loans")
public class Loan {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Book book;

    @ManyToOne(optional = false)
    private Member member;

    private LocalDate issueDate;
    private LocalDate dueDate;
    private int renewalCount;

    private static final int MAX_RENEWALS = 2;

    public boolean canRenew() {
        return renewalCount < MAX_RENEWALS;
    }

    public void renew() {
        if (!canRenew()) {
            throw new IllegalStateException("Renewal limit exceeded");
        }
        this.dueDate = this.dueDate.plusDays(14); // 2 weeks extension
        this.renewalCount += 1;
    }

    // getters/setters
}
```

---

## 🧩 5. Repository

```java
package com.example.lms.repo;

import com.example.lms.entity.Loan;
import com.example.lms.entity.Book;
import com.example.lms.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    Optional<Loan> findByBookAndMember(Book book, Member member);
}
```

---

## ⚙️ 6. Renewal Logic — RenewalService.java

```java
package com.example.lms.service;

import com.example.lms.entity.*;
import com.example.lms.repo.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RenewalService {

    private final LoanRepository loanRepo;
    private final BookRepository bookRepo;
    private final ReservationRepository reservationRepo;
    private final BookStateService stateService;

    public RenewalService(LoanRepository loanRepo,
                          BookRepository bookRepo,
                          ReservationRepository reservationRepo,
                          BookStateService stateService) {
        this.loanRepo = loanRepo;
        this.bookRepo = bookRepo;
        this.reservationRepo = reservationRepo;
        this.stateService = stateService;
    }

    @Transactional
    public Loan renewBook(Long bookId, Member member) {
        Book book = bookRepo.findByIdForUpdate(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        if (book.getStatus() != BookStatus.ISSUED) {
            throw new IllegalStateException("Book not currently issued.");
        }

        Loan loan = loanRepo.findByBookAndMember(book, member)
                .orElseThrow(() -> new IllegalStateException("Book not issued to this member."));

        // 1️⃣ Check if reserved by someone else
        boolean reserved = reservationRepo.existsByBookAndStatus(book, ReservationStatus.ACTIVE);
        if (reserved) {
            throw new IllegalStateException("Book is reserved by another member; cannot renew.");
        }

        // 2️⃣ Check if within due date
        if (loan.getDueDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Book is overdue; cannot renew without paying fine.");
        }

        // 3️⃣ Check renewal limit
        if (!loan.canRenew()) {
            throw new IllegalStateException("Max renewals reached.");
        }

        // 4️⃣ Perform renewal
        loan.renew();
        return loanRepo.save(loan);
    }
}
```

---

## 🧪 7. Controller

```java
@RestController
@RequestMapping("/api")
public class RenewalController {

    private final RenewalService renewalService;

    public RenewalController(RenewalService renewalService) {
        this.renewalService = renewalService;
    }

    @PostMapping("/books/{bookId}/renew")
    public ResponseEntity<?> renewBook(@PathVariable Long bookId, @RequestBody Member member) {
        Loan updatedLoan = renewalService.renewBook(bookId, member);
        return ResponseEntity.ok("Book renewed successfully. New due date: " + updatedLoan.getDueDate());
    }
}
```

---

## 🧩 8. Add Renewal Notification (Event-Based)

### NotificationService.java

```java
@Service
public class NotificationService {

    @EventListener
    public void onBookRenewed(BookRenewedEvent event) {
        System.out.printf(
            "Email sent to member %d: Your book %d has been renewed until %s%n",
            event.getMemberId(),
            event.getBookId(),
            event.getNewDueDate()
        );
    }
}
```

### BookRenewedEvent.java

```java
public class BookRenewedEvent extends ApplicationEvent {
    private final Long memberId;
    private final Long bookId;
    private final LocalDate newDueDate;

    public BookRenewedEvent(Object source, Long memberId, Long bookId, LocalDate newDueDate) {
        super(source);
        this.memberId = memberId;
        this.bookId = bookId;
        this.newDueDate = newDueDate;
    }
}
```

### Inside RenewalService After Successful Renewal

```java
eventPublisher.publishEvent(new BookRenewedEvent(this, member.getId(), bookId, loan.getDueDate()));
```

---

## 🧠 9. Concurrency & Consistency Considerations

| Issue | Solution |
|-------|----------|
| Two members trying to renew the same book | Only one can, since book is locked via `findByIdForUpdate` |
| Reservation appears after renewal starts | DB lock ensures consistent read of reservation state |
| Member renews after due date | Prevented via date check |
| Member tries more than 2 renewals | Controlled by `renewalCount` + `MAX_RENEWALS` |

---

## 🔁 10. Integration with Earlier Modules

- **State machine:** Still uses `BookStateService` — no status change needed here (book remains ISSUED)
- **Reservation flow:** Prevents renewal when a reservation exists
- **Event handling:** Uses the same async event or Kafka mechanism as before for notifications

---

## 🎯 11. Quick Summary (What to Say in Interview)

> "For book renewal, I model a separate **Loan** entity representing each issued copy, containing fields like `issueDate`, `dueDate`, and `renewalCount`. When a member requests renewal, the system checks that the book is still issued to them, the due date has not passed, the book isn't reserved by another member, and the renewal count hasn't reached the limit. The renewal simply extends the `dueDate` and increments the `renewalCount`.
>
> I ensure consistency using transactional locking on the **Book** record, and I publish an asynchronous event to notify the member of the new due date. This design integrates cleanly with the reservation and state machine modules, keeping everything consistent and extensible."
