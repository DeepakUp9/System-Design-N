public class Admin {
  private Account account;

  public Admin(Account account) {
    this.account = account;
  }

  public boolean blockUser(Account accountToBlock) {
    if (accountToBlock != null) {
      accountToBlock.setStatus(AccountStatus.BLOCKED);
      System.out.println("User " + accountToBlock.getUserName() + " has been blocked.");
      return true;
    }
    return false;
  }

  public boolean addNewProductCategory(ProductCategory category) {
    if (category != null) {
      System.out.println("Product category '" + category.getName() + "' added.");
      return true;
    }
    return false;
  }

  public boolean modifyProductCategory(ProductCategory category) {
    if (category != null) {
      System.out.println("Product category '" + category.getName() + "' modified.");
      return true;
    }
    return false;
  }

  public boolean deleteProductCategory(ProductCategory category) {
    if (category != null) {
      System.out.println("Product category '" + category.getName() + "' deleted.");
      return true;
    }
    return false;
  }
}
