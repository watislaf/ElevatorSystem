package model.objects.building;

import controller.elevatorSystemController.ElevatorSystemSettings;
import model.objects.elevator.Elevator;
import model.objects.MovingObject.Vector2D;

import java.util.LinkedList;
import java.util.Random;

public class Building {
    public final LinkedList<Elevator> ELEVATORS = new LinkedList<>();
    public final ElevatorSystemSettings SETTINGS;
    public final double WALL_SIZE;

    public Building(ElevatorSystemSettings settings) {
        this.SETTINGS = settings;
        this.WALL_SIZE = ((double) settings.BUILDING_SIZE.y) / settings.FLOORS_COUNT;

        double distanceBetweenElevators = ((double) settings.BUILDING_SIZE.x) / (settings.ELEVATOR_COUNT + 1);
        for (int i = 0; i < settings.ELEVATOR_COUNT; i++) {
            ELEVATORS.add(new Elevator(new Vector2D(distanceBetweenElevators * (i + 1), 0), settings,
                    this.WALL_SIZE));
        }
    }

    public Vector2D getStartPositionAfterBuilding(int floorStart) {
        boolean spawnInLeftCorner = new Random().nextBoolean();
        if (spawnInLeftCorner) {
            return new Vector2D(0, (int) (floorStart * WALL_SIZE));
        }
        return new Vector2D(SETTINGS.BUILDING_SIZE.x, (int) (floorStart * WALL_SIZE));
    }

    public Vector2D getClosestButtonOnFloor(Vector2D position, int floorStart) {
        if (ELEVATORS.size() == 0) {
            return null;
        }
        var nearestButton = new Vector2D(SETTINGS.BUILDING_SIZE.x * 2, position.y);
        for (var elevator : ELEVATORS) {
            var buttonPosition = new Vector2D(elevator.getPosition().x + SETTINGS.BUTTON_RELATIVE_POSITION,
                    position.y);
            nearestButton = position.getNearest(buttonPosition, nearestButton);
        }
        return nearestButton;
    }

    public Elevator getClosestOpenedElevatorOnFloor(Vector2D position, int floor) {
        if (!isOpenedElevatorOnFloorExist(floor)) {
            return null;
        }
        if (ELEVATORS.size() == 0) {
            return null;
        }
        Elevator closestElevator = null;
        for (var elevator : ELEVATORS) {
            if (elevator.getCurrentFloor() == floor && elevator.isOpened()) {
                if (closestElevator == null) {
                    closestElevator = elevator;
                    continue;
                }
                var nearestElevatorPosition = position.getNearest(elevator.getPosition(),
                        closestElevator.getPosition());
                if (nearestElevatorPosition == elevator.getPosition()) {
                    closestElevator = elevator;
                }
            }
        }
        return closestElevator;
    }

    private boolean isOpenedElevatorOnFloorExist(int floor) {
        return ELEVATORS.stream().anyMatch(elevator -> elevator.getCurrentFloor() == floor && elevator.isOpened());
    }
}


