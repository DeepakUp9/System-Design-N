import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class RoomHousekeeping {
    private String description;
    private Date startDatetime;
    private int duration;
    private Housekeeper housekeeper;

    public boolean addHousekeeping(Room room) {
        // Logic to assign housekeeping task to room
        return true;
    }

    // Getters and Setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(Date startDatetime) {
        this.startDatetime = startDatetime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Housekeeper getHousekeeper() {
        return housekeeper;
    }

    public void setHousekeeper(Housekeeper housekeeper) {
        this.housekeeper = housekeeper;
    }
}