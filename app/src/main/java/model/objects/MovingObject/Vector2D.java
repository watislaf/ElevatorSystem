package model.objects.MovingObject;

import java.awt.geom.Point2D;

public class Vector2D extends Point2D.Double {
    public Vector2D(double x, double y) {
        super(x, y);
    }

    public Vector2D(Double position) {
        super(position.x, position.y);
    }

    public static double distance(Double positionA, Double positionB) {
        return Math.sqrt((positionA.x - positionB.x) * (positionA.y - positionB.y));
    }

    public Vector2D getVectorTo(Double destination) {
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

    public Vector2D add(Double second) {
        return new Vector2D(x + second.x, y + second.y);
    }

    public Vector2D getNearest(Vector2D vectorA, Vector2D vectorB) {
        if (Vector2D.distance(this, vectorA) <
                Vector2D.distance(this, vectorB)) {
            return vectorA;
        }
        return vectorB;
    }
}
