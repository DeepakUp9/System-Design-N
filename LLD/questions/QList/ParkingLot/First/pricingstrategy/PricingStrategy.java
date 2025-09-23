package questions.QList.ParkingLot.First.pricingstrategy;

import questions.QList.ParkingLot.First.Ticket;

public class PricingStrategy {
    
    public double price(Ticket ticket){
        return ticket.getParkingspot().price;
    }

}
