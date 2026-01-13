import java.util.*;


public class Exit {
    private int id;
    public Exit(int id) { this.id = id; }
    public void validateTicket(ParkingTicket t) {
        Date now = new Date();
        t.setExitTime(now);
        double hrs = (now.getTime() - t.getEntryTime().getTime()) / 3600000.0;
        double fee = ParkingLot.getInstance().rate.calculate(hrs, t.getVehicle(), ParkingLot.getInstance().getSpot(t.getSlotNo()));
        t.setAmount(fee);
        System.out.printf("Ticket %d | Parked: %.2f hrs | Fee: $%.2f\n", t.getTicketNo(), hrs, fee);
        Payment p = (fee > 10) ? new CreditCard(fee) : new Cash(fee);
        p.initiateTransaction();
        ParkingLot.getInstance().freeSlot(t.getSlotNo());
        t.setStatus(TicketStatus.PAID);
    }
}
