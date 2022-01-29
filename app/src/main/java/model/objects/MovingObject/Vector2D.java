package model.objects.MovingObject;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;

public class Vector2D extends Point2D.Double implements Serializable {
    public Vector2D(double x, double y) {
        super(x, y);
    }

    public Vector2D(Point2D.Double position) {
        super(position.x, position.y);
    }

    public Vector2D(Point position) {
        super(position.x, position.y);
    }

    public static double distance(Point2D.Double positionA, Point2D.Double positionB) {
        return Math.sqrt((positionA.x - positionB.x) * (positionA.x - positionB.x)
                + (positionA.y - positionB.y) * (positionA.y - positionB.y));
    }

    public Vector2D getVectorTo(Point2D.Double destination) {
        return new Vector2D(destination.x - x, destination.y - y);
    }

    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2D divide(double length) {
        return new Vector2D(x / length, y / length);
    }

    public Vector2D multiply(double length) {
        return new Vector2D(x * length, y * length);
    }

    public Vector2D add(Point2D.Double second) {
        return new Vector2D(x + second.x, y + second.y);
    }

    public Vector2D getNearest(Vector2D vectorA, Vector2D vectorB) {
        if (Vector2D.distance(this, vectorA) <
                Vector2D.distance(this, vectorB)) {
            return vectorA;
        }
        return vectorB;
    }

    public Vector2D sub(Vector2D vectorB) {
        this.x -= vectorB.x;
        this.y -= vectorB.y;
        return this;
    }
}
