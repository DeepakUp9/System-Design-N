package questions.QList.ElevatorSystem.first;

// ElevatorSystemDemo.java
public class ElevatorSystemDemo {
    public static void main(String[] args) {
        System.out.println("=== Elevator System Simulation ===\n");
        
        // Create building with 10 floors and 3 elevators
        Building building = new Building(10, 3);
        
        // Get some floors for easy access
        Floor floor3 = building.getFloor(3);
        Floor floor5 = building.getFloor(5);
        Floor floor8 = building.getFloor(8);
        
        // Simulation scenario
        System.out.println("1. Person on floor 3 presses UP button");
        floor3.pressUpButton();
        
        try { Thread.sleep(3000); } catch (InterruptedException e) {}
        
        System.out.println("\n2. Person on floor 5 presses DOWN button");
        floor5.pressDownButton();
        
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        System.out.println("\n3. Person gets in elevator and presses floor 7");
        // Simulate someone pressing internal button
        building.getElevators().get(0).getInternalPanel().pressButton(7);
        
        try { Thread.sleep(10000); } catch (InterruptedException e) {}
        
        System.out.println("\n4. Person on floor 8 presses UP button");
        floor8.pressUpButton();
        
        // Let the simulation run for a while
        try { Thread.sleep(15000); } catch (InterruptedException e) {}
        
        System.out.println("\n=== Simulation Complete ===");
        
        // Cleanup
        ElevatorController.getInstance().stopSystem();
    }
}
