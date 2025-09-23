package questions.QList.ParkingLot.First;

public class Vehicle {
    String vcNo;
    VT vehicleType;

    
    public Vehicle(String vcNo, VT vehicleType) {
        this.vcNo = vcNo;
        this.vehicleType = vehicleType;
    }
    
    public String getVcNo() {
        return vcNo;
    }
    public void setVcNo(String vcNo) {
        this.vcNo = vcNo;
    }
    public VT getVehicleType() {
        return vehicleType;
    }
    public void setVehicleType(VT vehicleType) {
        this.vehicleType = vehicleType;
    }

    
}
