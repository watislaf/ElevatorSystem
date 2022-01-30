package drawable.drawableObjects;

import drawable.Drawable;
import model.objects.MovingObject.Creature;
import model.objects.MovingObject.Vector2D;
import tools.GameDrawer;
import java.awt.*;

public class BlackSpace extends Creature implements Drawable {

    public BlackSpace(Vector2D position, Creature parentElevator) {
        super(position, parentElevator.getSize());
        size = new Point(size.x + 10, size.y + 5);
    }


    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.setColor(Color.black);

        gameDrawer.fillRect(this);
    }

    public void tick(long delta_time) {
    }

}

