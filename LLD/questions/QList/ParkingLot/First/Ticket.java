package questions.QList.ParkingLot.First;

import questions.QList.ParkingLot.First.parkingspot.ParkingSpot;

public class Ticket {
    long id;
    long EntryTime;
    Vehicle vehcile;
    ParkingSpot parkingspot;

    public Ticket(long id, long entryTime, Vehicle vehcile, ParkingSpot parkingspot) {
        this.id = id;
        EntryTime = entryTime;
        this.vehcile = vehcile;
        this.parkingspot = parkingspot;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEntryTime() {
        return EntryTime;
    }

    public void setEntryTime(long entryTime) {
        EntryTime = entryTime;
    }

    public Vehicle getVehcile() {
        return vehcile;
    }

    public void setVehcile(Vehicle vehcile) {
        this.vehcile = vehcile;
    }

    public ParkingSpot getParkingspot() {
        return parkingspot;
    }

    public void setParkingspot(ParkingSpot parkingspot) {
        this.parkingspot = parkingspot;
    }

    
}
