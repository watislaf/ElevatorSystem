package model.objects.elevator;

import model.objects.MovingObject.MovingObject;
import model.objects.MovingObject.Vector2D;

import java.awt.*;
import java.util.*;

public class Elevator extends MovingObject {
    private final int UNEXIST_FLOOR = 999;

    private final double WALL_SIZE;
    private final Point BUILDING_SIZE;
    private ElevatorState state;
    boolean isGoUp = false;

    private final int MAX_HUMAN_CAPACITY;
    private int current_human_count = 0;
    private int current_booked_count = 0;

    private TreeMap<Integer, Integer> pickUpTop = new TreeMap<>();
    private TreeSet<Integer> throwUtTop = new TreeSet<Integer>();
    private TreeMap<Integer, Integer> pickUpBottom = new TreeMap<>(Comparator.reverseOrder());
    private TreeSet<Integer> throwOutBottom = new TreeSet<Integer>();

    @Override
    public void tick(long deltaTime) {
        switch (state) {
            case WAIT -> {
                int best_floor = findBestFloor(deltaTime);
                if (best_floor != UNEXIST_FLOOR) {
                    setDestination(new Vector2D(position.x,
                            best_floor * WALL_SIZE));
                    state = ElevatorState.IN_MOTION;
                }
            }
        }
        super.tick(deltaTime);
    }

    private int findBestFloor(long deltaTime) {
        Map<Integer, Integer> map_to_work_with;
        Integer floorToGetUp = UNEXIST_FLOOR;
        if (!pickUpTop.isEmpty()) {
            floorToGetUp = Math.min(pickUpTop.firstKey(), floorToGetUp);
        }
        if (!throwUtTop.isEmpty()) {
            floorToGetUp = Math.min(throwUtTop.first(), floorToGetUp);
        }
        if (isGoUp && floorToGetUp != UNEXIST_FLOOR) {
            return floorToGetUp;
        }
        Integer floorToGetDown = -UNEXIST_FLOOR;
        if (!pickUpBottom.isEmpty()) {
            floorToGetDown = Math.max(pickUpBottom.firstKey(), floorToGetDown);
        }
        if (!throwOutBottom.isEmpty()) {
            floorToGetDown = Math.max(throwOutBottom.first(), floorToGetDown);
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
        return current_booked_count + current_human_count <= this.MAX_HUMAN_CAPACITY;
    }

    public Elevator() {
        this(new Vector2D(0, -999), 0, new Point(0, 0),
                0, 0, new Point(0, 0));
    }

    public Elevator(Vector2D position, double speed, Point size,
                    double wall_size, int MAX_HUMAN_CAPACITY,
                    Point BUILDING_SIZE) {
        super(position, speed, size);
        this.WALL_SIZE = wall_size;
        this.state = ElevatorState.WAIT;
        this.MAX_HUMAN_CAPACITY = MAX_HUMAN_CAPACITY;
        this.BUILDING_SIZE = BUILDING_SIZE;
    }

    public int getCurrentFloor() {
        return (int) Math.floor(position.x / (BUILDING_SIZE.y / WALL_SIZE));
    }

    public boolean isOpened() {
        return ElevatorState.OPENED.equals(state);
    }

    public void addFloorToPickUp(int toAddFloor) {
        current_booked_count++;
        Map<Integer, Integer> map_to_work_with;
        if (state == ElevatorState.WAIT || toAddFloor > getCurrentFloor()) {
            map_to_work_with = pickUpTop;
        } else {
            map_to_work_with = pickUpBottom;
        }
        if (map_to_work_with.containsKey(toAddFloor)) {
            map_to_work_with.put(toAddFloor, map_to_work_with.get(toAddFloor) + 1);
        } else {
            map_to_work_with.put(toAddFloor, 0);
        }
    }
}
