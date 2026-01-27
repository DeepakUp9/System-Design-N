package Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.enums.BookStatus;

public class BookItem {
    private String id;
    private Book book;
    private boolean isReferenceOnly;
    private Date borrowed;
    private Date dueDate;
    private double price;
    private BookStatus status;
    private Date dateOfPurchase;
    private Date publicationDate;
    private Rack placedAt;

    public BookItem(String id, Book book, Rack placedAt, double price,
                    Date dateOfPurchase, Date publicationDate) {
        this.id = id;
        this.book = book;
        this.placedAt = placedAt;
        this.price = price;
        this.dateOfPurchase = dateOfPurchase;
        this.publicationDate = publicationDate;
        this.isReferenceOnly = false;
        this.status = BookStatus.AVAILABLE;
    }

    public boolean checkout(String memberId) {
        if (status != BookStatus.AVAILABLE) {
            System.out.println("BookItem not available for checkout.");
            return false;
        }
        status = BookStatus.LOANED;
        borrowed = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(borrowed);
        c.add(Calendar.DATE, 15);
        dueDate = c.getTime();
        System.out.println("BookItem " + id + " checked out to member " + memberId +
                ". Due date: " + new SimpleDateFormat("yyyy-MM-dd").format(dueDate));
        return true;
    }

    public boolean returnBook() {
        if (status != BookStatus.LOANED) {
            System.out.println("BookItem not loaned out.");
            return false;
        }
        status = BookStatus.AVAILABLE;
        borrowed = null;
        dueDate = null;
        System.out.println("BookItem " + id + " returned.");
        return true;
    }

    public boolean reserve() {
        if (status == BookStatus.AVAILABLE) {
            status = BookStatus.RESERVED;
            return true;
        }
        return false;
    }

    public boolean renew() {
        if (status == BookStatus.LOANED && dueDate != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(dueDate);
            c.add(Calendar.DATE, 15);
            dueDate = c.getTime();
            System.out.println("BookItem " + id + " renewed. New due date: " +
                    new SimpleDateFormat("yyyy-MM-dd").format(dueDate));
            return true;
        }
        System.out.println("Cannot renew a book that's not loaned.");
        return false;
    }
    
    // Getters
    public String getId() { return id; }
    public Book getBook() { return book; }
    public BookStatus getStatus() { return status; }
    public Date getDueDate() { return dueDate; }
    public Rack getPlacedAt() { return placedAt; }

    public void setId(String id) {
        this.id = id;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setReferenceOnly(boolean isReferenceOnly) {
        this.isReferenceOnly = isReferenceOnly;
    }

    public void setBorrowed(Date borrowed) {
        this.borrowed = borrowed;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public void setDateOfPurchase(Date dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setPlacedAt(Rack placedAt) {
        this.placedAt = placedAt;
    }

    //Setters
    
}


