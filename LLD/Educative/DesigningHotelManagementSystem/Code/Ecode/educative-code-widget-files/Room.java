import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Room {
    private String roomNumber;
    private RoomStyle style;
    private RoomStatus status;
    private double bookingPrice;
    private boolean isSmoking;
    private List<RoomKey> keys;
    private List<RoomHousekeeping> housekeepingLog;

    public Room(String roomNumber, RoomStyle style, RoomStatus status, double bookingPrice, boolean isSmoking) {
        this.roomNumber = roomNumber;
        this.style = style;
        this.status = status;
        this.bookingPrice = bookingPrice;
        this.isSmoking = isSmoking;
        this.keys = new ArrayList<>();
        this.housekeepingLog = new ArrayList<>();
    }

    public boolean isRoomAvailable() {
        return status == RoomStatus.AVAILABLE; // Check room availability
    }

    public boolean checkin() {
        this.status = RoomStatus.OCCUPIED;
        return true;
    }

    public boolean checkout() {
        this.status = RoomStatus.AVAILABLE;
        return true;
    }

    // Getters and Setters
    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomStyle getStyle() {
        return style;
    }

    public void setStyle(RoomStyle style) {
        this.style = style;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public double getBookingPrice() {
        return bookingPrice;
    }

    public void setBookingPrice(double bookingPrice) {
        this.bookingPrice = bookingPrice;
    }

    public boolean isSmoking() {
        return isSmoking;
    }

    public void setSmoking(boolean smoking) {
        isSmoking = smoking;
    }

    public List<RoomKey> getKeys() {
        return keys;
    }

    public void setKeys(List<RoomKey> keys) {
        this.keys = keys;
    }

    public List<RoomHousekeeping> getHousekeepingLog() {
        return housekeepingLog;
    }

    public void setHousekeepingLog(List<RoomHousekeeping> housekeepingLog) {
        this.housekeepingLog = housekeepingLog;
    }
}