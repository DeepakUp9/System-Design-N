package LLD.DesignPattern.behaviouralPattern.iterator.thirdResource.librarymanagementsystem;

import java.util.List;

public class Solution {
    public static void main(String[] args) {
       List list = List.of(new Book(1, "A", 2), new Book(2, "B", 3), new Book(3, "C", 4), new Book(4, "D", 5));
       Aggregtor library = new Library(list);

       Iterator iterator = library.createIterator();

       while (iterator.hashNext()) {
           System.out.println(iterator.next());
       }

    }
}
