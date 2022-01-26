package model.objects;

import java.awt.*;

public class Elevator {
    private static int id_counter = 0;
    private int ID = 0;
    private final double MAX_SPEED;
    private double current_speed = 0;
    private Point position = new Point(0, 0);

    public Elevator(Elevator elevator_model) {
        this.MAX_SPEED = elevator_model.MAX_SPEED;
        this.ID = id_counter++;
    }

    public Elevator(double max_speed) {
        this.MAX_SPEED = max_speed;
    }

    public static void resetId() {
        id_counter = 0;
    }
}
