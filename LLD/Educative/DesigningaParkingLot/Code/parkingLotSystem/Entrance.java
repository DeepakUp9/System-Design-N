public class Entrance {
    private int id;
    public Entrance(int id) { this.id = id; }
    public ParkingTicket getTicket(Vehicle v) {
        return ParkingLot.getInstance().parkVehicle(v);
    }
}
