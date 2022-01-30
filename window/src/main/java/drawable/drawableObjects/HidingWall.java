package drawable.drawableObjects;

import drawable.Drawable;
import model.objects.MovingObject.Creature;
import model.objects.MovingObject.Vector2D;
import tools.GameDrawer;

import java.awt.*;

public class HidingWall extends Creature implements Drawable {
    private final Color BACK_GROUND_COLOR;

    public HidingWall(Vector2D position, Point size, Color backGroundColor) {
        super(position, size);
        this.BACK_GROUND_COLOR = backGroundColor;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.setColor(BACK_GROUND_COLOR);
        gameDrawer.fillRect(this);
    }

    public void tick(long delta_time){};
}


