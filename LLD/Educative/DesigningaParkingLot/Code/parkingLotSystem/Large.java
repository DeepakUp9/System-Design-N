public class Large extends ParkingSpot {
    public Large(int id) { super(id); }
    public boolean assignVehicle(Vehicle v) {
        if (isFree) {
            System.out.println("Allocated Large slot " + id + " to " + v.getLicenseNo());
            this.vehicle = v; isFree = false; return true;
        }
        return false;
    }
}
