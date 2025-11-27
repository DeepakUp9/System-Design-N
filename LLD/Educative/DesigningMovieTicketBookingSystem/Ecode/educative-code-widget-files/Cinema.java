import java.util.*;

public class Cinema {
    public int cinemaId;
    public List<Hall> halls = new ArrayList<>();
    public City city;

    public Cinema(int cinemaId, List<Hall> halls, City city) {
        this.cinemaId = cinemaId;
        this.halls = halls;
        this.city = city;
    }
}
