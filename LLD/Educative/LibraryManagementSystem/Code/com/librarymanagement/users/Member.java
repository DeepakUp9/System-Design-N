package Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.users;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models.BookItem;
import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models.LibraryCard;
import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models.Person;
import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.services.Fine;

public class Member extends User {
    private Date dateOfMembership;
    private int totalBooksCheckedOut;
    private List<BookItem> booksBorrowed;
    private double finesDue;

    public Member(String id, String password, Person person, LibraryCard card) {
        super(id, password, person, card);
        this.dateOfMembership = new Date();
        this.totalBooksCheckedOut = 0;
        this.booksBorrowed = new ArrayList<>();
        this.finesDue = 0.0;
    }

    public boolean reserveBookItem(BookItem bookItem) {
        if (bookItem.reserve()) {
            System.out.println("BookItem " + bookItem.getId() + " reserved by member " + getId());
            return true;
        }
        System.out.println("Cannot reserve book; it is not available.");
        return false;
    }

    public boolean checkoutBookItem(BookItem bookItem) {
        if (totalBooksCheckedOut >= 10) {
            System.out.println("Book limit reached.");
            return false;
        }
        if (bookItem.checkout(getId())) {
            booksBorrowed.add(bookItem);
            totalBooksCheckedOut++;
            System.out.println("Member " + getId() + " checked out BookItem " + bookItem.getId());
            return true;
        }
        return false;
    }

    public boolean returnBookItem(BookItem bookItem) {
        if (!booksBorrowed.contains(bookItem)) {
            System.out.println("Book not checked out by member.");
            return false;
        }
        int lateDays = 0;
        if (bookItem.getDueDate() != null) {
            long diffMs = new Date().getTime() - bookItem.getDueDate().getTime();
            lateDays = (int) (diffMs / (1000 * 60 * 60 * 24));
        }
        if (lateDays > 0) {
            double fine = Fine.collectFine(getId(), lateDays);
            finesDue += fine;
            System.out.printf("Fine of $%.2f applied for %d late days.\n", fine, lateDays);
        }
        bookItem.returnBook();
        booksBorrowed.remove(bookItem);
        totalBooksCheckedOut--;
        return true;
    }

    public boolean renewBookItem(BookItem bookItem) {
        if (booksBorrowed.contains(bookItem)) {
            return bookItem.renew();
        }
        System.out.println("This member has not checked out the book.");
        return false;
    }
    
    // Getters
    public double getFinesDue() { return finesDue; }
    public List<BookItem> getBooksBorrowed() { return new ArrayList<>(booksBorrowed); }
}


