public class Branch {
    private String name;
    private Address location;
    private Menu menu;
    private TableConfiguration tableConfig;

    public Branch(String name, Address location, Menu menu, TableConfiguration tableConfig) {
        this.name = name;
        this.location = location;
        this.menu = menu;
        this.tableConfig = tableConfig;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getLocation() {
        return location;
    }

    public void setLocation(Address location) {
        this.location = location;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public TableConfiguration getTableConfig() {
        return tableConfig;
    }

    public void setTableConfig(TableConfiguration tableConfig) {
        this.tableConfig = tableConfig;
    }
}
