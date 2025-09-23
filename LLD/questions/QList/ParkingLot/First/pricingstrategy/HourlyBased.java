package questions.QList.ParkingLot.First.pricingstrategy;

import questions.QList.ParkingLot.First.Ticket;

public class HourlyBased extends PricingStrategy{

    @Override
    public double price(Ticket ticket){
        //calculate hrs based exntry time and exit time
          return ticket.getParkingspot().price;
    }


    
    
}
