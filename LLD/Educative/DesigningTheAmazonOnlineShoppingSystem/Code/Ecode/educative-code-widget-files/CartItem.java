public class CartItem {
  private Product product;
  private int quantity;

  public CartItem(Product product, int quantity, double price) {
    this.product = product;
    this.quantity = quantity;
  }

  public double getPrice() {
    return product.getPrice() * quantity;
  }

  public boolean updateQuantity(int quantity) {
    this.quantity = quantity;
    return true;
  }

  public Product getProduct() {
    return product;
  }

  public int getQuantity() {
    return quantity;
  }
}