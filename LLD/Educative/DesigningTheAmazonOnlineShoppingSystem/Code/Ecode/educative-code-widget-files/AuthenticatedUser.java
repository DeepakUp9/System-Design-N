import java.util.List;

public class AuthenticatedUser extends Customer {
  private Account account;
  private Order order;

  public OrderStatus placeOrder(CartItem item, double amount) {
    // Logic to place an order
    System.out.println("Placing order for item: " + item.getProduct().getName() + " Amount: $" + amount);
    return OrderStatus.PENDING;
  }

  public boolean initiatePayment(double amount) {
    System.out.println("Initiating payment of $" + amount);
    return true;
  }

  public List<Product> searchProduuct(String name) {
    Search search = new Search();
    return search.searchProductsByName(name);
  }

  public boolean addItem(Product product, int quantity) {
    CartItem item = new CartItem(product, quantity, product.getPrice());
    return this.getShoppingCart().addItem(item);
  }
  
  public ShoppingCart getShoppingCart(){
    if (cart == null) {
      cart = new ShoppingCart();
    }
    return cart;
  }
  
}
