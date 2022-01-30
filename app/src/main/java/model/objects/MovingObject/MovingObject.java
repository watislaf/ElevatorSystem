package model.objects.MovingObject;


import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class MovingObject extends Creature {

    protected static final int SPEED_COEFFICIENT = 1000;
    private static final double EPSILON = 0.0001;
    @Getter
    @Setter
    protected  double speed;
    @Getter
    @Setter
    protected boolean isDead = false;


    protected Vector2D destination;

    public MovingObject(Vector2D position, double speed, Point size) {
        super(position, size);
        this.destination = position;
        this.speed = speed;
    }

    public MovingObject(Vector2D position, double speed) {
        super(position);
        this.destination = position;
        this.speed = speed;
    }

    public void tick(long delta_time) {
        if (isReachedDestination()) {
            return;
        }
        Vector2D moveDirection = new Vector2D(position).getVectorTo(destination);

        if (moveDirection.getLength() > EPSILON) {
            moveDirection = moveDirection.divide(moveDirection.getLength());
            moveDirection = moveDirection.multiply(delta_time * speed / SPEED_COEFFICIENT);
        }
        var first_vector = new Vector2D(position).add(moveDirection).getVectorTo(destination);
        var second_vector = new Vector2D(position).getVectorTo(destination);
        if (first_vector.x * second_vector.x <= EPSILON &&
                first_vector.y * second_vector.y <= EPSILON) {
            position = destination;
        } else {
            position = new Vector2D(position).add(moveDirection);
        }

    }

    public boolean isReachedDestination() {
        return new Vector2D(this.destination).getVectorTo(this.position).getLength() < EPSILON;
    }

    public void setDestination(Vector2D destination) {
        this.destination = destination;
    }

}