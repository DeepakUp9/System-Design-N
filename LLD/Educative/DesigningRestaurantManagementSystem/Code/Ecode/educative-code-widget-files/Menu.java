import java.util.List;

public class Menu {
    private int menuID;
    private String title;
    private String description;
    private double price;
    private List<MenuSection> menuSections;

    public Menu(int menuID, String title, String description, double price, List<MenuSection> menuSections) {
        this.menuID = menuID;
        this.title = title;
        this.description = description;
        this.price = price;
        this.menuSections = menuSections;
    }

    public Menu() {}

    public int getMenuID() {
        return menuID;
    }

    public void setMenuID(int menuID) {
        this.menuID = menuID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<MenuSection> getMenuSections() {
        return menuSections;
    }

    public void setMenuSections(List<MenuSection> menuSections) {
        this.menuSections = menuSections;
    }

    public boolean addMenuSection(MenuSection menuSection) {
        return this.menuSections.add(menuSection);
    }
}
