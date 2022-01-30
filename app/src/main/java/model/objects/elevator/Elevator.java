package model.objects.elevator;

import controller.elevatorSystemController.ElevatorSystemSettings;
import lombok.Getter;
import lombok.Setter;
import model.objects.MovingObject.MovingObject;
import model.objects.MovingObject.Vector2D;
import model.objects.custumer.Customer;

import java.awt.*;
import java.util.*;

public class Elevator extends MovingObject {
    private final long TIME_TO_STOP_ON_FLOOR;
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
        this.TIME_TO_STOP_ON_FLOOR = settings.ELEVATOR_OPEN_CLOSE_TIME * 2 +
                settings.ELEVATOR_AFTER_CLOSE_AFK_TIME + settings.ELEVATOR_WAIT_AS_OPENED_TIME;
    }

    public int getCurrentFloor() {
        return (int) Math.round(position.y / WALL_SIZE);
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
        int currentFloor = getCurrentFloor();
        if (mapToWorkWith.containsKey(currentFloor)) {
            currentBookedCount -= mapToWorkWith.get(currentFloor);
            mapToWorkWith.remove(getCurrentFloor());
        }

        SortedSet<Integer> setToWorkWith;
        if (isGoUp) {
            setToWorkWith = THROW_OUT_TOP;
        } else {
            setToWorkWith = THROW_OUT_BOTTOM;
        }
        setToWorkWith.remove(currentFloor);
    }

    public boolean isInMotion() {
        return state.equals(ElevatorState.IN_MOTION);
    }

    public void addFloorToThrowOut(int floorEnd) {
        SortedSet<Integer> setToWorkWith;
        if (floorEnd > getCurrentFloor()) {
            setToWorkWith = THROW_OUT_TOP;
        } else {
            setToWorkWith = THROW_OUT_BOTTOM;
        }
        setToWorkWith.add(floorEnd);
    }

    public double getTimeToBeeHere(int requestFloor) {
        if (getBooking() == 0) {
            return getTimeToGetTo(requestFloor);
        }
        TreeSet<Integer> allTop = new TreeSet<>();
        TreeSet<Integer> allBot = new TreeSet<>();

        PICK_UP_TOP.forEach((key, value) -> allTop.add(key));
        allTop.addAll(THROW_OUT_TOP.stream().toList());
        PICK_UP_BOTTOM.forEach((key, value) -> allBot.add(key));
        allBot.addAll(THROW_OUT_BOTTOM.stream().toList());

        allTop.add(requestFloor);
        allBot.add(requestFloor);
        Integer[] allTopSorted = (allTop).toArray(new Integer[allTop.size()]);
        Integer[] allBotSorted = (allBot).toArray(new Integer[allBot.size()]);

        // 5 6 7 9  on the way
        //1 2 4not on the way
        double penalty = 0;
        int curr = getCurrentFloor();
        if (isGoUp) {
            if (position.y < getPositionForFloor(requestFloor)) {
                penalty = Arrays.binarySearch(allTopSorted, requestFloor) * TIME_TO_STOP_ON_FLOOR;
            } else {
                penalty = getTimeToGetTo(allTopSorted[allTopSorted.length - 1]) * 2;
                penalty += (allBotSorted.length - 1 - (Arrays.binarySearch(allBotSorted, requestFloor))
                        + allTopSorted.length - 1)
                        * TIME_TO_STOP_ON_FLOOR;
            }

            return getTimeToGetTo(requestFloor) + penalty;
        }
        if (position.y > getPositionForFloor(requestFloor)) {
            penalty = (allBotSorted.length - Arrays.binarySearch(allBotSorted, requestFloor) - 1)
                    * TIME_TO_STOP_ON_FLOOR;
        } else {
            penalty = getTimeToGetTo(allBotSorted[allBotSorted.length - 1]) * 2;
            penalty += ((Arrays.binarySearch(allTopSorted, requestFloor) + allBotSorted.length - 1)
                    * TIME_TO_STOP_ON_FLOOR);
        }
        return getTimeToGetTo(requestFloor) + penalty;
    }


    private double getPositionForFloor(int requestFloor) {
        return requestFloor * WALL_SIZE;
    }

    private double getTimeToGetTo(int requestFloor) {
        return Math.abs(getPositionForFloor(requestFloor) - position.y) * SPEED_COEFFICIENT / getSpeed();
    }

    public int getBooking() {
        return currentBookedCount + currentCustomersCount;
    }
}
