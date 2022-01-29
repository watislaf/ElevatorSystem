package tools;

import model.objects.MovingObject.Creature;
import model.objects.MovingObject.Vector2D;

import java.awt.*;

public class GameDrawer {
    // The ratio of game coordinates to real
    private final Graphics2D GRAPHICS_2D;
    private final Point REAL_SIZE;
    private final double SCALING_COEFFICIENT;
    private final Point originalOffset;

    public GameDrawer(Point gameSize, Point realSize, Graphics2D g2d) {
        this.GRAPHICS_2D = g2d;
        this.REAL_SIZE = realSize;
        SCALING_COEFFICIENT = Math.max(((double) gameSize.x) / REAL_SIZE.x,
                ((double) gameSize.y) / REAL_SIZE.y);
        Point new_size = new Point((int) (((double) gameSize.x) / SCALING_COEFFICIENT),
                (int) (((double) gameSize.y) / SCALING_COEFFICIENT));

        originalOffset = new Point((REAL_SIZE.x - new_size.x) / 2, (REAL_SIZE.y - new_size.y) / 2);
    }


    public Vector2D windowToGameCoordinate(Vector2D window_coordinate) {
        return (window_coordinate.sub(new Vector2D(originalOffset))).multiply(SCALING_COEFFICIENT);
    }

    public Vector2D gameToWindowCoordinate(Vector2D game_coordinate) {
        return (game_coordinate.divide(SCALING_COEFFICIENT)).add(new Vector2D((originalOffset)));
    }

    public Vector2D gameToWindowSize(Vector2D game_size) {
        return game_size.divide(SCALING_COEFFICIENT);
    }

    public double windowToGameLength(double length) {
        return length * SCALING_COEFFICIENT;
    }

    public double gameToWindowLength(double game_length) {
        return game_length / SCALING_COEFFICIENT;
    }


    public void setColor(Color red) {
        GRAPHICS_2D.setColor(red);
    }

    public void fillRect(Vector2D position, Point size) {
        GRAPHICS_2D.fillRect(
                (int) (originalOffset.x + (position.x) / SCALING_COEFFICIENT),
                (int) (REAL_SIZE.y - originalOffset.y - (position.y + size.y) / SCALING_COEFFICIENT),
                (int) (size.x / SCALING_COEFFICIENT),
                (int) (size.y / SCALING_COEFFICIENT)
        );
    }

    public void setFont(String fontName, int type, int size) {
        GRAPHICS_2D.setFont(new Font(fontName, type, (int) (size / SCALING_COEFFICIENT)));
    }

    public void drawString(String text, Vector2D position) {
        GRAPHICS_2D.drawString(text, (int) (position.x / SCALING_COEFFICIENT + originalOffset.x),
                (int) (REAL_SIZE.y - position.y / SCALING_COEFFICIENT - originalOffset.y));
    }

    public void fillRect(Creature creature) {
        GRAPHICS_2D.fillRect(
                (int) (originalOffset.x + creature.getPosition().x / SCALING_COEFFICIENT),
                (int) (REAL_SIZE.y - originalOffset.y -
                        (creature.getSize().y + creature.getPosition().y) / SCALING_COEFFICIENT),
                (int) (creature.getSize().x / SCALING_COEFFICIENT),
                (int) (creature.getSize().y / SCALING_COEFFICIENT)
        );
    }

    public void drawRect(Vector2D position, Point size, double thickness) {
        Stroke oldStroke = GRAPHICS_2D.getStroke();
        GRAPHICS_2D.setStroke(new BasicStroke((float) (thickness / SCALING_COEFFICIENT)));

        GRAPHICS_2D.drawRect(
                (int) (originalOffset.x + (position.x) / SCALING_COEFFICIENT),
                (int) (REAL_SIZE.y - originalOffset.y - (position.y + size.y) / SCALING_COEFFICIENT),
                (int) (size.x / SCALING_COEFFICIENT),
                (int) (size.y / SCALING_COEFFICIENT)
        );

        GRAPHICS_2D.setStroke(oldStroke);

    }
}
