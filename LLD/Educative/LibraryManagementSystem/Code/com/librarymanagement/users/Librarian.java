package com.librarymanagement.users;

import com.librarymanagement.models.BookItem;
import com.librarymanagement.models.LibraryCard;
import com.librarymanagement.models.Person;
import com.librarymanagement.system.Library;

/**
 * Librarian (R5): issues/returns books, adds book items, blocks/unblocks members.
 * Class diagram: Delegation pattern — Librarian initiates; actual add/status updates delegated to Library and BookItem.
 */
public class Librarian extends User {
    public Librarian(String id, String password, Person person, LibraryCard card) {
        super(id, password, person, card);
    }

    /** Class diagram: Delegation — add book item delegated to Library (composition). */
    public boolean addBookItem(Library library, BookItem bookItem) {
        if (library == null || bookItem == null) return false;
        library.addBookItem(bookItem);
        return true;
    }

    public boolean blockMember(Member member) {
        member.setStatus(com.librarymanagement.enums.AccountStatus.BLACKLISTED);
        return true;
    }

    public boolean unBlockMember(Member member) {
        member.setStatus(com.librarymanagement.enums.AccountStatus.ACTIVE);
        return true;
    }

    /** Issue book to member (R7, R8). Returns message for member. */
    public String issueBook(Member member, BookItem bookItem) {
        return Library.getInstance().issueBook(this, member, bookItem);
    }

    /** Return book from member. Calculates fine if overdue (R8). */
    public String returnBook(Member member, BookItem bookItem) {
        return Library.getInstance().returnBook(this, member, bookItem);
    }

    /** Reserve book for member when unavailable (R9, R13). */
    public String reserveBook(Member member, BookItem bookItem) {
        return Library.getInstance().reserveBook(this, member, bookItem);
    }

    /** Renew book for member (R11). */
    public String renewBook(Member member, BookItem bookItem) {
        return Library.getInstance().renewBook(this, member, bookItem);
    }
}
