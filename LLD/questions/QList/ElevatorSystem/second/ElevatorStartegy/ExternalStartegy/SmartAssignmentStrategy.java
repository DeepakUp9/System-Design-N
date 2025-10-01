package questions.QList.ElevatorSystem.second.ElevatorStartegy.ExternalStartegy;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import questions.QList.ElevatorSystem.second.Elevator.ElevatorCar;
import questions.QList.ElevatorSystem.second.enums.Direction;
import questions.QList.ElevatorSystem.second.enums.ElevatorState;
import questions.QList.ElevatorSystem.second.request.ExternalRequest;

// Strategy 1: Smart Assignment (Immediate -> EnRoute -> Proximity -> EvenOdd)
/**
 * Smart Assignment Strategy for External Requests
 * Priority: Immediate → EnRoute → Proximity → Load Balancing
 */
public class SmartAssignmentStrategy implements ExternalRequestStrategy {

    @Override
    public String getStrategyName() {
        return "Smart Assignment Strategy";
    }

    @Override
    public ElevatorCar assignElevator(ExternalRequest request, List<ElevatorCar> elevators) {
        int targetFloor = request.getFloor();
        Direction requestDirection = request.getDirection();

        System.out.println("🎯 " + getStrategyName() + " processing: " + request);

        // STEP 1: Immediate Service - ElevatorCar already at floor
        ElevatorCar immediate = findImmediateService(elevators, targetFloor);
        if (immediate != null) {
            System.out.println("✅ IMMEDIATE: Elevator " + immediate.getId() + " at floor " + targetFloor);
            return immediate;
        }

        // STEP 2: En-Route Service - ElevatorCar moving toward floor in correct direction
        ElevatorCar enRoute = findEnRouteService(elevators, targetFloor, requestDirection);
        if (enRoute != null) {
            System.out.println("🚀 EN-ROUTE: Elevator " + enRoute.getId() + " heading toward floor " + targetFloor);
            return enRoute;
        }

        // STEP 3: Proximity + Load Balancing
        ElevatorCar proximity = findProximityService(elevators, targetFloor);
        if (proximity != null) {
            System.out.println("📍 PROXIMITY: Elevator " + proximity.getId() + " assigned");
        }

        return proximity;
    }

    private ElevatorCar findImmediateService(List<ElevatorCar> elevators, int targetFloor) {
        return elevators.stream()
                .filter(e -> e.getCurrentFloor() == targetFloor && e.getState() == ElevatorState.IDLE)
                .findFirst()
                .orElse(null);
    }

    private ElevatorCar findEnRouteService(List<ElevatorCar> elevators, int targetFloor, Direction requestDirection) {
        return elevators.stream()
                .filter(e -> isEnRoute(e, targetFloor, requestDirection))
                .min(Comparator.comparingInt(e -> Math.abs(e.getCurrentFloor() - targetFloor)))
                .orElse(null);
    }

    private boolean isEnRoute(ElevatorCar elevator, int targetFloor, Direction requestDirection) {
        int currentFloor = elevator.getCurrentFloor();
        Direction elevatorDirection = elevator.getDirection();

        if (elevatorDirection == Direction.UP && requestDirection == Direction.UP) {
            return currentFloor < targetFloor;
        } else if (elevatorDirection == Direction.DOWN && requestDirection == Direction.DOWN) {
            return currentFloor > targetFloor;
        }
        return false;
    }
    
    private ElevatorCar findProximityService(List<ElevatorCar> ElevatorCars, int targetFloor) {
        List<ElevatorCar> candidates = ElevatorCars.stream()
                .filter(e -> e.getState() != ElevatorState.MAINTENANCE)
                .collect(Collectors.toList());

        if (candidates.isEmpty())
            return null;

        // Calculate scores for all ElevatorCars
        Map<ElevatorCar, Double> scores = new HashMap<>();
        double bestScore = Double.MAX_VALUE;

        for (ElevatorCar ElevatorCar : candidates) {
            double score = calculateProximityScore(ElevatorCar, targetFloor);
            scores.put(ElevatorCar, score);
            bestScore = Math.min(bestScore, score);
        }

        // Find ElevatorCars with similar scores (within threshold)
        final double threshold = 0.5;
        final double finalBestScore = bestScore;
        List<ElevatorCar> similarScoreCandidates = candidates.stream()
                .filter(e -> Math.abs(scores.get(e) - finalBestScore) <= threshold)
                .collect(Collectors.toList());

        // Apply even-odd tiebreaker for similar scores
        if (similarScoreCandidates.size() > 1) {
            return applyEvenOddTieBreaker(similarScoreCandidates, targetFloor);
        }

        return similarScoreCandidates.isEmpty() ? candidates.get(0) : similarScoreCandidates.get(0);
    }

    private double calculateProximityScore(ElevatorCar ElevatorCar, int targetFloor) {
        int distance = Math.abs(ElevatorCar.getCurrentFloor() - targetFloor);
        double loadFactor = (double) ElevatorCar.getCurrentLoad() / ElevatorCar.getMaxLoad();
        double directionPenalty = ElevatorCar.getDirection() != Direction.IDLE ? 1.0 : 0.0;
        //double pendingRequestsPenalty = ElevatorCar.hasPendingFloors() ? 0.5 : 0.0;

        return distance + loadFactor + directionPenalty ;//+ pendingRequestsPenalty;
    }

    private ElevatorCar applyEvenOddTieBreaker(List<ElevatorCar> candidates, int targetFloor) {
        boolean targetIsEven = (targetFloor % 2 == 0);

        for (ElevatorCar ElevatorCar : candidates) {
            boolean ElevatorCarIdIsEven = (ElevatorCar.getId() % 2 == 0);
            if (targetIsEven == ElevatorCarIdIsEven) {
                System.out.println("⚖️ Even-Odd tiebreaker: Floor " + targetFloor +
                        (targetIsEven ? " (even)" : " (odd)") +
                        " → ElevatorCar " + ElevatorCar.getId());
                return ElevatorCar;
            }
        }

        return candidates.get(0); // Fallback
    }
}
