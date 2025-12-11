import java.util.Date;

public class Order {
    private int orderID;
    private OrderStatus status;
    private Date creationTime;
    private Meal[] meals;
    private Table table;
    private Waiter waiter;

    public Order(int orderID, OrderStatus status, Date creationTime, Meal[] meals,
                 Table table, Waiter waiter) {
        this.orderID = orderID;
        this.status = status;
        this.creationTime = creationTime;
        this.meals = meals;
        this.table = table;
        this.waiter = waiter;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Meal[] getMeals() {
        return meals;
    }

    public void setMeals(Meal[] meals) {
        this.meals = meals;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public void setWaiter(Waiter waiter) {
        this.waiter = waiter;
    }

    public boolean addMeal(Meal meal) {
        // Add logic to add meal to array
        return true;
    }

    public boolean removeMeal(Meal meal) {
        // Add logic to remove meal
        return true;
    }

    public boolean replaceMealItem(Meal oldItem, Meal newItem) {
        // Replace old meal with new one
        return true;
    }
}
