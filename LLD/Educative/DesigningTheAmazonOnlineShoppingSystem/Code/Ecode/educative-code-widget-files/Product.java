import java.util.ArrayList;
import java.util.List;

public class Product {
  private String productId;
  private String name;
  private String description;
  private byte[] image;
  private double price;
  private ProductCategory category;
  private List<ProductReview> reviews = new ArrayList<>();
  private int availableItemCount;
  private Account account;

  public Product(String productId, String name, String description, double price, ProductCategory category) {
    this.productId = productId;
    this.name = name;
    this.description = description;
    this.price = price;
    this.category = category;
  }

  public int getAvailableCount() {
    return availableItemCount;
  }

  public int updateAvailableCount() {
    return ++availableItemCount;
  }

  public boolean updatePrice(double newPrice) {
    this.price = newPrice;
    return true;
  }

  public double getPrice() {
    return price;
  }

  public String getProductId() {
    return productId;
  }

  public String getName() {
    return name;
  }

  public List<ProductReview> getReviews() {
    return reviews;
  }
}