public class FlightSeat extends Seat {
  private double fare;
  private SeatStatus status;
  private String reservationNumber;

  public FlightSeat(String seatNumber, SeatType type, SeatClass _class, double fare) {
    super(seatNumber, type, _class);
    this.fare = fare;
    this.status = SeatStatus.AVAILABLE;
    this.reservationNumber = null;
  }

  public double getFare() { return fare; }
  public void setFare(double fare) { this.fare = fare; }

  public SeatStatus getStatus() { return status; }
  public void setStatus(SeatStatus status) { this.status = status; }

  public String getReservationNumber() { return reservationNumber; }
  public void setReservationNumber(String reservationNumber) { this.reservationNumber = reservationNumber; }
}
