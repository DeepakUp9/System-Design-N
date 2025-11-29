import java.util.*;

public abstract class Vehicle {
    private String vehicleId;
    private String licensePlateNumber;
    private int passengerCapacity;
    private boolean hasSunroof;
    private VehicleStatus status;
    private String model;
    private int manufacturingYear;
    private int mileage;
    private List<VehicleLog> log = new ArrayList<>();

    public String getVehicleId() { return vehicleId; }
    public void setVehicleId(String id) { vehicleId = id; }
    public void setModel(String m) { model = m; }
    public String getModel() { return model; }
    public void setManufacturingYear(int y) { manufacturingYear = y; }
    public int getManufacturingYear() { return manufacturingYear; }
    public void setStatus(VehicleStatus s) { status = s; }
    public VehicleStatus getStatus() { return status; }

    public boolean reserveVehicle() {
        if (status == VehicleStatus.AVAILABLE) {
            status = VehicleStatus.RESERVED;
            return true;
        }
        return false;
    }

    public boolean returnVehicle() {
        if (status == VehicleStatus.RESERVED) {
            status = VehicleStatus.AVAILABLE;
            return true;
        }
        return false;
    }
}
