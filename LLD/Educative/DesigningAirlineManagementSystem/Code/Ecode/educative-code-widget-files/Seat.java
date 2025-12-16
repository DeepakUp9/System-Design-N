public class Seat {
  private String seatNumber;
  private SeatType type;
  private SeatClass _class;

  public Seat(String seatNumber, SeatType type, SeatClass _class) {
    this.seatNumber = seatNumber;
    this.type = type;
    this._class = _class;
  }

  public String getSeatNumber() { return seatNumber; }
  public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

  public SeatType getType() { return type; }
  public void setType(SeatType type) { this.type = type; }

  public SeatClass getSeatClass() { return _class; }
  public void setSeatClass(SeatClass _class) { this._class = _class; }
}
