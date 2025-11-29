import java.util.*;

public class VehicleReservation {
    private int reservationId;
    private String customerId;
    private String vehicleId;
    private Date creationDate;
    private ReservationStatus status;
    private Date dueDate;
    private Date returnDate;
    private String pickupLocation;
    private String returnLocation;
    private List<Equipment> equipments = new ArrayList<>();
    private List<Service> services = new ArrayList<>();

    public void setReservationId(int id) { reservationId = id; }
    public int getReservationId() { return reservationId; }
    public void setCustomerId(String id) { customerId = id; }
    public String getCustomerId() { return customerId; }
    public void setVehicleId(String id) { vehicleId = id; }
    public String getVehicleId() { return vehicleId; }
    public void setCreationDate(Date d) { creationDate = d; }
    public Date getCreationDate() { return creationDate; }
    public void setStatus(ReservationStatus s) { status = s; }
    public ReservationStatus getStatus() { return status; }
    public void setDueDate(Date d) { dueDate = d; }
    public Date getDueDate() { return dueDate; }
    public void setReturnDate(Date d) { returnDate = d; }
    public Date getReturnDate() { return returnDate; }
    public void setPickupLocation(String l) { pickupLocation = l; }
    public String getPickupLocation() { return pickupLocation; }
    public void setReturnLocation(String l) { returnLocation = l; }
    public String getReturnLocation() { return returnLocation; }

    public VehicleReservation getReservationDetails() { return this; }

    public boolean addEquipment(Equipment e) {
        equipments.add(e);
        return true;
    }
    public boolean addService(Service s) {
        services.add(s);
        return true;
    }
}
