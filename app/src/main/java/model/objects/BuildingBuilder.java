package model.objects;

import java.awt.*;
import java.util.Arrays;

public class BuildingBuilder {
    final int FLOORS_COUNT;
    final int ELEVATORS_COUNT;
    Elevator elevator_model = new Elevator(10);
    Elevator[] elevators;
    Point floor_size = new Point(300, 400);

    public BuildingBuilder(int FLOORS_COUNT, int ELEVATORS_COUNT) {
        Elevator.resetId();
        this.FLOORS_COUNT = FLOORS_COUNT;
        this.ELEVATORS_COUNT = ELEVATORS_COUNT;
        elevators = new Elevator[ELEVATORS_COUNT];
    }

    public BuildingBuilder elevatorModel(Elevator elevator_model) {
        this.elevator_model = elevator_model;
        return this;
    }

    public BuildingBuilder floorSize(Point floor_size) {
        this.floor_size = floor_size;
        return this;
    }

    public Building build() {
        Building building = new Building(this);
        Arrays.fill(this.elevators, new Elevator(elevator_model));
        return building;
    }
}
