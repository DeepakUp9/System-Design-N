package Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.users;

import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.enums.AccountStatus;
import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models.BookItem;
import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models.LibraryCard;
import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models.Person;
import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.search.Catalog;

public class Librarian extends User {
    public Librarian(String id, String password, Person person, LibraryCard card) {
        super(id, password, person, card);
    }

    public boolean addBookItem(Catalog catalog, BookItem bookItem) {
        catalog.addBookItem(bookItem);
        System.out.println("BookItem " + bookItem.getId() + " added to catalog by librarian " + getId());
        return true;
    }

    public boolean blockMember(Member member) {
        member.setStatus(AccountStatus.BLACKLISTED);
        System.out.println("Member " + member.getId() + " is now blacklisted.");
        return true;
    }

    public boolean unBlockMember(Member member) {
        member.setStatus(AccountStatus.ACTIVE);
        System.out.println("Member " + member.getId() + " is now active.");
        return true;
    }
}
