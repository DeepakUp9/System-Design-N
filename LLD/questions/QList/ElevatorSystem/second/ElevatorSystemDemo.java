package questions.QList.ElevatorSystem.second;

import java.util.List;
import java.util.Set;

import questions.QList.ElevatorSystem.second.Elevator.ElevatorCar;
import questions.QList.ElevatorSystem.second.Elevator.ElevatorController;
import questions.QList.ElevatorSystem.second.ElevatorStartegy.ExternalStartegy.ClosestAvailableStrategy;
import questions.QList.ElevatorSystem.second.ElevatorStartegy.ExternalStartegy.EvenOddStrategy;
import questions.QList.ElevatorSystem.second.ElevatorStartegy.ExternalStartegy.SmartAssignmentStrategy;
import questions.QList.ElevatorSystem.second.ElevatorStartegy.InternalStrategy.FifoStrategy;
import questions.QList.ElevatorSystem.second.ElevatorStartegy.InternalStrategy.ShortestSeekTimeStrategy;
import questions.QList.ElevatorSystem.second.enums.ElevatorState;
import questions.QList.ElevatorSystem.second.request.ElevatorRequestProcessor;

/*
 * Global Request Flow:
Button Press → Request → Global RequestProcessor → Routes to appropriate handler

External Request Flow:
Global RequestProcessor → ElevatorController → ExternalRequestHandler → Strategy → Specific ElevatorProcessor

Internal Request Flow:  
Global RequestProcessor → Directly to Specific ElevatorProcessor → Internal Strategy → Physical ElevatorCar

 ***/

public class ElevatorSystemDemo {
    public static void main(String[] args) {
        System.out.println("=== 🏢 Elevator System with Strategy Pattern ===\n");

        // Create building with 10 floors and 3 elevators
        Building building = new Building(10, 3);
        
        // Get controller instance
        ElevatorController controller = ElevatorController.getInstance();
        
        // Get elevator references for easier testing
        ElevatorCar elevator1 = building.getElevators().get(0);
        ElevatorCar elevator2 = building.getElevators().get(1);
        ElevatorCar elevator3 = building.getElevators().get(2);

        System.out.println("📍 Initial elevator positions:");
        building.getElevators().forEach(System.out::println);
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("🚀 TEST SCENARIO 1: SMART ASSIGNMENT STRATEGY (DEFAULT)");
        System.out.println("=".repeat(80));

        // Test 1: Smart Assignment - Immediate Service Scenario
        System.out.println("\n1. 🟢 Person on floor 3 presses UP button");
        building.getFloor(3).pressUpButton();
        try { Thread.sleep(3000); } catch (InterruptedException e) {}

        // Test 2: Smart Assignment - En-route Service
        System.out.println("\n2. 🟢 Person on floor 7 presses DOWN button");
        building.getFloor(7).pressDownButton();
        try { Thread.sleep(3000); } catch (InterruptedException e) {}

        // Test 3: Internal Request with LOOK Algorithm
        System.out.println("\n3. 🟢 Person gets in elevator and presses multiple floors (2, 5, 8)");
        elevator1.getInternalPanel().pressButton(2);
        elevator1.getInternalPanel().pressButton(5);
        elevator1.getInternalPanel().pressButton(8);
        try { Thread.sleep(5000); } catch (InterruptedException e) {}

        System.out.println("\n" + "=".repeat(80));
        System.out.println("🔄 TEST SCENARIO 2: EVEN-ODD STRATEGY");
        System.out.println("=".repeat(80));

        // Test 4: Switch to Even-Odd Strategy
        System.out.println("\n4. 🔄 Switching to Even-Odd Strategy");
        controller.setExternalStrategy(new EvenOddStrategy());

        // Test 5: Even floor request (should prefer even elevator)
        System.out.println("\n5. 🟢 Person on floor 4 (EVEN) presses UP button");
        building.getFloor(4).pressUpButton();
        try { Thread.sleep(3000); } catch (InterruptedException e) {}

        // Test 6: Odd floor request (should prefer odd elevator)
        System.out.println("\n6. 🟢 Person on floor 5 (ODD) presses DOWN button");
        building.getFloor(5).pressDownButton();
        try { Thread.sleep(3000); } catch (InterruptedException e) {}

        System.out.println("\n" + "=".repeat(80));
        System.out.println("📍 TEST SCENARIO 3: CLOSEST AVAILABLE STRATEGY");
        System.out.println("=".repeat(80));

        // Test 7: Switch to Closest Available Strategy
        System.out.println("\n7. 🔄 Switching to Closest Available Strategy");
        controller.setExternalStrategy(new ClosestAvailableStrategy());

        // Test 8: Closest elevator should be selected
        System.out.println("\n8. 🟢 Person on floor 6 presses DOWN button");
        building.getFloor(6).pressDownButton();
        try { Thread.sleep(3000); } catch (InterruptedException e) {}

        System.out.println("\n" + "=".repeat(80));
        System.out.println("🎯 TEST SCENARIO 4: YOUR SPECIFIC USE CASE");
        System.out.println("=".repeat(80));

        // Reset elevators to specific positions for the test case
        System.out.println("\n9. 🔄 Resetting elevators for specific test case:");
        elevator1.moveToFloor(1);
        elevator2.moveToFloor(1);
        elevator3.moveToFloor(1);
        elevator1.setIdle();
        elevator2.setIdle();
        elevator3.setIdle();
        
        System.out.println("📍 Reset positions:");
        building.getElevators().forEach(System.out::println);

        // Switch back to Smart Assignment for the main test
        System.out.println("\n10. 🔄 Switching back to Smart Assignment Strategy");
        controller.setExternalStrategy(new SmartAssignmentStrategy());

        // Your specific scenario
        System.out.println("\n11. 👤 Person A at 7th floor requests DOWN");
        building.getFloor(7).pressDownButton();
        try { Thread.sleep(4000); } catch (InterruptedException e) {}

        // Person A enters elevator and selects floor 4
        ElevatorCar elevatorForPersonA = findMovingElevator(building.getElevators());
        if (elevatorForPersonA != null) {
            System.out.println("\n12. 👤 Person A enters elevator " + elevatorForPersonA.getId() + " and presses floor 4");
            elevatorForPersonA.getInternalPanel().pressButton(4);
            try { Thread.sleep(6000); } catch (InterruptedException e) {}
        }

        // Person B at floor 4 requests DOWN - This should demonstrate immediate service
        System.out.println("\n13. 👤 Person B at 4th floor requests DOWN (Testing Immediate Service)");
        building.getFloor(4).pressDownButton();
        try { Thread.sleep(4000); } catch (InterruptedException e) {}

        System.out.println("\n" + "=".repeat(80));
        System.out.println("🔄 TEST SCENARIO 5: INTERNAL STRATEGY SWITCHING");
        System.out.println("=".repeat(80));

        // Test different internal strategies
        System.out.println("\n14. 🔄 Testing Internal Strategy Switching");

        // Test LOOK Algorithm
        System.out.println("\n15. 🧪 Testing LOOK Algorithm (Default)");
        elevator2.getInternalPanel().pressButton(3);
        elevator2.getInternalPanel().pressButton(7);
        elevator2.getInternalPanel().pressButton(1);
        try { Thread.sleep(5000); } catch (InterruptedException e) {}

        // Test SSTF Algorithm
        System.out.println("\n16. 🔄 Switching to SSTF Internal Strategy");
        ElevatorRequestProcessor processor2 = controller.getProcessorForElevator(2);
        processor2.setInternalStrategy(new ShortestSeekTimeStrategy());
        
        System.out.println("\n17. 🧪 Testing SSTF Algorithm");
        elevator2.getInternalPanel().pressButton(4);
        elevator2.getInternalPanel().pressButton(9);
        elevator2.getInternalPanel().pressButton(2);
        try { Thread.sleep(5000); } catch (InterruptedException e) {}

        // Test FIFO Algorithm
        System.out.println("\n18. 🔄 Switching to FIFO Internal Strategy");
        processor2.setInternalStrategy(new FifoStrategy());
        
        System.out.println("\n19. 🧪 Testing FIFO Algorithm");
        elevator2.getInternalPanel().pressButton(6);
        elevator2.getInternalPanel().pressButton(3);
        elevator2.getInternalPanel().pressButton(8);
        try { Thread.sleep(5000); } catch (InterruptedException e) {}

        System.out.println("\n" + "=".repeat(80));
        System.out.println("🚨 TEST SCENARIO 6: CONCURRENT REQUESTS");
        System.out.println("=".repeat(80));

        // Test concurrent requests
        System.out.println("\n20. 🧪 Testing Concurrent Requests");
        
        // Multiple people pressing buttons around the same time
        System.out.println("    • Person at floor 2 presses UP");
        building.getFloor(2).pressUpButton();
        
        System.out.println("    • Person at floor 9 presses DOWN");
        building.getFloor(9).pressDownButton();
        
        System.out.println("    • Person at floor 5 presses UP");
        building.getFloor(5).pressUpButton();
        
        try { Thread.sleep(8000); } catch (InterruptedException e) {}

        System.out.println("\n" + "=".repeat(80));
        System.out.println("📊 PERFORMANCE METRICS AND FINAL STATUS");
        System.out.println("=".repeat(80));

        // Wait for all operations to complete
        try { Thread.sleep(5000); } catch (InterruptedException e) {}

        System.out.println("\n=== 📊 Final Elevator Status ===");
        building.getElevators().forEach(System.out::println);

        // Show pending requests
        System.out.println("\n=== 📋 Pending Requests Status ===");
        for (ElevatorCar elevator : building.getElevators()) {
            ElevatorRequestProcessor processor = controller.getProcessorForElevator(elevator.getId());
            Set<Integer> pending = processor.getPendingFloors();
            if (!pending.isEmpty()) {
                System.out.println("Elevator " + elevator.getId() + " pending floors: " + pending);
            }
        }

        System.out.println("\n=== 🎯 Demo Complete ===");
        System.out.println("Strategies Tested:");
        System.out.println("  ✅ Smart Assignment Strategy (Immediate → En-route → Proximity → Even-Odd)");
        System.out.println("  ✅ Even-Odd Strategy");
        System.out.println("  ✅ Closest Available Strategy");
        System.out.println("  ✅ LOOK/SCAN Internal Strategy");
        System.out.println("  ✅ Shortest Seek Time First Internal Strategy");
        System.out.println("  ✅ FIFO Internal Strategy");
        System.out.println("  ✅ Concurrent Request Handling");
        System.out.println("  ✅ Your Specific Use Case (Immediate Service)");

        // Cleanup
        controller.stopSystem();
    }

    /**
     * Helper method to find which elevator is currently moving
     */
    private static ElevatorCar findMovingElevator(List<ElevatorCar> elevators) {
        return elevators.stream()
                .filter(e -> e.getState() != ElevatorState.IDLE)
                .findFirst()
                .orElse(null);
    }
}
