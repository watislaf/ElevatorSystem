package drawable.objects;

import drawable.Drawable;
import model.objects.MovingObject.Creature;
import model.objects.MovingObject.Vector2D;

import java.awt.*;

public class DrawableElevator extends Creature implements Drawable {
    public DrawableElevator(Vector2D position, Point size) {
        super(position, size);
    }

    public DrawableElevator(Creature creature) {
        super(creature);
    }

    @Override
    public void Draw(Graphics g2d) {
        g2d.setColor(Color.white);
        g2d.drawRect((int) (position.x - size.x / 2.), (int) (position.y - size.y), size.x, size.y);
    }
}
