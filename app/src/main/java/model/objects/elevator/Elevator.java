package model.objects.elevator;

import controller.elevatorSystemController.ElevatorSystemSettings;
import lombok.Getter;
import lombok.Setter;
import model.objects.MovingObject.MovingObject;
import model.objects.MovingObject.Vector2D;
import model.objects.custumer.Customer;
import tools.Timer;

import java.awt.*;
import java.util.*;

public class Elevator extends MovingObject {
    public final int UNEXIST_FLOOR = 999;

    private final double WALL_SIZE;
    private final Point BUILDING_SIZE;
    @Getter
    @Setter
    private ElevatorState state;
    private boolean isGoUp = false;

    private final int MAX_HUMAN_CAPACITY;

    private int currentCustomersCount = 0;
    private int currentBookedCount = 0;
    // Floor and how many people are waiting
    private final TreeMap<Integer, Integer> PICK_UP_TOP = new TreeMap<>();
    private final TreeSet<Integer> THROW_OUT_TOP = new TreeSet<>();
    private final TreeMap<Integer, Integer> PICK_UP_BOTTOM = new TreeMap<>(Comparator.reverseOrder());
    private final TreeSet<Integer> THROW_OUT_BOTTOM = new TreeSet<>();

    private final LinkedList<Customer> CUSTOMERS_INSIDE = new LinkedList<>();

    @Override
    public void tick(long deltaTime) {
        super.tick(deltaTime);
    }


    public int findBestFloor() {
        int floorToGetUp = UNEXIST_FLOOR;
        if (!PICK_UP_TOP.isEmpty()) {
            floorToGetUp = Math.min(PICK_UP_TOP.firstKey(), floorToGetUp);
        }
        if (!THROW_OUT_TOP.isEmpty()) {
            floorToGetUp = Math.min(THROW_OUT_TOP.first(), floorToGetUp);
        }
        if (isGoUp && floorToGetUp != UNEXIST_FLOOR) {
            return floorToGetUp;
        }
        int floorToGetDown = -UNEXIST_FLOOR;
        if (!PICK_UP_BOTTOM.isEmpty()) {
            floorToGetDown = Math.max(PICK_UP_BOTTOM.firstKey(), floorToGetDown);
        }
        if (!THROW_OUT_BOTTOM.isEmpty()) {
            floorToGetDown = Math.max(THROW_OUT_BOTTOM.first(), floorToGetDown);
        }
        if (!isGoUp && floorToGetDown != -UNEXIST_FLOOR) {
            return floorToGetDown;
        }
        if (!isGoUp && floorToGetUp != UNEXIST_FLOOR) {
            isGoUp = true;
            return floorToGetUp;
        }
        if (isGoUp && floorToGetDown != -UNEXIST_FLOOR) {
            isGoUp = false;
            return floorToGetDown;
        }
        return UNEXIST_FLOOR;
    }

    public boolean isAvailable() {
        return currentBookedCount + currentCustomersCount <= this.MAX_HUMAN_CAPACITY;
    }


    public Elevator(Vector2D position, ElevatorSystemSettings settings,
                    double wall_size) {
        super(position, settings.ELEVATOR_SPEED, settings.ELEVATOR_SIZE);
        this.MAX_HUMAN_CAPACITY = settings.ELEVATOR_MAX_HUMAN_CAPACITY;
        this.BUILDING_SIZE = settings.BUILDING_SIZE;
        this.state = ElevatorState.WAIT;
        this.WALL_SIZE = wall_size;
    }

    public int getCurrentFloor() {
        return (int) Math.floor(position.x / (BUILDING_SIZE.y / WALL_SIZE));
    }

    public boolean isOpened() {
        return ElevatorState.OPENED.equals(state);
    }

    public void addFloorToPickUp(int toAddFloor) {
        currentBookedCount++;
        Map<Integer, Integer> mapToWorkWith;
        if (toAddFloor > getCurrentFloor()) {
            mapToWorkWith = PICK_UP_TOP;
        } else {
            mapToWorkWith = PICK_UP_BOTTOM;
        }
        if (mapToWorkWith.containsKey(toAddFloor)) {
            mapToWorkWith.put(toAddFloor, mapToWorkWith.get(toAddFloor) + 1);
        } else {
            mapToWorkWith.put(toAddFloor, 1);
        }
    }

    public void put() {
        currentCustomersCount++;

    }

    public void remove() {
        currentCustomersCount--;
    }

    public void setFloorDestination(int bestFloor) {
        setDestination(new Vector2D(position.x, bestFloor * WALL_SIZE));
    }

    public void arrived() {
        Map<Integer, Integer> mapToWorkWith;
        if (isGoUp) {
            mapToWorkWith = PICK_UP_TOP;
        } else {
            mapToWorkWith = PICK_UP_BOTTOM;
        }
        currentBookedCount -= mapToWorkWith.get(getCurrentFloor());
        mapToWorkWith.remove(getCurrentFloor());
    }

    public boolean isInMotion() {
        return state.equals(ElevatorState.IN_MOTION);
    }
}
