package com.librarymanagement.main;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.librarymanagement.enums.BookFormat;
import com.librarymanagement.factory.BookFactory;
import com.librarymanagement.factory.UserFactory;
import com.librarymanagement.models.Address;
import com.librarymanagement.models.Author;
import com.librarymanagement.models.Book;
import com.librarymanagement.models.BookItem;
import com.librarymanagement.models.Person;
import com.librarymanagement.models.Rack;
import com.librarymanagement.notifications.EmailNotification;
import com.librarymanagement.notifications.PostalNotification;
import com.librarymanagement.system.Library;
import com.librarymanagement.users.Librarian;
import com.librarymanagement.users.Member;

/**
 * Driver: end-to-end execution aligned with Class Diagram (Factory, Delegation, Observer, Decorator).
 * Use cases: Search (Catalog contains Book), Issue, Return, Late fine (FineTransaction+Decorator),
 * Reserve, Observer notification, Renew, Notifications, Transaction log.
 */
public class Driver {
    public static void main(String[] args) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            // ---------- SYSTEM INITIALIZATION (Class diagram: Factory pattern) ----------
            System.out.println("\n========== LIBRARY MANAGEMENT SYSTEM - CLASS DIAGRAM DEMO ==========\n");
            Address libAddress = new Address("1 Main St", "Springfield", "State", 12345, "Country");
            Library library = Library.getInstance("Springfield Public Library", libAddress);

            Author author1 = new Author("Jane Austen", libAddress, "austen@email.com", "111", "Novelist");
            Author author2 = new Author("Mark Twain", libAddress, "twain@email.com", "222", "Novelist");

            Book book1 = BookFactory.createBook("ISBN-001", "Pride and Prejudice", "Novel", "Publisher A",
                    sdf.parse("2000-01-01"), "English", 300, BookFormat.HARDCOVER, Arrays.asList(author1));
            Book book2 = BookFactory.createBook("ISBN-002", "Adventures of Huckleberry Finn", "Adventure", "Publisher B",
                    sdf.parse("2005-05-20"), "English", 250, BookFormat.PAPERBACK, Arrays.asList(author2));

            Rack rack1 = new Rack(1, "A1");
            Rack rack2 = new Rack(2, "B2");
            BookItem item1 = BookFactory.createBookItem("BI001", book1, rack1, 30.0, sdf.parse("2020-01-01"), book1.getPublicationDate());
            BookItem item2 = BookFactory.createBookItem("BI002", book2, rack2, 25.0, sdf.parse("2021-06-15"), book2.getPublicationDate());

            Librarian librarian = UserFactory.createLibrarian("LIB001", "pass", new Person("Libby", libAddress, "libby@lib.com", "999"));
            librarian.addBookItem(library, item1);
            librarian.addBookItem(library, item2);

            Member member1 = UserFactory.createMember("MEM001", "pass", new Person("Alice", libAddress, "alice@email.com", "111"));
            Member member2 = UserFactory.createMember("MEM002", "pass", new Person("Bob", libAddress, "bob@email.com", "222"));

            // ---------- USE CASE 1: SEARCH (Class diagram: Catalog contains Book, Search returns List<Book>) ----------
            System.out.println("--- USE CASE 1: Search catalog by title ---");
            List<Book> foundBooks = library.getCatalog().searchByTitle("Pride and Prejudice");
            System.out.println("Search 'Pride and Prejudice': " + foundBooks.size() + " result(s).");

            // ---------- USE CASE 2: ISSUE BOOK (Delegation: Librarian -> Library -> BookItem) ----------
            System.out.println("\n--- USE CASE 2: Librarian issues book to Member ---");
            String result = librarian.issueBook(member1, item1);
            System.out.println("Issue result: " + result);
            System.out.println("Member " + member1.getPerson().getName() + " has " + member1.getTotalBooksCheckedOut() + " book(s) checked out.");

            // ---------- USE CASE 3: RETURN BOOK (ON TIME) ----------
            System.out.println("\n--- USE CASE 3: Member returns book on time ---");
            result = librarian.returnBook(member1, item1);
            System.out.println("Return result: " + result);

            // ---------- USE CASE 4: LATE RETURN (Class diagram: FineTransaction + Decorator pattern) ----------
            System.out.println("\n--- USE CASE 4: Issue book then return late (FineTransaction + Decorator) ---");
            librarian.issueBook(member1, item1);
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, -20);
            item1.setDueDateForDemo(cal.getTime());
            result = librarian.returnBook(member1, item1);
            System.out.println("Return (late) result: " + result);
            System.out.println("Member fines due: $" + String.format("%.2f", member1.getFinesDue()));

            member1.payFine(member1.getFinesDue());
            System.out.println("Fines paid. Outstanding: $" + String.format("%.2f", member1.getFinesDue()));

            // ---------- USE CASE 5: RESERVE (when book is loaned) ----------
            System.out.println("\n--- USE CASE 5: Reserve book (when unavailable) ---");
            librarian.issueBook(member1, item1);
            result = librarian.reserveBook(member2, item1);
            System.out.println("Reserve result: " + result);

            // ---------- USE CASE 6: RETURN -> Observer pattern (notify reserver) ----------
            System.out.println("\n--- USE CASE 6: Return book -> Observer notifies reserver ---");
            result = librarian.returnBook(member1, item1);
            System.out.println("Return result: " + result);

            // ---------- USE CASE 7: RENEW ----------
            System.out.println("\n--- USE CASE 7: Renew book ---");
            librarian.issueBook(member1, item2);
            result = librarian.renewBook(member1, item2);
            System.out.println("Renew result: " + result);

            System.out.println("\n--- USE CASE 8: Pay fine ---");
            System.out.println("Outstanding fines: $" + String.format("%.2f", member1.getFinesDue()));

            // ---------- NOTIFICATIONS (R12) ----------
            System.out.println("\n--- NOTIFICATIONS (R12) ---");
            new EmailNotification("N1", "Your book is overdue. Please return it.", member1.getPerson().getEmail()).sendNotification();
            new PostalNotification("N2", "Reminder: return your book.", member1.getPerson().getAddress()).sendNotification();

            // ---------- TRANSACTION LOG (R1, R10) ----------
            System.out.println("\n--- Transaction log (R1, R10) ---");
            library.getTransactionHistory().forEach(t -> System.out.println(t.getType() + " | " + t.getMember().getId() + " | " + t.getBookItem().getId() + " | " + t.getTransactionDate()));

            System.out.println("\n========== END OF DEMO ==========\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
