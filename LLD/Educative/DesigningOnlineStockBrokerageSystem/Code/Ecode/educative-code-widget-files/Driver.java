import java.util.*;

public class Driver {
    public static void main(String[] args) {
        System.out.println("========== ONLINE STOCK BROKERAGE SYSTEM ==========");

        // --------------------------------------------------
        // SYSTEM INITIALIZATION
        // --------------------------------------------------
        System.out.println("\n--- SYSTEM INITIALIZATION ---");

        // Create and configure essential system components
        StockInventory inventory = new StockInventory();
        StockExchange exchange = StockExchange.getInstance();

        // Create and initialize member
        Member member = new Member();
        member.setName("Alice");
        member.setAvailableFundsForTrading(10000.0);

        System.out.println("[INFO] Member Initialized: " + member.getName());
        System.out.println("[INFO] Available Funds: $" + member.getAvailableFundsForTrading());

        // --------------------------------------------------
        // SCENARIO 1: SEARCHING AND SELECTING A STOCK
        // --------------------------------------------------
        System.out.println("\n--- SCENARIO 1: SEARCHING AND SELECTING A STOCK ---");

        String symbolToSearch = "AAPL";
        System.out.println("[STEP] Searching for stock with symbol: " + symbolToSearch);
        Stock stock = inventory.searchSymbol(symbolToSearch);

        // Fallback if stock not found
        if (stock == null) {
            System.out.println("[WARNING] Stock not found in inventory. Creating mock stock data.");
            stock = new Stock();
            stock.setSymbol(symbolToSearch);
            stock.setPrice(180.50);
        }

        System.out.println("[RESULT] Stock Found: " + stock.getSymbol() + " @ $" + stock.getPrice());

        // Member selects the stock
        member.selectStock(stock.getSymbol());
        double quantity = 10;
        System.out.println("[ACTION] Selected Quantity: " + quantity);

        // --------------------------------------------------
        // SCENARIO 2: PLACING A LIMIT ORDER
        // --------------------------------------------------
        System.out.println("\n--- SCENARIO 2: PLACING A LIMIT ORDER ---");

        Order order = new LimitOrder();
        order.setIsBuyOrder(true);
        order.setOrderNumber("ORD123");
        ((LimitOrder) order).setPriceLimit(stock.getPrice() - 5); // Buy at $175.50
        order.setTimeEnforcement(TimeEnforcementType.GOOD_TILL_CANCELED);
        order.setCreationTime(new Date());

        System.out.println("[ORDER] Type: Limit Order");
        System.out.println("[ORDER] Order Number: " + order.getOrderNumber());
        System.out.println("[ORDER] Buy Order: " + order.isBuyOrder);
        System.out.println("[ORDER] Limit Price: $" + ((LimitOrder) order).getPriceLimit());
        System.out.println("[ORDER] Time Enforcement: " + order.getTimeEnforcement());
        System.out.println("[ORDER] Created On: " + order.getCreationTime());

        // Place order through stock exchange
        boolean orderPlaced = exchange.placeOrder(order);
        inventory.sendOrderDetails(order);

        if (orderPlaced) {
            System.out.println("[SUCCESS] Order placed successfully: " + order.getOrderNumber());
        } else {
            System.out.println("[FAILED] Order placement failed.");
        }

        // Acknowledge order by exchange
        boolean ack = exchange.acknowledge(order);
        System.out.println("[EXCHANGE] Order Acknowledged: " + ack);

        // --------------------------------------------------
        // SCENARIO 3: SENDING ORDER NOTIFICATION
        // --------------------------------------------------
        System.out.println("\n--- SCENARIO 3: SENDING ORDER NOTIFICATION ---");

        Notification notification = new EmailNotification();
        notification.setCreationDate(new Date());
        notification.setContent("Your order " + order.getOrderNumber() + " has been received.");
        ((EmailNotification) notification).setEmail("alice@example.com");

        boolean sent = notification.sendNotification();
        System.out.println("[NOTIFICATION] Email sent to " + ((EmailNotification) notification).getEmail() + ": " + sent);

        // --------------------------------------------------
        // SESSION END
        // --------------------------------------------------
        System.out.println("\n===================================================");
        System.out.println("   Thank you for trading with us, " + member.getName());
        System.out.println("===================================================");
    }
}
