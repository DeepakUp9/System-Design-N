public class ProductReview {
  private int rating;
  private String review;
  private byte[] image;
  private AuthenticatedUser user;

  public int getRating() {
    return rating;
  }

  public String getReview() {
    return review;
  }

  public AuthenticatedUser getUser() {
    return user;
  }
}
