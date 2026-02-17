package com.library.models;

import com.library.observers.EmailNotificationObserver;
import com.library.observers.SMSNotificationObserver;
import com.library.services.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Main Library class representing the library system.
 * Central coordination point for all library operations.
 * 
 * DESIGN PATTERN: Singleton Pattern
 * - Ensures only one instance of Library exists
 * - Provides global point of access to library services
 * - Thread-safe implementation using double-checked locking
 * 
 * SOLID PRINCIPLE: Dependency Inversion Principle
 * - Depends on service abstractions
 * - High-level module that coordinates lower-level services
 */
public class Library {
    // Singleton instance (volatile for thread-safety)
    private static volatile Library instance;
    
    // Library metadata
    private final String libraryId;
    private String name;
    private Address address;
    
    // Core components
    private final Catalog catalog;
    private final List<Member> members;
    private final List<Librarian> librarians;
    private final List<Rack> racks;
    
    // Services (following Dependency Injection pattern)
    private final NotificationService notificationService;
    private final BorrowingService borrowingService;
    private final ReservationService reservationService;
    private final RenewalService renewalService;
    private final FineService fineService;
    
    /**
     * Private constructor to prevent instantiation.
     * DESIGN PATTERN: Singleton - private constructor
     */
    private Library() {
        this.libraryId = "LIB-" + System.currentTimeMillis();
        this.name = "Central Library";
        this.catalog = new Catalog();
        this.members = new ArrayList<>();
        this.librarians = new ArrayList<>();
        this.racks = new ArrayList<>();
        
        // Initialize services
        this.notificationService = new NotificationService();
        this.borrowingService = new BorrowingService(notificationService);
        this.reservationService = new ReservationService(notificationService);
        this.renewalService = new RenewalService();
        this.fineService = new FineService(notificationService);
        
        // Register notification observers (Observer Pattern)
        notificationService.attach(new EmailNotificationObserver());
        notificationService.attach(new SMSNotificationObserver());
    }
    
    /**
     * Get singleton instance of Library.
     * DESIGN PATTERN: Singleton - thread-safe double-checked locking
     * 
     * @return The singleton Library instance
     */
    public static Library getInstance() {
        if (instance == null) {
            synchronized (Library.class) {
                if (instance == null) {
                    instance = new Library();
                }
            }
        }
        return instance;
    }
    
    // Getters
    public String getLibraryId() {
        return libraryId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Address getAddress() {
        return address;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }
    
    public Catalog getCatalog() {
        return catalog;
    }
    
    public List<Member> getMembers() {
        return Collections.unmodifiableList(members);
    }
    
    public List<Librarian> getLibrarians() {
        return Collections.unmodifiableList(librarians);
    }
    
    public List<Rack> getRacks() {
        return Collections.unmodifiableList(racks);
    }
    
    // Service getters
    public NotificationService getNotificationService() {
        return notificationService;
    }
    
    public BorrowingService getBorrowingService() {
        return borrowingService;
    }
    
    public ReservationService getReservationService() {
        return reservationService;
    }
    
    public RenewalService getRenewalService() {
        return renewalService;
    }
    
    public FineService getFineService() {
        return fineService;
    }
    
    // Member management
    public void addMember(Member member) {
        if (!members.contains(member)) {
            members.add(member);
        }
    }
    
    public void removeMember(Member member) {
        members.remove(member);
    }
    
    public Member findMemberById(String memberId) {
        return members.stream()
                .filter(m -> m.getUserId().equals(memberId))
                .findFirst()
                .orElse(null);
    }
    
    public Member findMemberByEmail(String email) {
        return members.stream()
                .filter(m -> m.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }
    
    // Librarian management
    public void addLibrarian(Librarian librarian) {
        if (!librarians.contains(librarian)) {
            librarians.add(librarian);
        }
    }
    
    public void removeLibrarian(Librarian librarian) {
        librarians.remove(librarian);
    }
    
    public Librarian findLibrarianById(String librarianId) {
        return librarians.stream()
                .filter(l -> l.getUserId().equals(librarianId))
                .findFirst()
                .orElse(null);
    }
    
    // Rack management
    public void addRack(Rack rack) {
        if (!racks.contains(rack)) {
            racks.add(rack);
        }
    }
    
    public void removeRack(Rack rack) {
        racks.remove(rack);
    }
    
    public Rack findRackByNumber(String rackNumber) {
        return racks.stream()
                .filter(r -> r.getRackNumber().equals(rackNumber))
                .findFirst()
                .orElse(null);
    }
    
    // Book management (delegates to catalog)
    public void addBook(Book book) {
        catalog.addBook(book);
    }
    
    public void removeBook(Book book) {
        catalog.removeBook(book);
    }
    
    public Book findBookByISBN(String isbn) {
        return catalog.findBookByISBN(isbn);
    }
    
    // Statistics and reporting
    public int getTotalBooks() {
        return catalog.getBookCount();
    }
    
    public int getTotalMembers() {
        return members.size();
    }
    
    public int getTotalLibrarians() {
        return librarians.size();
    }
    
    public long getTotalAvailableBooks() {
        return catalog.getAllBooks().stream()
                .mapToLong(Book::getAvailableCopiesCount)
                .sum();
    }
    
    public long getTotalBorrowedBooks() {
        return borrowingService.getAllLendings().stream()
                .filter(lending -> !lending.isReturned())
                .count();
    }
    
    public int getTotalOverdueBooks() {
        return borrowingService.getOverdueLendings().size();
    }
    
    public double getTotalPendingFines() {
        return fineService.getAllPendingFines().stream()
                .mapToDouble(Fine::getAmount)
                .sum();
    }
    
    /**
     * Print library statistics.
     */
    public void printStatistics() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("📊 LIBRARY STATISTICS");
        System.out.println("=".repeat(80));
        System.out.println("Library Name: " + name);
        System.out.println("Library ID: " + libraryId);
        System.out.println("-".repeat(80));
        System.out.println("Total Books in Catalog: " + getTotalBooks());
        System.out.println("Total Book Copies Available: " + getTotalAvailableBooks());
        System.out.println("Total Books Borrowed: " + getTotalBorrowedBooks());
        System.out.println("Total Overdue Books: " + getTotalOverdueBooks());
        System.out.println("-".repeat(80));
        System.out.println("Total Members: " + getTotalMembers());
        System.out.println("Total Librarians: " + getTotalLibrarians());
        System.out.println("-".repeat(80));
        System.out.println("Total Pending Fines: $" + String.format("%.2f", getTotalPendingFines()));
        System.out.println("=".repeat(80));
        System.out.println();
    }
    
    @Override
    public String toString() {
        return "Library{" +
                "libraryId='" + libraryId + '\'' +
                ", name='" + name + '\'' +
                ", totalBooks=" + getTotalBooks() +
                ", totalMembers=" + getTotalMembers() +
                ", totalLibrarians=" + getTotalLibrarians() +
                '}';
    }
}
