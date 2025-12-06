import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Hotel {
    private String name;
    private List<HotelBranch> locations;

    public Hotel(String name) {
        this.name = name;
        this.locations = new ArrayList<>();
    }

    public boolean addLocation(HotelBranch location) {
        return locations.add(location);
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<HotelBranch> getLocations() {
        return locations;
    }

    public void setLocations(List<HotelBranch> locations) {
        this.locations = locations;
    }
}