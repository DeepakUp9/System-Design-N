import java.util.Date;

public class Manager extends Person {
    public Manager(String name, String email, String password, Address address, String phone) {
        super(name, email, password, address, phone);
    }

    public TableConfiguration addTableConfig() {
        // Logic to configure table
        return new TableConfiguration(1, new byte[0]);
    }

    public Menu updateMenu() {
        // Logic to update menu
        return new Menu();
    }
}
