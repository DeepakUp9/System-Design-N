import java.util.*;

public class Driver {
    public static void main(String[] args) {
        System.out.println("=== Restaurant Management System Simulation ===\n");

        // SYSTEM SETUP
        Address address = new Address(12345, "123 Main St", "Springfield", "IL", "USA");

        Waiter waiter = new Waiter("John", "john@restaurant.com", "waiter123", address, "555-9876");
        Receptionist receptionist = new Receptionist("Emma", "emma@restaurant.com", "rec123", address, "555-4567");
        Manager manager = new Manager("Olivia", "olivia@restaurant.com", "mgr456", address, "555-8888");

        Customer customer = new Customer("Alice", "alice@example.com", "cust001", address, "555-1122");
        System.out.println("[1] Customer registered: " + customer.getName());

        // MENU SETUP
        MenuItem pizza = new MenuItem(1, "Pizza", "Cheesy pizza", 12.0);
        MenuItem pasta = new MenuItem(2, "Pasta", "Creamy Alfredo pasta", 10.0);
        MenuSection section = new MenuSection(1, "Main", "Main Course", new ArrayList<>(Arrays.asList(pizza, pasta)));
        Menu menu = new Menu(1, "Dinner Menu", "Evening specials", 0.0, new ArrayList<>(Arrays.asList(section)));
        System.out.println("[2] Menu and items initialized.");

        // TABLE SETUP
        Table table = new Table(101, TableStatus.Free, 4, 1, 4);
        System.out.println("[3] Table created. ID: " + table.getTableID());

        // ------------------------------------------
        // SCENARIO 1: Reservation Flow (Customer → Receptionist → Table)
        // ------------------------------------------
        System.out.println("\n--- SCENARIO 1: Table Reservation ---");

        Reservation reservation = new Reservation(
                1,
                new Date(),  // reservation time
                2,
                ReservationStatus.Requested,
                "Near window",
                null,
                customer,
                new Table[]{table},
                new ArrayList<>()
        );

        boolean isConfirmed = receptionist.confirmAndSaveReservation(reservation);
        if (isConfirmed) {
            reservation.setStatus(ReservationStatus.Confirmed);
            table.setStatus(TableStatus.Reserved);
            System.out.println("Reservation confirmed by receptionist for table " + table.getTableID());
        }

        // ------------------------------------------
        // SCENARIO 2: Order Flow (Customer → Waiter → Meal → Order)
        // ------------------------------------------
        System.out.println("\n--- SCENARIO 2: Placing an Order ---");

        MealItem order1 = new MealItem(1, 1, pizza);
        MealItem order2 = new MealItem(2, 1, pasta);

        Meal meal = new Meal(1, table, 1, new ArrayList<>());
        meal.addMealItem(order1);
        meal.addMealItem(order2);

        Meal[] meals = {meal};
        Order order = new Order(1, OrderStatus.Received, new Date(), meals, table, waiter);
        System.out.println("Order created for table " + table.getTableID() + " with items: Pizza and Pasta");

        // ------------------------------------------
        // SCENARIO 3: Billing and Payment (Waiter → Bill → Payment → Table Status)
        // ------------------------------------------
        System.out.println("\n--- SCENARIO 3: Billing & Payment ---");

        float subtotal = (float)(pizza.getPrice() + pasta.getPrice());
        float tax = subtotal * 0.1f;
        Bill bill = waiter.generateBill(order.getOrderID());

        float total = bill.getAmount() + bill.getTax();
        CreditCard payment = new CreditCard(1, new Date(), total, PaymentStatus.Unpaid, "Alice", 12345);

        // waiter.initiatePayment(bill, payment);
        payment.processPayment();
        payment.updateTableStatus(table);

        System.out.println("Bill paid: $" + total + " | Status: " + payment.getStatus());
        System.out.println("Table status after payment: " + table.getStatus());

        System.out.println("\n=== Simulation Completed ===");
    }
}
