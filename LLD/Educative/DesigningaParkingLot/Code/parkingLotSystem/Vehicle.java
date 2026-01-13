public abstract class Vehicle {
    private String licenseNo;
    private ParkingTicket ticket;
    public Vehicle(String lic) { this.licenseNo = lic; }
    public String getLicenseNo() { return licenseNo; }
    public void assignTicket(ParkingTicket t) { this.ticket = t; }
    public ParkingTicket getTicket() { return ticket; }
}
