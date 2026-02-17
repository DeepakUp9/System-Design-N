package com.library;

import com.library.enums.BookFormat;
import com.library.factories.BookFactory;
import com.library.factories.LibraryFactory;
import com.library.factories.UserFactory;
import com.library.models.*;
import com.library.services.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Main driver class demonstrating end-to-end Library Management System functionality.
 * 
 * This class demonstrates:
 * - Factory Pattern for object creation
 * - Singleton Pattern for Library instance
 * - Observer Pattern for notifications
 * - Strategy Pattern for search operations
 * - Complete workflow: borrowing, returning, reserving, renewing, fine management
 * - SOLID principles in action
 * 
 * @author Library Management System
 * @version 1.0
 */
public class LibraryManagementSystemDemo {
    
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("🏛️  LIBRARY MANAGEMENT SYSTEM - PRODUCTION READY IMPLEMENTATION");
        System.out.println("=".repeat(80));
        System.out.println("Demonstrating: Factory, Singleton, Observer, Strategy Design Patterns");
        System.out.println("Following: SOLID Principles & Clean Code Architecture");
        System.out.println("=".repeat(80));
        System.out.println();
        
        try {
            // Step 1: Initialize Library (Singleton Pattern)
            Library library = initializeLibrary();
            
            // Step 2: Setup Library Infrastructure
            setupLibraryInfrastructure(library);
            
            // Step 3: Add Books to Catalog (Factory Pattern)
            addBooksToLibrary(library);
            
            // Step 4: Register Users (Factory Pattern)
            Member alice = registerMember(library, "Alice Johnson", "alice@email.com", "123-456-7890");
            Member bob = registerMember(library, "Bob Smith", "bob@email.com", "098-765-4321");
            Librarian sarah = registerLibrarian(library, "Sarah Williams", "sarah@library.com", "555-111-2222");
            
            // Step 5: Demonstrate Search Functionality (Strategy Pattern)
            demonstrateSearchOperations(library);
            
            // Step 6: Demonstrate Borrowing Workflow
            demonstrateBorrowingWorkflow(library, alice, sarah);
            
            // Step 7: Demonstrate Reservation System
            demonstrateReservationSystem(library, bob, alice, sarah);
            
            // Step 8: Demonstrate Renewal System
            demonstrateRenewalSystem(library, alice, sarah);
            
            // Step 9: Demonstrate Return and Fine Management
            demonstrateReturnAndFineManagement(library, alice, sarah);
            
            // Step 10: Demonstrate Search by Different Criteria (Strategy Pattern)
            demonstrateAdvancedSearch(library);
            
            // Step 11: Print Final Statistics
            library.printStatistics();
            
            // Step 12: Summary
            printSummary();
            
        } catch (Exception e) {
            System.err.println("❌ Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Initialize Library using Singleton Pattern.
     */
    private static Library initializeLibrary() {
        System.out.println("📌 STEP 1: Initializing Library (Singleton Pattern)");
        System.out.println("-".repeat(80));
        
        // DESIGN PATTERN: Singleton - only one instance of Library
        Library library = Library.getInstance();
        
        // Verify singleton
        Library sameInstance = Library.getInstance();
        System.out.println("✅ Singleton verified: " + (library == sameInstance));
        
        library.setName("City Central Library");
        Address libraryAddress = LibraryFactory.createAddress(
            "123 Main Street",
            "New York",
            "NY",
            "10001",
            "USA"
        );
        library.setAddress(libraryAddress);
        
        System.out.println("Library initialized: " + library.getName());
        System.out.println("Location: " + libraryAddress);
        System.out.println();
        
        return library;
    }
    
    /**
     * Setup library infrastructure (racks, locations).
     */
    private static void setupLibraryInfrastructure(Library library) {
        System.out.println("📌 STEP 2: Setting up Library Infrastructure");
        System.out.println("-".repeat(80));
        
        // DESIGN PATTERN: Factory - creating Rack objects
        Rack rack1 = LibraryFactory.createRack("A-001", "Floor 1, Section A", 1);
        Rack rack2 = LibraryFactory.createRack("A-002", "Floor 1, Section A", 2);
        Rack rack3 = LibraryFactory.createRack("B-001", "Floor 2, Section B", 1);
        
        library.addRack(rack1);
        library.addRack(rack2);
        library.addRack(rack3);
        
        System.out.println("✅ Added " + library.getRacks().size() + " racks to library");
        library.getRacks().forEach(rack -> System.out.println("   - " + rack));
        System.out.println();
    }
    
    /**
     * Add books to library catalog using Factory Pattern.
     */
    private static void addBooksToLibrary(Library library) {
        System.out.println("📌 STEP 3: Adding Books to Catalog (Factory Pattern)");
        System.out.println("-".repeat(80));
        
        // DESIGN PATTERN: Factory - creating Author and Book objects
        
        // Book 1: Clean Code
        Author robertMartin = LibraryFactory.createAuthor("Robert C. Martin", "Software craftsman");
        Book cleanCode = BookFactory.createDetailedBook(
            "978-0-13-468599-1",
            "Clean Code: A Handbook of Agile Software Craftsmanship",
            Arrays.asList(robertMartin),
            "Software Engineering",
            "Prentice Hall",
            LocalDate.of(2008, 8, 1),
            464,
            "English",
            BookFormat.HARDCOVER
        );
        library.addBook(cleanCode);
        
        // Add 3 copies
        Rack rackA1 = library.findRackByNumber("A-001");
        BookFactory.createMultipleBookItems(cleanCode, rackA1, 45.99, 3);
        System.out.println("✅ Added: " + cleanCode.getTitle() + " (3 copies)");
        
        // Book 2: Design Patterns
        Author gangOfFour = LibraryFactory.createAuthor("Erich Gamma", "Software design expert");
        Book designPatterns = BookFactory.createDetailedBook(
            "978-0-20-163361-0",
            "Design Patterns: Elements of Reusable Object-Oriented Software",
            Arrays.asList(gangOfFour),
            "Software Engineering",
            "Addison-Wesley",
            LocalDate.of(1994, 10, 31),
            395,
            "English",
            BookFormat.HARDCOVER
        );
        library.addBook(designPatterns);
        BookFactory.createMultipleBookItems(designPatterns, rackA1, 54.99, 2);
        System.out.println("✅ Added: " + designPatterns.getTitle() + " (2 copies)");
        
        // Book 3: The Pragmatic Programmer
        Author andrewHunt = LibraryFactory.createAuthor("Andrew Hunt", "Pragmatic programmer");
        Book pragmaticProgrammer = BookFactory.createDetailedBook(
            "978-0-13-595705-9",
            "The Pragmatic Programmer: Your Journey to Mastery",
            Arrays.asList(andrewHunt),
            "Software Engineering",
            "Addison-Wesley",
            LocalDate.of(2019, 9, 13),
            352,
            "English",
            BookFormat.PAPERBACK
        );
        library.addBook(pragmaticProgrammer);
        Rack rackA2 = library.findRackByNumber("A-002");
        BookFactory.createMultipleBookItems(pragmaticProgrammer, rackA2, 39.99, 2);
        System.out.println("✅ Added: " + pragmaticProgrammer.getTitle() + " (2 copies)");
        
        // Book 4: Effective Java
        Author joshuaBloch = LibraryFactory.createAuthor("Joshua Bloch", "Java architect");
        Book effectiveJava = BookFactory.createDetailedBook(
            "978-0-13-468599-7",
            "Effective Java",
            Arrays.asList(joshuaBloch),
            "Programming",
            "Addison-Wesley",
            LocalDate.of(2017, 12, 27),
            416,
            "English",
            BookFormat.HARDCOVER
        );
        library.addBook(effectiveJava);
        BookFactory.createMultipleBookItems(effectiveJava, rackA2, 44.99, 2);
        System.out.println("✅ Added: " + effectiveJava.getTitle() + " (2 copies)");
        
        System.out.println("\nTotal books in catalog: " + library.getTotalBooks());
        System.out.println("Total available copies: " + library.getTotalAvailableBooks());
        System.out.println();
    }
    
    /**
     * Register a member using Factory Pattern.
     */
    private static Member registerMember(Library library, String name, String email, String phone) {
        Address address = LibraryFactory.createAddress("100 Member St", "New York", "NY", "10002", "USA");
        
        // DESIGN PATTERN: Factory - creating Member
        Member member = UserFactory.createMember(name, email, phone, address);
        library.addMember(member);
        
        System.out.println("✅ Member registered: " + member.getName() + 
                         " (Card: " + member.getLibraryCard().getCardNumber() + ")");
        return member;
    }
    
    /**
     * Register a librarian using Factory Pattern.
     */
    private static Librarian registerLibrarian(Library library, String name, String email, String phone) {
        Address address = LibraryFactory.createAddress("200 Staff Ave", "New York", "NY", "10003", "USA");
        
        // DESIGN PATTERN: Factory - creating Librarian
        Librarian librarian = UserFactory.createLibrarian(name, email, phone, address, "Circulation");
        library.addLibrarian(librarian);
        
        System.out.println("✅ Librarian registered: " + librarian.getName() + 
                         " (Employee ID: " + librarian.getEmployeeId() + ")");
        return librarian;
    }
    
    /**
     * Demonstrate search operations using Strategy Pattern.
     */
    private static void demonstrateSearchOperations(Library library) {
        System.out.println("📌 STEP 5: Search Functionality (Strategy Pattern)");
        System.out.println("-".repeat(80));
        
        Catalog catalog = library.getCatalog();
        
        // DESIGN PATTERN: Strategy - different search algorithms
        
        // Search by title
        System.out.println("🔍 Searching by title 'Clean Code':");
        List<Book> titleResults = catalog.searchByTitle("Clean Code");
        titleResults.forEach(book -> System.out.println("   - " + book.getTitle()));
        
        // Search by author
        System.out.println("\n🔍 Searching by author 'Martin':");
        List<Book> authorResults = catalog.searchByAuthor("Martin");
        authorResults.forEach(book -> System.out.println("   - " + book.getTitle() + " by " + book.getAuthorNames()));
        
        // Search by subject
        System.out.println("\n🔍 Searching by subject 'Software Engineering':");
        List<Book> subjectResults = catalog.searchBySubject("Software Engineering");
        System.out.println("   Found " + subjectResults.size() + " books");
        
        // Search by publication year
        System.out.println("\n🔍 Searching by publication year '2008':");
        List<Book> yearResults = catalog.searchByPublicationDate("2008");
        yearResults.forEach(book -> System.out.println("   - " + book.getTitle() + 
                                                      " (" + book.getPublicationDate().getYear() + ")"));
        System.out.println();
    }
    
    /**
     * Demonstrate borrowing workflow.
     */
    private static void demonstrateBorrowingWorkflow(Library library, Member alice, Librarian sarah) {
        System.out.println("📌 STEP 6: Borrowing Workflow");
        System.out.println("-".repeat(80));
        
        Book cleanCode = library.findBookByISBN("978-0-13-468599-1");
        BookItem bookItem = cleanCode.getAvailableCopy();
        
        System.out.println("Alice wants to borrow: " + cleanCode.getTitle());
        System.out.println("Available copies: " + cleanCode.getAvailableCopiesCount());
        System.out.println("Alice's current borrowed books: " + alice.getBorrowedBooksCount());
        System.out.println();
        
        // OBSERVER PATTERN: Notifications will be sent automatically
        BorrowingService borrowingService = library.getBorrowingService();
        BookLending lending = borrowingService.borrowBook(alice, bookItem, sarah);
        
        System.out.println("Updated state:");
        System.out.println("   Book status: " + bookItem.getStatus());
        System.out.println("   Due date: " + bookItem.getDueDate());
        System.out.println("   Alice's borrowed books: " + alice.getBorrowedBooksCount());
        System.out.println();
    }
    
    /**
     * Demonstrate reservation system.
     */
    private static void demonstrateReservationSystem(Library library, Member bob, Member alice, Librarian sarah) {
        System.out.println("📌 STEP 7: Reservation System (FIFO Queue)");
        System.out.println("-".repeat(80));
        
        // Alice already borrowed Clean Code, Bob wants to reserve it
        Book cleanCode = library.findBookByISBN("978-0-13-468599-1");
        BookItem borrowedItem = cleanCode.getBookItems().stream()
                .filter(item -> item.getBorrowedBy() != null && 
                              item.getBorrowedBy().equals(alice))
                .findFirst()
                .orElse(null);
        
        if (borrowedItem != null) {
            System.out.println("Bob wants to reserve: " + cleanCode.getTitle());
            System.out.println("Book status: " + borrowedItem.getStatus());
            System.out.println();
            
            // OBSERVER PATTERN: Bob will be notified when book is available
            ReservationService reservationService = library.getReservationService();
            BookReservation reservation = reservationService.reserveBook(bob, borrowedItem);
            
            System.out.println("Reservation created:");
            System.out.println("   Status: " + reservation.getStatus());
            System.out.println("   Queue position: " + borrowedItem.getReservationCount());
            System.out.println();
        }
    }
    
    /**
     * Demonstrate renewal system.
     */
    private static void demonstrateRenewalSystem(Library library, Member alice, Librarian sarah) {
        System.out.println("📌 STEP 8: Renewal System");
        System.out.println("-".repeat(80));
        
        // Get Alice's borrowed book
        List<BookLending> aliceLendings = library.getBorrowingService().getActiveLendings(alice);
        if (!aliceLendings.isEmpty()) {
            BookLending lending = aliceLendings.get(0);
            BookItem bookItem = lending.getBookItem();
            
            System.out.println("Alice wants to renew: " + bookItem.getBook().getTitle());
            System.out.println("Current due date: " + bookItem.getDueDate());
            System.out.println("Renewal count: " + lending.getRenewalCount() + " / 2");
            System.out.println();
            
            try {
                // Note: This will fail because book is reserved by Bob
                RenewalService renewalService = library.getRenewalService();
                LocalDate newDueDate = renewalService.renewBook(alice, bookItem, lending);
                System.out.println("New due date: " + newDueDate);
            } catch (Exception e) {
                System.out.println("⚠️  Renewal failed: " + e.getMessage());
                System.out.println("   (Book is reserved by another member)");
            }
            System.out.println();
        }
    }
    
    /**
     * Demonstrate return and fine management.
     */
    private static void demonstrateReturnAndFineManagement(Library library, Member alice, Librarian sarah) {
        System.out.println("📌 STEP 9: Return and Fine Management");
        System.out.println("-".repeat(80));
        
        // Get Alice's borrowed book
        List<BookLending> aliceLendings = library.getBorrowingService().getActiveLendings(alice);
        if (!aliceLendings.isEmpty()) {
            BookLending lending = aliceLendings.get(0);
            BookItem bookItem = lending.getBookItem();
            
            System.out.println("Alice is returning: " + bookItem.getBook().getTitle());
            System.out.println("Book status before return: " + bookItem.getStatus());
            System.out.println("Has reservations: " + bookItem.hasReservations());
            System.out.println();
            
            // OBSERVER PATTERN: Notifications sent to next person in reservation queue
            BorrowingService borrowingService = library.getBorrowingService();
            FineService fineService = library.getFineService();
            
            Fine fine = borrowingService.returnBook(alice, bookItem, sarah, fineService);
            
            System.out.println("Book returned:");
            System.out.println("   New status: " + bookItem.getStatus());
            System.out.println("   Fine issued: " + (fine != null ? "$" + fine.getAmount() : "None"));
            System.out.println("   Alice's outstanding fines: $" + alice.getOutstandingFines());
            System.out.println();
            
            // Pay fine if exists
            if (fine != null && fine.getAmount() > 0) {
                System.out.println("Alice is paying the fine...");
                fineService.payFine(alice, fine, fine.getAmount());
            }
        }
    }
    
    /**
     * Demonstrate advanced search with Strategy Pattern.
     */
    private static void demonstrateAdvancedSearch(Library library) {
        System.out.println("📌 STEP 10: Advanced Search (Strategy Pattern)");
        System.out.println("-".repeat(80));
        
        Catalog catalog = library.getCatalog();
        
        System.out.println("🔍 All available books:");
        List<Book> availableBooks = catalog.getAvailableBooks();
        availableBooks.forEach(book -> 
            System.out.println("   - " + book.getTitle() + 
                             " (" + book.getAvailableCopiesCount() + " available)")
        );
        
        System.out.println("\n🔍 Books published after 2010:");
        catalog.getAllBooks().stream()
                .filter(book -> book.getPublicationDate() != null && 
                              book.getPublicationDate().getYear() > 2010)
                .forEach(book -> System.out.println("   - " + book.getTitle() + 
                                                   " (" + book.getPublicationDate().getYear() + ")"));
        System.out.println();
    }
    
    /**
     * Print comprehensive summary.
     */
    private static void printSummary() {
        System.out.println("📌 DESIGN PATTERNS & PRINCIPLES DEMONSTRATED");
        System.out.println("=".repeat(80));
        
        System.out.println("✅ DESIGN PATTERNS IMPLEMENTED:");
        System.out.println("   1. Singleton Pattern - Library class (thread-safe)");
        System.out.println("   2. Factory Pattern - UserFactory, BookFactory, LibraryFactory");
        System.out.println("   3. Observer Pattern - Notification system with multiple observers");
        System.out.println("   4. Strategy Pattern - Search algorithms (Title, Author, Subject, Date)");
        System.out.println("   5. State Pattern - BookItem status transitions");
        System.out.println("   6. Composition Pattern - Book-BookItem, User-LibraryCard");
        System.out.println();
        
        System.out.println("✅ SOLID PRINCIPLES APPLIED:");
        System.out.println("   S - Single Responsibility: Each service handles one concern");
        System.out.println("   O - Open/Closed: Extensible through interfaces (Strategy, Observer)");
        System.out.println("   L - Liskov Substitution: Member and Librarian can substitute User");
        System.out.println("   I - Interface Segregation: Focused interfaces (SearchStrategy, NotificationObserver)");
        System.out.println("   D - Dependency Inversion: Services depend on abstractions");
        System.out.println();
        
        System.out.println("✅ REQUIREMENTS FULFILLED:");
        System.out.println("   R1-R4: Book and member data management with transaction logging");
        System.out.println("   R5-R6: User roles with library cards");
        System.out.println("   R7-R8: Borrowing limits (10 books, 15 days)");
        System.out.println("   R9-R11: Reservation and renewal with policy enforcement");
        System.out.println("   R12-R13: Notifications and reservation system");
        System.out.println("   R14: Multi-criteria search functionality");
        System.out.println();
        
        System.out.println("=".repeat(80));
        System.out.println("🎉 LIBRARY MANAGEMENT SYSTEM DEMO COMPLETED SUCCESSFULLY!");
        System.out.println("=".repeat(80));
        System.out.println();
    }
}
