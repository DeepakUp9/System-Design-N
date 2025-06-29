package LLD.DesignPattern.behaviouralPattern.state.thirdResource.Order.PackageState;
import LLD.DesignPattern.behaviouralPattern.state.thirdResource.Order.Package;
public class ReceivedState implements PackageState{

    @Override
    public void prev(Package state) {
        state.setContext(new DeliveredState());
    }

    @Override
    public void next(Package state) {
       System.out.println("Order already delivered.. can't move illegal command");
    }

    @Override
    public void printStatus() {
      System.out.println("Order Received!!!");
    }
    
}
