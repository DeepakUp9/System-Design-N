import java.util.*;

public class Hall {
    public int hallId;
    public List<ShowTime> shows = new ArrayList<>();

    public Hall(int hallId, List<ShowTime> shows) {
        this.hallId = hallId;
        this.shows = shows;
    }

    public List<ShowTime> findCurrentShows() {
        return shows;
    }
}
