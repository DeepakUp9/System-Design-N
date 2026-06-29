package LLD.SolidPrinciple;

// A class should have only one reason to change 

class Marker {
    String name;
    String color;
    int year;
    int price;

    public Marker(String name,String color,int year,int price){
        this.name = name;
        this.color = color;
        this.year = year;
        this.price = price;
    }
}

class Invoice{

    private Marker marker;
    private int quantity;

    public Invoice(Marker marker, int quantity){
        this.marker = marker;
        this.quantity = quantity;
    }

    public int calculateTotal(){
        int price = (marker.price * this.quantity);
        return price;
    }

    public void printInvoice(){
        //print the invoice 
    }

    public void saveToDB(){
         //save the data into db
    }
}

//above thing have many problem in one class we doing many thing So that is not follow SinSingle Responsibility principles 

//let's modified the above code so that it follow the Single Responsibility principles
  // -1 each class has only single Responsibility

class Invoice{
    private Marker marker;
    private int quantity;

    public Invoice(Marker marker,int quantity){
        this.marker = marker;
        this.quantity = quantity;
    }
    
    public int calculateTotal(){
        int price = (marker.price * this.quantity);
        return price;
    }
}

class InvoiceDao{

    Invoice invoice;

    public InvoiceDao(Invoice invoice){
        this.invoice = invoice;
    }

    public void saveToDB(){
         //save the data into db
    }    
}

class InvoicePrinter{

    private Invoice invoice;

    public InvoicePrinter(Invoice invoice){
        this.invoice = invoice;
    }

    public void printInvoice(){
        //print the invoice 
    }
}