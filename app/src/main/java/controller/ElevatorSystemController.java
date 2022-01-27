package controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import model.Model;
import model.objects.MovingObject.Vector2D;
import model.objects.elevator.Elevator;
import model.objects.elevator.ElevatorRequest;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ElevatorSystemController {
    private final Model MODEL;
    LinkedList<ElevatorRequest> pending = new LinkedList<>();


    public void tick(long delta_time) {

    }

    public void buttonClick(ElevatorRequest request) {
        // closest, free, and go the same way / or wait
        LinkedList<Elevator> elevators_available = Stream
                .of(MODEL.getBuilding().getElevators())
                .filter(this::isAvailable)
                .collect(Collectors.toCollection(LinkedList::new));

        if (elevators_available.size() == 0) {
            pending.add(request);
        }
        Elevator closest_elevator = elevators_available.stream()
                .reduce(
                        new Elevator(),
                        (elevatorA, elevatorB) -> this.NearestElevator(request, elevatorA, elevatorB)
                );
        closest_elevator.addFloorInQueue(request.current_floor());
    }

    private Elevator NearestElevator(ElevatorRequest request, Elevator elevatorA, Elevator elevatorB) {
        if (Math.abs(
                elevatorA.getPosition().y - getHieghtOfFloor(request.current_floor()))
                < Math.abs(elevatorB.getCurrentFloor() - request.current_floor())) {
            return elevatorA;
        }
        return elevatorB;
    }

    private double getHieghtOfFloor(int current_floor) {
        return current_floor * MODEL.getBuilding().WALL_SIZE;
    }

    private boolean isAvailable(Elevator elevator) {
        return false;
    }

    private Elevator getFreeElevator(ElevatorRequest request) {
        return null;
    }

}
