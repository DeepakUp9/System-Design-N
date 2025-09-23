package questions.QList.ParkingLot.First.parkingspot;

import questions.QList.ParkingLot.First.Vehicle;

public class ParkingSpot {
    Long id;
    boolean isEmpty;
    public double price;
    Vehicle vehicle;

    
    public ParkingSpot(Long id, boolean isEmpty, double price, Vehicle vehicle) {
        this.id = id;
        this.isEmpty = isEmpty;
        this.price = price;
        this.vehicle = vehicle;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public boolean isEmpty() {
        return isEmpty;
    }
    public void setEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public Vehicle getVehicle() {
        return vehicle;
    }
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    //custome methods

    public Vehicle addVehicle(Vehicle v){
      return null;
    }

    public void removeVehicle(){

    }

    



    
}
