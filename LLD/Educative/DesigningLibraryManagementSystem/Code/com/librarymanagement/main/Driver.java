package Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.main;

import java.util.*;

import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.enums.BookFormat;
import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models.Address;
import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models.Author;
import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models.Book;
import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models.BookItem;
import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models.LibraryCard;
import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models.Person;
import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models.Rack;
import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.notifications.EmailNotification;
import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.notifications.PostalNotification;
import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.system.Library;
import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.users.Librarian;
import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.users.Member;

import java.text.SimpleDateFormat;

public class Driver {
    public static void main(String[] args) {
        try {
            // ============================================
            // >>> SYSTEM INITIALIZATION
            // ============================================
            System.out.println("\n========== SYSTEM INITIALIZATION ==========\n");
            Address libAddress = new Address("1 Main St", "Springfield", "State", 12345, "Country");
            Library library = Library.getInstance("Springfield Public Library", libAddress);

            // ---> Authors
            Author author1 = new Author("Jane Austen", libAddress, "austen@email.com", "123456", "Famous novelist");
            Author author2 = new Author("Mark Twain", libAddress, "twain@email.com", "234567", "Another novelist");

            // ---> Books and BookItems
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Book book1 = new Book("ISBN123", "Pride and Prejudice", "Novel", "Publisher A",
                    sdf.parse("2000-01-01"), "English", 300, BookFormat.HARDCOVER, Arrays.asList(author1));
            Book book2 = new Book("ISBN456", "Adventures of Huckleberry Finn", "Adventure", "Publisher B",
                    sdf.parse("2005-05-20"), "English", 250, BookFormat.PAPERBACK, Arrays.asList(author2));
            Rack rack1 = new Rack(1, "A1");
            Rack rack2 = new Rack(2, "B2");
            BookItem bookItem1 = new BookItem("BI001", book1, rack1, 30.0, sdf.parse("2020-01-01"), book1.getPublicationDate());
            BookItem bookItem2 = new BookItem("BI002", book2, rack2, 25.0, sdf.parse("2021-06-15"), book2.getPublicationDate());
            library.getCatalog().addBookItem(bookItem1);
            library.getCatalog().addBookItem(bookItem2);

            // ---> Users
            LibraryCard cardMember = new LibraryCard("CARD1001", new Date());
            Person personMember = new Person("Alice", libAddress, "alice@email.com", "345678");
            Member member = new Member("MEM001", "pass", personMember, cardMember);

            LibraryCard cardLibrarian = new LibraryCard("CARD2001", new Date());
            Person personLibrarian = new Person("Libby", libAddress, "libby@email.com", "456789");
            Librarian librarian = new Librarian("LIB001", "pass", personLibrarian, cardLibrarian);

            // ============================================
            // >>> SCENARIO 1: Search and Reserve
            // ============================================
            System.out.println("\n------------------------------");
            System.out.println(">>> SCENARIO 1: Member searches and reserves a book");
            System.out.println("------------------------------\n");

            List<BookItem> foundBooks = library.getCatalog().searchByTitle("Pride and Prejudice");
            if (!foundBooks.isEmpty()) {
                BookItem bookItem = foundBooks.get(0);

                System.out.println("-> Member [" + member.getPerson().getName() + "] attempts to reserve book item [" + bookItem.getId() + "]");
                member.reserveBookItem(bookItem);

                System.out.println("-> Member [" + member.getPerson().getName() + "] checks out reserved book item [" + bookItem.getId() + "]");
                member.checkoutBookItem(bookItem);

            } else {
                System.out.println("Book not found.");
            }

            // ============================================
            // >>> SCENARIO 2: Return Book (Late Return)
            // ============================================
            System.out.println("\n------------------------------");
            System.out.println(">>> SCENARIO 2: Member returns the book late");
            System.out.println("------------------------------\n");

            // Fast forward due date to simulate late return
            Calendar lateCalendar = Calendar.getInstance();
            lateCalendar.setTime(new Date());
            lateCalendar.add(Calendar.DATE, -3); // Simulate due date as 3 days ago
            bookItem1.setDueDate(lateCalendar.getTime()); 

            System.out.println("-> Member [" + member.getPerson().getName() + "] returns book item [" + bookItem1.getId() + "] after due date.");
            member.returnBookItem(bookItem1);

            // ============================================
            // >>> SCENARIO 3: Renew Book
            // ============================================
            System.out.println("\n------------------------------");
            System.out.println(">>> SCENARIO 3: Member renews a book");
            System.out.println("------------------------------\n");

            // Check out again for demonstration
            System.out.println("-> Member [" + member.getPerson().getName() + "] checks out the book again.");
            member.checkoutBookItem(bookItem1);

            System.out.println("-> Member [" + member.getPerson().getName() + "] renews the book.");
            member.renewBookItem(bookItem1);

            // ============================================
            // >>> NOTIFICATIONS
            // ============================================
            System.out.println("\n------------------------------");
            System.out.println(">>> NOTIFICATIONS");
            System.out.println("------------------------------\n");

            EmailNotification emailNotification = new EmailNotification(
                    "N001", "Your book is overdue!", member.getPerson().getEmail());
            emailNotification.sendNotification();

            PostalNotification postalNotification = new PostalNotification(
                    "N002", "Please return your book!", member.getPerson().getAddress());
            postalNotification.sendNotification();

            System.out.println("\n========== END OF DEMO ==========\n");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

