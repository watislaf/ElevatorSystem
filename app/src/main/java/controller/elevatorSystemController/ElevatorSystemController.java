package controller.elevatorSystemController;

import controller.customerController.CustomerSettings;
import lombok.RequiredArgsConstructor;
import model.Model;
import model.objects.building.Building;
import model.objects.elevator.Elevator;
import model.objects.elevator.ElevatorRequest;

import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ElevatorSystemController {
    private final Model MODEL;
    public final ElevatorSystemSettings SETTINGS = new ElevatorSystemSettings();

    LinkedList<ElevatorRequest> pending = new LinkedList<>();

    private final Building DEFAULT_BUILDING = new Building(SETTINGS, 5, 4);

    public ElevatorSystemController(Model model) {
        this.MODEL = model;
        model.Initialize(DEFAULT_BUILDING);
    }


    public void tick(long delta_time) {
        pending.removeIf(this::tryToCallElevator);

        for (var elevator : MODEL.getBuilding().getElevators()) {
            elevator.tick(delta_time);
        }
    }

    public void buttonClick(ElevatorRequest request) {
        if (!tryToCallElevator(request)) {
            pending.add(request);
        }
    }

    private boolean tryToCallElevator(ElevatorRequest request) {
        // closest, free, and go the same way / or wait
        LinkedList<Elevator> elevators_available = Stream
                .of(MODEL.getBuilding().getElevators())
                .filter(Elevator::isAvailable)
                .collect(Collectors.toCollection(LinkedList::new));

        if (elevators_available.size() == 0) {
            return false;
        }
        Elevator closest_elevator = elevators_available.stream()
                .reduce(
                        new Elevator(),
                        (elevatorA, elevatorB) -> this.NearestElevator(request, elevatorA, elevatorB)
                );
        closest_elevator.addFloorToPickUp(request.current_floor());
        return true;
    }

    private Elevator NearestElevator(ElevatorRequest request, Elevator elevatorA, Elevator elevatorB) {
        if (Math.abs(
                elevatorA.getPosition().y - getHeightOfFloor(request.current_floor()))
                < Math.abs(elevatorB.getCurrentFloor() - request.current_floor())) {
            return elevatorA;
        }
        return elevatorB;
    }

    private double getHeightOfFloor(int current_floor) {
        return current_floor * MODEL.getBuilding().WALL_SIZE;
    }
}
