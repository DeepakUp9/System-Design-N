import java.util.*;

public class Admin extends Person {
    public boolean addShow(ShowTime show) {
        System.out.println("Show added by admin.");
        return true;
    }
    public boolean updateShow(ShowTime show) {
        System.out.println("Show updated by admin.");
        return true;
    }
    public boolean deleteShow(ShowTime show) {
        System.out.println("Show deleted by admin.");
        return true;
    }
    public boolean addMovie(Movie movie) {
        System.out.println("Movie added by admin.");
        return true;
    }
    public boolean deleteMovie(Movie movie) {
        System.out.println("Movie deleted by admin.");
        return true;
    }
}
