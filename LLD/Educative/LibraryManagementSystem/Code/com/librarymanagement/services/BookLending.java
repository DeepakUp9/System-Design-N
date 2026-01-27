package Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.services;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BookLending {
    private static Map<String, BookLending> lendings = new HashMap<>();
    private String itemId;
    private Date creationDate;
    private Date dueDate;
    private Date returnDate;
    private String memberId;
    
    public BookLending(String itemId, String memberId) {
        this.itemId = itemId;
        this.memberId = memberId;
        this.creationDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(creationDate);
        c.add(Calendar.DATE, 15);
        this.dueDate = c.getTime();
        lendings.put(itemId, this);
    }
    
    public static BookLending lendBook(String bookItemId, String memberId) {
        BookLending lending = new BookLending(bookItemId, memberId);
        System.out.println("BookItem " + bookItemId + " lent to member " + memberId);
        return lending;
    }
    
    public static BookLending fetchLendingDetails(String bookItemId) {
        return lendings.get(bookItemId);
    }
    
    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }
}

