import java.util.List;

public class Meal {
    private int mealID;
    private Table table;
    private int seatNumber;
    private List<MenuItem> menuItems;

    public Meal(int mealID, Table table, int seatNumber, List<MenuItem> menuItems) {
        this.mealID = mealID;
        this.table = table;
        this.seatNumber = seatNumber;
        this.menuItems = menuItems;
    }

    public int getMealID() {
        return mealID;
    }

    public void setMealID(int mealID) {
        this.mealID = mealID;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public boolean addMealItem(MealItem mealItem) {
        return this.menuItems.add(mealItem.getMenuItem());
    }
}