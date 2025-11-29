public class ParkingStall {
    private int stallId;
    private String locationIdentifier;

    public ParkingStall(int id, String loc) {
        stallId = id;
        locationIdentifier = loc;
    }

    public int getStallId() { return stallId; }
    public String getLocationIdentifier() { return locationIdentifier; }
}
