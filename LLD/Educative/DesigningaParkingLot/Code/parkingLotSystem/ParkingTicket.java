import java.util.*;


public class ParkingTicket {
    private static int ticketSeed = 1000;
    private int ticketNo;
    private int slotNo;
    private Vehicle vehicle;
    private Date entryTime, exitTime;
    private double amount;
    private TicketStatus status;
    private Payment payment;
    public ParkingTicket(int slotNo, Vehicle v) {
        this.ticketNo = ticketSeed++;
        this.slotNo = slotNo;
        this.vehicle = v;
        this.entryTime = new Date();
        this.status = TicketStatus.ISSUED;
        v.assignTicket(this);
        System.out.println("Ticket issued: " + ticketNo);
    }

    public int getTicketNo() { return ticketNo; }
    public int getSlotNo() { return slotNo; }
    public Vehicle getVehicle() { return vehicle; }
    public Date getEntryTime() { return entryTime; }
    public Date getExitTime() { return exitTime; }
    public void setExitTime(Date dt) { this.exitTime = dt; }
    public void setAmount(double amt) { this.amount = amt; }
    public double getAmount() { return amount; }
    public void setStatus(TicketStatus s) { this.status = s; }
    public TicketStatus getStatus() { return status; }
}
