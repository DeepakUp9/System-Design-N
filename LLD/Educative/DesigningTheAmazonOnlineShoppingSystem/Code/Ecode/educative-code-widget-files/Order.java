import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
  private String orderNumber;
  private AuthenticatedUser cutomer;
  private Date orderDate;
  private OrderStatus status;
  private ShoppingCart shoppingCart;

  public Order(String orderNumber, OrderStatus status) {
    this.orderNumber = orderNumber;
    this.orderDate = new Date();
    this.status = status;
    
  }

  public boolean sendForShipment() {
    return true;
  }

  public PaymentStatus makePayment(Payment payment) {
    return payment.makePayment();
  }

  public boolean verify(CartItem item) {
    return item != null && item.getQuantity() > 0 && item.getProduct() != null;
  }
}
