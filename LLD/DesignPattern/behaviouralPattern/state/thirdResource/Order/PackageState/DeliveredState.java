package LLD.DesignPattern.behaviouralPattern.state.thirdResource.Order.PackageState;
import LLD.DesignPattern.behaviouralPattern.state.thirdResource.Order.Package;
public class DeliveredState implements PackageState{

    @Override
    public void prev(Package state) {
        state.setContext(new OrderState());
    }

    @Override
    public void next(Package state) {
        state.setContext(new ReceivedState());
    }

    @Override
    public void printStatus() {
       System.out.println("currently in Delivered State");
    }
    
}
