package controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.awt.*;


@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ElevatorSystemSettings {
    public final double ELEVATOR_SPEED = 10;
    public final Point BUILDING_SIZE = new Point(400, 896);
    public final Point ELEVATOR_SIZE = new Point(20, 50);
    public final int ELEVATOR_MAX_HUMAN_CAPACITY = 5;
    public final double BUTTON_RELATIVE_POSITION = ELEVATOR_SIZE.x + 5;
    public final Point CUSTOMER_SIZE = new Point(10, 10);
}
