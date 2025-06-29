package LLD.DesignPattern.behaviouralPattern.state.thirdResource.Order;

import LLD.DesignPattern.behaviouralPattern.state.thirdResource.Order.PackageState.OrderState;
import LLD.DesignPattern.behaviouralPattern.state.thirdResource.Order.PackageState.PackageState;

public class Package {
    PackageState state;

    public Package(){
        state = new OrderState();
    }

    public void setContext(PackageState state){
        this.state = state;
    }   

    public void prevState(){
       state.prev(this);
    }

    public void nextState(){
        state.next(this);
    }

    public void printStatus(){
        state.printStatus();
    }

}
