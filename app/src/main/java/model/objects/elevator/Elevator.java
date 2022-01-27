package model.objects.elevator;

import model.objects.MovingObject.MovingObject;
import model.objects.MovingObject.Vector2D;

import java.awt.*;
import java.util.PriorityQueue;

public class Elevator extends MovingObject {
    private final double WALL_SIZE;
    private final Point BUILDING_SIZE;
    private ElevatorState state;

    private final int MAX_HUMAN_CAPACITY;
    private PriorityQueue<Integer> queue;

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

    public void addFloorInQueue(int current_floor) {

    }
}
