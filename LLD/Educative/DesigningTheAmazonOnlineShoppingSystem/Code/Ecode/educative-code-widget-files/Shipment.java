import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Shipment {
  private String shipmentNumber;
  private Date shipmentDate;
  private Date estimatedArrival;
  private String shipmentMethod;
  private ShipmentStatus status;

  public Shipment(String shipmentNumber, String shipmentMethod, Date estimatedArrival, ShipmentStatus status) {
    this.shipmentNumber = shipmentNumber;
    this.shipmentMethod = shipmentMethod;
    this.shipmentDate = new Date();
    this.estimatedArrival = estimatedArrival;
    this.status = status;
  }
}