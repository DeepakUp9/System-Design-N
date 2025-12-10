import java.util.List;

public class Account {
  private String userName;
  private String password;
  private String name;
  private List<Address> shippingAddress;
  private AccountStatus status;
  private String email;
  private String phone;

  private List<CreditCard> creditCards;
  private List<ElectronicBankTransfer> bankAccounts;

  public Address getShippingAddress() {
    return shippingAddress != null && !shippingAddress.isEmpty() ? shippingAddress.get(0) : null;
  }

  public boolean addProduct(Product product) {
    // Sample stub
    return true;
  }

  public boolean addProductReview(ProductReview review, Product product) {
    product.getReviews().add(review);
    return true;
  }

  public boolean deleteProduct(Product product) {
    // Sample stub
    return true;
  }

  public boolean deleteProductReview(ProductReview review, Product product) {
    return product.getReviews().remove(review);
  }

  public boolean resetPassword() {
    // Dummy implementation
    return true;
  }

  public void setStatus(AccountStatus status) {
    this.status = status;
  }

  public String getUserName() {
    return this.userName;
  }

  public String getEmail() {
    return this.email;
  }

  public String getPhone() {
    return this.phone;
  }

  public AccountStatus getStatus() {
    return this.status;
  }
}
