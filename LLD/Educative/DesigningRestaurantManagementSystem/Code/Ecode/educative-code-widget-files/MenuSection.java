import java.util.List;

public class MenuSection {
    private int menuSectionID;
    private String title;
    private String description;
    private List<MenuItem> menuItems;

    public MenuSection(int menuSectionID, String title, String description, List<MenuItem> menuItems) {
        this.menuSectionID = menuSectionID;
        this.title = title;
        this.description = description;
        this.menuItems = menuItems;
    }

    public int getMenuSectionID() {
        return menuSectionID;
    }

    public void setMenuSectionID(int menuSectionID) {
        this.menuSectionID = menuSectionID;
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

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public boolean addMenuItem(MenuItem menuItem) {
        return this.menuItems.add(menuItem);
    }
}
