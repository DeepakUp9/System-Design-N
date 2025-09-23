package questions.QList.ParkingLot.First.parkingspot;

import questions.QList.ParkingLot.First.Vehicle;

public class TwoWheelerParkingSpot extends ParkingSpot{

    public TwoWheelerParkingSpot(Long id, boolean isEmpty, double price, Vehicle vehicle) {
        super(id, isEmpty, price, vehicle);
    }

    public double price(){
        return price + 25;
    }

    

}
