package drawable.objects;

import drawable.Drawable;

import model.objects.MovingObject.Creature;
import model.objects.MovingObject.Vector2D;
import model.objects.custumer.Customer;
import tools.GameDrawer;

import java.awt.*;

public class DrawableCustomer extends Creature implements Drawable {
    public DrawableCustomer(Creature creature) {
        super(creature);
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.setColor(Color.white);
        gameDrawer.drawRect(this);
    }

    @Override
    public void tick(long deltaTime) {
    }
}
