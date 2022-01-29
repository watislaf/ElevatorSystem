package drawable.objects;

import drawable.Drawable;
import model.objects.MovingObject.Creature;
import model.objects.MovingObject.Vector2D;
import tools.GameDrawer;

import java.awt.*;

public class DrawableElevator extends Creature implements Drawable {
    public DrawableElevator(Creature creature) {
        super(creature);
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.setColor(Color.white);
        gameDrawer.drawRect(this);
    }

    @Override
    public void tick(long delta_time) {

    }
}
