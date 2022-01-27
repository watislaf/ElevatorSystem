package model.objects;

import java.awt.*;
import java.awt.geom.Point2D;

public class MovingObject {
    private static final int SPEED_COEFFICIENT = 1000;
    private static final int EPSILON = 10 ^ -4;

    protected final double SPEED;
    protected final Point SIZE;
    protected Point2D.Double position;
    protected Point2D.Double destination;

    public MovingObject(Point2D.Double position, double speed, Point size) {
        this.position = position;
        this.destination = position;
        this.SPEED = speed;
        this.SIZE = size;
    }

    public void tick(long delta_time) {
        Point2D.Double move_direction = new Point2D.Double(
                destination.x - position.x,
                destination.y - position.y);
        var second_vector = new Point2D.Double(move_direction.x, move_direction.y);

        if (position.distance(destination) > EPSILON) {
            double vector_length =
                    Math.sqrt(
                            Math.pow(move_direction.x, 2) + Math.pow(move_direction.y, 2)
                    );
            double speed_coef = delta_time * SPEED / SPEED_COEFFICIENT;
            move_direction = new Point2D.Double(
                    speed_coef * move_direction.x / vector_length,
                    speed_coef * move_direction.y / vector_length);
        }
        var first_vector = new Point2D.Double(
                destination.x - position.x + move_direction.x,
                destination.y - position.y + move_direction.y);

        if (first_vector.x * second_vector.x <= EPSILON &&
                first_vector.y * second_vector.y <= EPSILON) {
            position = destination;
        } else {
            position = new Point2D.Double(position.x + move_direction.x,
                    position.y + move_direction.y);
        }
    }
}
