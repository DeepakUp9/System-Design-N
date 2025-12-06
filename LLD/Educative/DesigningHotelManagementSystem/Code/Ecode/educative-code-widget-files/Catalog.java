import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Catalog implements Search {
    private List<Room> rooms;

    public Catalog() {
        this.rooms = new ArrayList<>();
    }

    @Override
    public List<Room> search(RoomStyle style, Date date, int duration) {
        // Filter rooms based on style, date, and duration
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.isRoomAvailable() && room.getStyle() == style) {
                availableRooms.add(room);
            }
        }
        return availableRooms; // Return filtered list of rooms
    }

    // Getters and Setters
    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}