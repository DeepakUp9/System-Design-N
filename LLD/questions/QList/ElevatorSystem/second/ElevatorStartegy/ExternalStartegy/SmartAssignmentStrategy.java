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
public class SmartAssignmentStrategy implements ExternalRequestStrategy {

    @Override
    public String getStrategyName() {
        return "Smart Assignment Strategy";
    }

    @Override
    public ElevatorCar assignElevator(ExternalRequest request, List<ElevatorCar> ElevatorCars) {
        int targetFloor = request.getFloor();
        Direction requestDirection = request.getDirection();

        System.out.println("🎯 " + getStrategyName() + " processing: " + request);

        // STEP 1: Immediate Service - ElevatorCar already at floor
        ElevatorCar immediateService = findImmediateService(ElevatorCars, targetFloor);
        if (immediateService != null) {
            System.out.println("✅ Immediate Service: " + immediateService.getId() + " already at floor " + targetFloor);
            return immediateService;
        }

        // STEP 2: En-Route Service - ElevatorCar moving toward floor in correct direction
        ElevatorCar enRouteService = findEnRouteService(ElevatorCars, targetFloor, requestDirection);
        if (enRouteService != null) {
            System.out
                    .println("🚀 En-Route Service: " + enRouteService.getId() + " heading toward floor " + targetFloor);
            return enRouteService;
        }

        // STEP 3: Proximity + Load Balancing
        ElevatorCar proximityService = findProximityService(ElevatorCars, targetFloor);
        System.out.println("📍 Proximity Service: " + (proximityService != null ? proximityService.getId() : "none"));
        return proximityService;
    }

    private ElevatorCar findImmediateService(List<ElevatorCar> ElevatorCars, int targetFloor) {
        return ElevatorCars.stream()
                .filter(e -> e.getCurrentFloor() == targetFloor && e.getState() == ElevatorState.IDLE)
                .findFirst()
                .orElse(null);
    }

    private ElevatorCar findEnRouteService(List<ElevatorCar> ElevatorCars, int targetFloor,
            Direction requestDirection) {
        return ElevatorCars.stream()
                .filter(e -> isEnRoute(e, targetFloor, requestDirection))
                .min(Comparator.comparingInt(e -> Math.abs(e.getCurrentFloor() - targetFloor)))
                .orElse(null);
    }

    private boolean isEnRoute(ElevatorCar ElevatorCar, int targetFloor, Direction requestDirection) {
        int currentFloor = ElevatorCar.getCurrentFloor();
        Direction ElevatorCarDirection = ElevatorCar.getDirection();

        if (ElevatorCarDirection == Direction.UP && requestDirection == Direction.UP) {
            return currentFloor < targetFloor;
        } else if (ElevatorCarDirection == Direction.DOWN && requestDirection == Direction.DOWN) {
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
