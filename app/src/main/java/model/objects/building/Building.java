package model.objects.building;

import controller.ElevatorSystemSettings;
import lombok.Getter;
import model.objects.elevator.Elevator;
import model.objects.MovingObject.Vector2D;

import java.util.Random;



public class Building {
    public final ElevatorSystemSettings SETTINGS;
    public final double WALL_SIZE;
    public final int FLOORS_COUNT;
    public final int ELEVATOR_COUNT;

    @Getter
    private Elevator[] elevators;

    public Building(ElevatorSystemSettings settings, int FLOORS_COUNT, int ELEVATOR_COUNT) {
        this.SETTINGS = settings;
        this.FLOORS_COUNT = FLOORS_COUNT;
        this.ELEVATOR_COUNT = ELEVATOR_COUNT;
        this.WALL_SIZE = ((double) settings.BUILDING_SIZE.x) / FLOORS_COUNT;
        elevators = new Elevator[ELEVATOR_COUNT];

        for (int i = 0; i < ELEVATOR_COUNT; i++) {
            elevators[i] = new Elevator(
                    new Vector2D(
                            ((double) settings.BUILDING_SIZE.x * (i + 1)) / (ELEVATOR_COUNT + 1), 0),
                    settings.ELEVATOR_SPEED, settings.ELEVATOR_SIZE,
                    this.WALL_SIZE, settings.ELEVATOR_MAX_HUMAN_CAPACITY,
                    settings.BUILDING_SIZE);
        }
    }

    public Vector2D getStartPosition(int floorStart) {
        boolean leftCorner = new Random().nextBoolean();
        if (leftCorner) {
            return new Vector2D(
                    -SETTINGS.CUSTOMER_SIZE.x / 2.,
                    (int) (floorStart * WALL_SIZE));
        } else {
            return new Vector2D(
                    SETTINGS.BUILDING_SIZE.x + SETTINGS.CUSTOMER_SIZE.x / 2.,
                    (int) (floorStart * WALL_SIZE));
        }
    }

    public Vector2D getNearestButtonOnFloor(Vector2D position, int floorStart) {
        var nearestButton = new Vector2D(SETTINGS.BUILDING_SIZE.x * 2, position.y);
        for (var elevator : elevators) {
            var buttonPosition = new Vector2D(elevator.getPosition()).add(
                    new Vector2D(SETTINGS.BUTTON_RELATIVE_POSITION, 0.));
            nearestButton = position.getNearest(buttonPosition, nearestButton);
        }
        return nearestButton;
    }

    public Vector2D getNearestOpenedElevatorOnFloor(Vector2D position, int floor) {
        if (!isOpenedElevatorOnFloorExist(floor)) {
            return null;
        }
        var nearestElevatorPosition = new Vector2D(SETTINGS.BUILDING_SIZE.x * 2, position.y);
        for (var elevator : elevators) {
            if (elevator.getCurrentFloor() == floor && elevator.isOpened()) {
                nearestElevatorPosition = position.getNearest(elevator.getPosition(), nearestElevatorPosition);
            }
        }
        return nearestElevatorPosition;
    }

    private boolean isOpenedElevatorOnFloorExist(int floor) {
        for (var elevator : elevators) {
            if (elevator.getCurrentFloor() == floor && elevator.isOpened()) {
                return true;
            }
        }
        return false;
    }

}


