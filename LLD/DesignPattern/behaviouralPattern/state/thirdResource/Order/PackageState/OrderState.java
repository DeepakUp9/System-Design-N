package LLD.DesignPattern.behaviouralPattern.state.thirdResource.Order.PackageState;
import LLD.DesignPattern.behaviouralPattern.state.thirdResource.Order.Package;
public class OrderState implements PackageState{

    @Override
    public void prev(Package state) {
        System.out.println("already on OrderState can't move to the prev state can't illigale command");
    }

    @Override
    public void next( Package state) {
        state.setContext(new DeliveredState());
    }

    @Override
    public void printStatus() {
        System.out.println("currently in OrderState state");
    }
    
}
