package model.objects.MovingObject;


import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class MovingObject extends Creature {
    protected static final int SPEED_COEFFICIENT = 1000;

    public double getSpeed() {
        return speed * speedMultiPly;
    }

    private double speed;
    @Getter
    @Setter
    protected double speedMultiPly = 1;

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
        position = position.trendTo(destination, delta_time * speed / SPEED_COEFFICIENT);
    }

    public boolean isReachedDestination() {
        return new Vector2D(this.destination).getVectorTo(this.position).getLength() < Vector2D.EPSILON;
    }

    public void setDestination(Vector2D destination) {
        this.destination = destination;
    }

}