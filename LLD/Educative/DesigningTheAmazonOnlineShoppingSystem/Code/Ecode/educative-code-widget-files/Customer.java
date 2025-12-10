import java.util.List;

public abstract class Customer {
  protected ShoppingCart cart;
  
  public abstract List<Product> searchProduuct(String name);
}