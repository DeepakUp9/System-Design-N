import java.util.*;

public class Driver {
  public static void main(String[] args) {
    System.out.println("========== Welcome to the Amazon Online Shopping System ==========\n");

    // Scenario 1: Guest Browses and Registers
    System.out.println("==== Scenario 1: Guest Browses and Registers ====");
    Guest guest = new Guest();
    System.out.println("🧑 Guest browses the platform...");
    guest.registerAccount("john_doe", "password123", "john@example.com");
    
    // Scenario 2: Authenticated User Adds Items to Cart and Places an Order
    System.out.println("\n==== Scenario 2: Authenticated User Adds Items to Cart and Places an Order ====");
    
    // 2.1. Authenticated User Setup
    Account johnAccount = new Account();
    johnAccount.setStatus(AccountStatus.ACTIVE);
    AuthenticatedUser john = new AuthenticatedUser();
    
    // 2.2. Create Product Catalog
    Search search = new Search();
    ProductCategory electronics = new ProductCategory("Electronics", "Devices and gadgets");
    Product laptop = new Product("P001", "Gaming Laptop", "Powerful 16GB RAM laptop", 1500.00, electronics);
    Product mouse = new Product("P002", "Wireless Mouse", "Ergonomic wireless mouse", 25.00, electronics);
    
    // Manually populate product catalog
    List<Product> electronicsList = new ArrayList<>();
    electronicsList.add(laptop);
    electronicsList.add(mouse);
    Map<String, List<Product>> productMap = new HashMap<>();
    productMap.put("electronics", electronicsList);
    search.setProducts(productMap);

    System.out.println("📦 Products available in 'Electronics':");
    for (Product p : search.searchProductsByCategory("electronics")) {
      System.out.println(" - " + p.getName() + ": $" + p.getPrice());
    }

    // 2.3. User adds items to shopping cart
    System.out.println("\n🛒 User adds items to shopping cart:");
    john.addItem(laptop, 1);
    john.addItem(mouse, 2);

    // 2.4. Show cart summary
    System.out.println("\n🧾 Shopping Cart Summary:");
    ShoppingCart cart = john.getShoppingCart();
    for (CartItem item : cart.getItems()) {
      System.out.println(" - " + item.getProduct().getName() + " x " + item.getQuantity() + " = $" + item.getPrice());
    }
    System.out.println("Total: $" + cart.getTotalPrice());

    // 2.5. Verify cart and place order
    if (cart.verify()) {
      System.out.println("\n✅ Cart verified. Proceeding to place order...");
    }
    CartItem itemToOrder = cart.getItems().get(0);
    OrderStatus status = john.placeOrder(itemToOrder, itemToOrder.getPrice());

    // Scenario 3: Order Payment with Credit Card
    System.out.println("\n==== Scenario 3: Order Payment with Credit Card ====");
    // 3.1. Process payment using CreditCard
    Payment payment = new CreditCard(itemToOrder.getPrice(), "John Doe", "1234567812345678", "123 Street, NY", 123);

    // 3.2. Order and Shipment
    Order order = new Order("ORD123", status);
    PaymentStatus paymentStatus = order.makePayment(payment);

    Shipment shipment = new Shipment("SHIP123", "FedEx", new Date(System.currentTimeMillis() + 3 * 86400000), ShipmentStatus.PENDING);

    // 3.3. Display Shipment and Payment Status
    // System.out.println("\n📦 Shipment Details:");
    // for (ShipmentLog log : shipment.getShipmentLogs()) {
    //   System.out.println(" - Status: " + log.getStatus() + ", Logged on: " + log.getCreationDate());
    // }

    System.out.println("\n💳 Payment Status: " + paymentStatus);
    System.out.println("📑 Order Status: " + status);

    System.out.println("\n🎉 Thank you for shopping with us!");
    System.out.println("=====================================================");
  }
}