package LLD.DesignPattern.behaviouralPattern.state.thirdResource.Order.PackageState;
import LLD.DesignPattern.behaviouralPattern.state.thirdResource.Order.Package;
public interface PackageState {
   public void prev(Package state);
   public void next(Package state);
   public void printStatus();
} 