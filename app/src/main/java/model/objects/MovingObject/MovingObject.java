package model.objects.MovingObject;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class MovingObject extends Creature {
    @Getter
    @Setter
    private boolean reachedDestination = true;

    private static final int SPEED_COEFFICIENT = 1000;
    private static final int EPSILON = 10 ^ -4;

    protected final double SPEED;


    protected Vector2D destination;

    public MovingObject(Vector2D position, double speed, Point size) {
        super(position, size);
        this.destination = position;
        this.SPEED = speed;
    }

    public void tick(long delta_time) {
        if (reachedDestination) {
            return;
        }
        Vector2D moveDirection = new Vector2D(position);
        moveDirection.getVectorTo(destination);

        if (moveDirection.getLength() > EPSILON) {
            moveDirection = moveDirection.divide(moveDirection.getLength());
            moveDirection = moveDirection.multiply(delta_time * SPEED / SPEED_COEFFICIENT);
        }
        var first_vector = new Vector2D(position).add(moveDirection).getVectorTo(destination);
        var second_vector = new Vector2D(position).getVectorTo(destination);
        if (first_vector.x * second_vector.x <= EPSILON &&
                first_vector.y * second_vector.y <= EPSILON) {
            position = destination;
            reachedDestination = true;
        } else {
            position = new Vector2D(position).add(moveDirection);
            reachedDestination = false;
        }
    }

    public void setDestination(Vector2D destination) {
        this.destination = destination;
        if (new Vector2D(this.destination).getVectorTo(this.position).getLength() > EPSILON) {
            reachedDestination = false;
        }
    }
}