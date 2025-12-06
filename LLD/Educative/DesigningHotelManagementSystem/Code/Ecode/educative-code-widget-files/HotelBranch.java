import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class HotelBranch {
    private String name;
    private Address location;

    public HotelBranch(String name, Address location) {
        this.name = name;
        this.location = location;
    }

    public List<Room> getRooms() {
        return new ArrayList<>(); // Return actual rooms list
    }

    // Getters and Setters
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
}