package LLD.DesignPattern.behaviouralPattern.iterator.thirdResource.librarymanagementsystem;

import java.util.List;

public class BookIterator implements Iterator{
    List<Book>books;
    int idx = 0;

    BookIterator(List<Book>books){
        this.books = books;
    }

    @Override
    public boolean hashNext() {
        return idx < this.books.size();
    }

    @Override
    public Object next() {
       if(this.hashNext()){
          return this.books.get(idx++);
       }
       return null;
    }
    
}
