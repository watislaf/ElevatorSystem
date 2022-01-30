package drawable.drawableObjects;

import drawable.Drawable;
import model.objects.MovingObject.Creature;
import model.objects.MovingObject.Vector2D;
import tools.GameDrawer;

import java.awt.*;

public class BlackSpace extends Creature implements Drawable {
    Color color;

    public BlackSpace(Vector2D position, Creature parentElevator, Color color, int borderSize) {
        super(position, parentElevator.getSize());
        size = new Point(size.x + borderSize * 2, size.y + borderSize);
        this.color = color;
    }


    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.setColor(color);
        gameDrawer.fillRect(this);
    }

    public void tick(long delta_time) {
    }

}

