package questions.QList.ElevatorSystem.second;

import questions.QList.ElevatorSystem.second.Elevator.ElevatorCar;
import questions.QList.ElevatorSystem.second.Elevator.ElevatorController;
import questions.QList.ElevatorSystem.second.ElevatorStartegy.ExternalStartegy.ClosestAvailableStrategy;
import questions.QList.ElevatorSystem.second.ElevatorStartegy.ExternalStartegy.EvenOddStrategy;

public class ElevatorSystemDemo {
    public static void main(String[] args) {
        System.out.println("=== 🏢 Elevator System with Strategy Pattern ===\n");

        // Create building with 10 floors and 3 elevators
        Building building = new Building(10, 3);

        // Get elevator references for easier testing
        ElevatorCar elevator1 = building.getElevators().get(0);
        ElevatorCar elevator2 = building.getElevators().get(1);
        ElevatorCar elevator3 = building.getElevators().get(2);

        // Set initial positions
        elevator1.moveTo(5);
        elevator2.moveTo(8);
        elevator3.moveTo(3);

        System.out.println("📍 Initial elevator positions:");
        building.getElevators().forEach(System.out::println);

        // Test scenarios
        System.out.println("\n1. 🟢 Person on floor 3 presses UP button");
        building.getFloor(3).pressUpButton();

        try { Thread.sleep(5000); } catch (InterruptedException e) {}

        System.out.println("\n2. 🟢 Person on floor 7 presses DOWN button");
        building.getFloor(7).pressDownButton();

        try { Thread.sleep(3000); } catch (InterruptedException e) {}

        System.out.println("\n3. 🟢 Person gets in elevator 1 and presses floor 2");
        elevator1.getInternalPanel().pressButton(2);

        try { Thread.sleep(8000); } catch (InterruptedException e) {}

        // Test strategy switching
        System.out.println("\n4. 🔄 Switching to Even-Odd Strategy");
        ElevatorController.getInstance().setExternalStrategy(new EvenOddStrategy());

        System.out.println("\n5. 🟢 Person on floor 4 presses UP button (Even-Odd strategy)");
        building.getFloor(4).pressUpButton();

        try { Thread.sleep(5000); } catch (InterruptedException e) {}

        System.out.println("\n6. 🔄 Switching to Closest Available Strategy");
        //ElevatorController.getInstance().setExternalStrategy(new ClosestAvailableStrategy());
        ElevatorController.getInstance().setExternalStrategy(new ClosestAvailableStrategy());

        System.out.println("\n7. 🟢 Person on floor 6 presses DOWN button (Closest strategy)");
        building.getFloor(6).pressDownButton();

        try { Thread.sleep(5000); } catch (InterruptedException e) {}

        System.out.println("\n=== 📊 Final Status ===");
        building.getElevators().forEach(System.out::println);

        System.out.println("\n=== 🎯 Demo Complete ===");

        // Cleanup
        ElevatorController.getInstance().stopSystem();
    }

}
