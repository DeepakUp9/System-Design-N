package LLD.DesignPattern.behaviouralPattern.iterator.thirdResource.librarymanagementsystem;

public class Book {
    int id;
    String name;
    int price;
    public Book(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getPrice() {
        return price;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    @Override
    public String toString() {
        return String.format("Book [id=%s, name=%s, price=%s]", id, name, price);
    }

    
    

    
}
