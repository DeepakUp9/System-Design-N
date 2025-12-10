import java.util.List;

public class Guest extends Customer {

  public boolean registerAccount(String username, String password, String email) {
    // Dummy logic to simulate registration
    System.out.println("Account registered for user: " + username);
    return true;
  }

  public List<Product> searchProduuct(String name){
    return null;
  }
}
