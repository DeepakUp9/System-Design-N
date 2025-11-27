import java.util.*;

public class City {
    public String name;
    public String state;
    public int zipCode;
    public List<Cinema> cinemas = new ArrayList<>();

    public City(String name, String state, int zipCode, List<Cinema> cinemas) {
        this.name = name;
        this.state = state;
        this.zipCode = zipCode;
        this.cinemas = cinemas;
    }
}
