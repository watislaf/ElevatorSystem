package model.objects.building;

import model.objects.Elevator;

import java.awt.*;
import java.awt.geom.Point2D;

public class BuildingBuilder {
    final int FLOORS_COUNT;
    final int ELEVATORS_COUNT;
    Elevator[] elevators;
    final Point BUILDING_SIZE = new Point(BuildingSettings.BUILDING_SIZE);
    final double WALL_SIZE;

    public BuildingBuilder(int FLOORS_COUNT, int ELEVATORS_COUNT) {
        this.FLOORS_COUNT = FLOORS_COUNT;
        this.ELEVATORS_COUNT = ELEVATORS_COUNT;
        this.WALL_SIZE = ((double) BUILDING_SIZE.x) / FLOORS_COUNT;
        elevators = new Elevator[ELEVATORS_COUNT];
    }


    public Building build() {
        Building building = new Building(this);
        for (int i = 0; i < ELEVATORS_COUNT; i++) {
            elevators[i] = new Elevator(
                    new Point2D.Double(
                            ((double) BUILDING_SIZE.x * (i + 1)) / (ELEVATORS_COUNT + 1), 0),
                    BuildingSettings.ELEVATOR_SPEED,
                    BuildingSettings.ELEVATOR_SIZE);
        }
        return building;
    }
}
