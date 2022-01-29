package controller.elevatorSystemController;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.awt.*;
import java.io.Serializable;


@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ElevatorSystemSettings implements Serializable {
    public final double ELEVATOR_SPEED = 50;
    public final long ELEVATOR_AFTER_CLOSE_AFK_TIME = 1000;
    public final long ELEVATOR_WAIT_AS_OPENED_TIME = 3000;
    public final long ELEVATOR_OPEN_CLOSE_TIME = 1500;
    public final Point ELEVATOR_SIZE = new Point(70, 110);

    public final Point BUILDING_SIZE = new Point(500, 896);

    public final int ELEVATOR_MAX_HUMAN_CAPACITY = 5;
    public final double BUTTON_RELATIVE_POSITION = ELEVATOR_SIZE.x/2 + 5;
    public final int ELEVATOR_COUNT = 4;
    public final int FLOORS_COUNT = 5;
}
