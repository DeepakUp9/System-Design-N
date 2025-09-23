package questions.QList.ParkingLot.First.pricingstrategy;

import questions.QList.ParkingLot.First.Ticket;

public class CostComputation {
    PricingStrategy pricingStrategy;
    Ticket ticket;

    public CostComputation(Ticket ticket, PricingStrategy pricingStrategy){
        this.ticket = ticket;
        this.pricingStrategy = pricingStrategy;
    }

    public double price(){
      return pricingStrategy.price(ticket);
    }

}
