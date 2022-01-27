package model.objects.building;

import model.objects.Elevator;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

public class Building {
    private final int FLOORS_COUNT;
    private final int ELEVATORS_COUNT;
    final Point BUILDING_SIZE = new Point(BuildingSettings.BUILDING_SIZE);
    final double WALL_SIZE;
    Elevator[] elevators;

    public Building(BuildingBuilder builder) {
        this.FLOORS_COUNT = builder.FLOORS_COUNT;
        this.ELEVATORS_COUNT = builder.ELEVATORS_COUNT;
        this.WALL_SIZE = builder.WALL_SIZE;
        this.elevators = builder.elevators;
    }

    public Point2D.Double getStartPosition(int floor_start) {
        boolean left_corner = new Random().nextBoolean();
        if (left_corner) {
            return new Point2D.Double(
                    -BuildingSettings.CUSTOMER_SIZE.x / 2.,
                    (int) (floor_start * WALL_SIZE));
        } else {
            return new Point2D.Double(
                    BUILDING_SIZE.x + BuildingSettings.CUSTOMER_SIZE.x / 2.,
                    (int) (floor_start * WALL_SIZE));
        }
    }
}


