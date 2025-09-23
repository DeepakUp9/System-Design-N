package questions.QList.ParkingLot.First.pricingstrategy;

import questions.QList.ParkingLot.First.Ticket;

public class Minutebase extends PricingStrategy{

    @Override
    public double price(Ticket ticket){
        //calculate minute based exntry time and exit time
        return ticket.getParkingspot().price;
    }


}