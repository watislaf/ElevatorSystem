package drawable.objects;

import drawable.Drawable;

import model.objects.MovingObject.Creature;
import tools.GameDrawer;

import java.awt.*;

public class DrawableCustomer extends Creature implements Drawable {
    public DrawableCustomer(Creature creature) {
        super(creature);
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.setColor(Color.pink);
        gameDrawer.fillRect(this);
    }

    @Override
    public void tick(long deltaTime) {
    }
}
