import java.util.List;

public class Aircraft {
  private String name;
  private String code;
  private String model;
  private int seatCapacity;
  private List<Seat> seats;

  public Aircraft(String name, String code, String model, int seatCapacity, List<Seat> seats) {
    this.name = name;
    this.code = code;
    this.model = model;
    this.seatCapacity = seatCapacity;
    this.seats = seats;
  }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getCode() { return code; }
  public void setCode(String code) { this.code = code; }

  public String getModel() { return model; }
  public void setModel(String model) { this.model = model; }

  public int getSeatCapacity() { return seatCapacity; }
  public void setSeatCapacity(int seatCapacity) { this.seatCapacity = seatCapacity; }

  public List<Seat> getSeats() { return seats; }
  public void setSeats(List<Seat> seats) { this.seats = seats; }
}
