package model.objects.building;

import controller.elevatorSystemController.ElevatorSystemSettings;
import lombok.Getter;
import model.objects.elevator.Elevator;
import model.objects.MovingObject.Vector2D;

import java.util.Arrays;
import java.util.Random;


public class Building {
    public final ElevatorSystemSettings SETTINGS;
    public final double WALL_SIZE;
    @Getter
    private final Elevator[] ELEVATORS;

    public Building(ElevatorSystemSettings settings) {
        this.SETTINGS = settings;
        this.WALL_SIZE = ((double) settings.BUILDING_SIZE.y) / settings.FLOORS_COUNT;
        this.ELEVATORS = new Elevator[settings.ELEVATOR_COUNT];

        for (int i = 0; i < settings.ELEVATOR_COUNT; i++) {
            ELEVATORS[i] = new Elevator(
                    new Vector2D(
                            ((double) settings.BUILDING_SIZE.x * (i + 1)) / (settings.ELEVATOR_COUNT + 1), 0),
                    settings, this.WALL_SIZE);
        }
    }

    public Vector2D getStartPositionAfterBuilding(int floorStart) {
        boolean leftCorner = new Random().nextBoolean();
        if (leftCorner) {
            return new Vector2D(0,
                    (int) (floorStart * WALL_SIZE));
        } else {
            return new Vector2D(
                    SETTINGS.BUILDING_SIZE.x,
                    (int) (floorStart * WALL_SIZE));
        }
    }

    public Vector2D getNearestButtonOnFloor(Vector2D position, int floorStart) {
        if (ELEVATORS.length == 0) {
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

    public Elevator getNearestOpenedElevatorOnFloor(Vector2D position, int floor) {
        if (!isOpenedElevatorOnFloorExist(floor)) {
            return null;
        }
        if (ELEVATORS.length == 0) {
            return null;
        }
        Elevator nearestElevator = ELEVATORS[0];
        for (var elevator : ELEVATORS) {
            if (elevator.getCurrentFloor() == floor && elevator.isOpened()) {
                var nearestElevatorPosition = position.getNearest(elevator.getPosition(),
                        nearestElevator.getPosition());
                if (nearestElevatorPosition == elevator.getPosition()) {
                    nearestElevator = elevator;
                }
            }
        }
        return nearestElevator;
    }

    private boolean isOpenedElevatorOnFloorExist(int floor) {
        return Arrays.stream(ELEVATORS).anyMatch(elevator -> elevator.getCurrentFloor() == floor && elevator.isOpened());
    }

}


