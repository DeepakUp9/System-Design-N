import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class  Search {
  private HashMap<String, List<Product>> products = new HashMap<>();

  public void setProducts(Map<String, List<Product>> products) {
    this.products = new HashMap<>(products);
  }

  public List<Product> searchProductsByName(String name) {
    return products.getOrDefault(name.toLowerCase(), new java.util.ArrayList<>());
  }

  public List<Product> searchProductsByCategory(String category) {
    return products.getOrDefault(category.toLowerCase(), new java.util.ArrayList<>());
  }

  public Product getProductDetails(String productId) {
    for (List<Product> productList : products.values()) {
      for (Product product : productList) {
        if (product.getProductId().equals(productId)) {
          return product;
        }
      }
    }
    return null;
  }
}