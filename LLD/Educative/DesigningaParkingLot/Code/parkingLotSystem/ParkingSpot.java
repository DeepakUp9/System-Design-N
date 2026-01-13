public abstract class ParkingSpot {
    protected int id;
    protected boolean isFree = true;
    protected Vehicle vehicle;

    public ParkingSpot(int id) { this.id = id; }
    public boolean isFree() { return isFree; }
    public abstract boolean assignVehicle(Vehicle v);
    public boolean removeVehicle() {
        if (!isFree && vehicle != null) {
            System.out.println("Slot " + id + " freed (was " + vehicle.getLicenseNo() + ")");
            vehicle = null; isFree = true;
            return true;
        }
        return false;
    }
    public int getId() { return id; }
}
