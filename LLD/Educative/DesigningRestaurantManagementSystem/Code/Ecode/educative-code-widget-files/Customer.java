import java.util.Date;
import java.util.List;

public class Customer extends Person {

    public Customer(String name, String email, String password, Address address, String phone) {
        super(name, email, password, address, phone);
    }

    public List<Table> searchAvailableTables() {
        // Logic to search for available tables
        return null;
    }

    public boolean makeReservation(Date date, Date time, Table table) {
        // Logic to make reservation
        return true;
    }

    public Order placeOrder(List<MenuItem> selectedItems) {
        // Logic to place an order
        return null;
    }

    public boolean cancelReservation() {
        // Logic to cancel reservation
        return true;
    }

    public boolean payBill(String paymentMethod) {
        // Logic to pay the bill
        return true;
    }
    
    public List<Table> searchAvailableTables(Date date){
        // Logic to process payment
        return null;
    }
    public Bill requestBillDetails(){
        // Logic to process payment
        return null;
    }
}
