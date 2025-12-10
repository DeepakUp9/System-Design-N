import java.util.ArrayList;
import java.util.List;

public class ProductCategory {
  private String name;
  private String description;
  private List<Product> products = new ArrayList<>();

  public ProductCategory(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public List<Product> getProducts() {
    return products;
  }

  public void addProduct(Product product) {
    products.add(product);
  }
}