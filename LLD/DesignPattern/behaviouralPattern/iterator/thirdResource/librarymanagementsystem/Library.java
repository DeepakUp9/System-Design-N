package LLD.DesignPattern.behaviouralPattern.iterator.thirdResource.librarymanagementsystem;

import java.util.List;

public class Library implements Aggregtor {
    List<Book>boolList;

    Library(List<Book>boolList){
        this.boolList = boolList;
    }

    @Override
    public Iterator createIterator() {
       return new BookIterator(boolList);
    }
    
}
