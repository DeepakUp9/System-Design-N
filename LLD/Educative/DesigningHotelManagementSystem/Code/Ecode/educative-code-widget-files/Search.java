import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public interface Search {
    List<Room> search(RoomStyle style, Date date, int duration);
}
