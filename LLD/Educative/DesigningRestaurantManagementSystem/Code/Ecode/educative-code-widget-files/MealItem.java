public class MealItem {
    private int mealItemID;
    private int quantity;
    private MenuItem menuItem;

    public MealItem(int mealItemID, int quantity, MenuItem menuItem) {
        this.mealItemID = mealItemID;
        this.quantity = quantity;
        this.menuItem = menuItem;
    }

    public int getMealItemID() {
        return mealItemID;
    }

    public void setMealItemID(int mealItemID) {
        this.mealItemID = mealItemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public boolean updateQuantity(int quantity) {
        this.quantity = quantity;
        return true;
    }
}
