import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
  private double totalPrice;
  private List<CartItem> items = new ArrayList<>();

  public boolean addItem(CartItem item) {
    items.add(item);
    totalPrice += item.getPrice();
    return true;
  }

  public boolean removeItem(CartItem item) {
    if (items.remove(item)) {
      totalPrice -= item.getPrice();
      return true;
    }
    return false;
  }

  public List<CartItem> getItems() {
    return items;
  }

  public boolean checkout() {
    System.out.println("Checkout complete.");
    return true;
  }

  public boolean verify() {
    return items != null && !items.isEmpty();
  }

  public double getTotalPrice() {
    return totalPrice;
  }
}
