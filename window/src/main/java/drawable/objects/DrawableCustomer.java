package drawable.objects;

import drawable.Drawable;

import model.objects.MovingObject.Creature;
import model.objects.MovingObject.Vector2D;
import model.objects.custumer.Customer;

import java.awt.*;

public class DrawableCustomer extends Creature implements Drawable {
    public DrawableCustomer(Vector2D position, Point size) {
        super(position, size);
    }

    public DrawableCustomer(Creature creature) {
        super(creature);
    }

    @Override
    public void Draw(Graphics g2d) {
        g2d.setColor(Color.white);
        g2d.drawRect((int) (position.x - size.x / 2.), (int) (position.y - size.y), size.x, size.y);
    }
}
