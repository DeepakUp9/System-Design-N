import java.util.List;
import java.util.Date;

public class Waiter extends Person {
    public Waiter(String name, String email, String password, Address address, String phone) {
        super(name, email, password, address, phone);
    }

    public boolean addOrder(List<MenuItem> selectedItems) {
        // Logic to add order
        return true;
    }

    public Menu viewMenu() {
        // Logic to return menu
        return null;
    }

    public List<Order> fetchOrderList(List<Order> allOrders) {
        // Logic to fetch order list
        return allOrders;
    }

    public Bill generateBill(int orderId) {
        // Logic to generate bill
        return new Bill(orderId, 100.0f, 10.0f, false);
    }

    public List<Table> searchAvailableTables(Date date){
        // Logic to process payment
        return null;
    }
}
