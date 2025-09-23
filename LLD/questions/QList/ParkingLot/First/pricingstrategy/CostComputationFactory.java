package questions.QList.ParkingLot.First.pricingstrategy;

import java.time.LocalTime;

import questions.QList.ParkingLot.First.Ticket;
import questions.QList.ParkingLot.First.VT;

public class CostComputationFactory {
    
    public CostComputation geCostComputation(Ticket ticket){
        if(ticket.getVehcile().getVehicleType().equals(VT.TWOWHEELER)){
            int currTime = LocalTime.now().getMinute();
            if(currTime - ticket.getEntryTime() > 60){
                return new TwoWheelerCostComputation(ticket, new HourlyBased());
            }else{
                 return new TwoWheelerCostComputation(ticket, new Minutebase());
            }
        }else if(ticket.getVehcile().getVehicleType().equals(VT.FOURWHEELER)){
            int currTime = LocalTime.now().getMinute();
            if(currTime - ticket.getEntryTime() > 60){
                return new FourWheelerCostComputation(ticket, new HourlyBased());
            }else{
                 return new FourWheelerCostComputation(ticket, new Minutebase());
            }
        }else{
            return null;
        }
    }
}
