package drawable.drawableObjects;

import drawable.Drawable;

import lombok.Getter;
import lombok.Setter;
import model.objects.MovingObject.Creature;
import tools.GameDrawer;

import java.awt.*;

public class DrawableCustomer extends Creature implements Drawable {
    public DrawableCustomer(Creature creature) {
        super(creature);
    }

    @Getter
    @Setter
    boolean behindElevator = true;

    @Override
    public void draw(GameDrawer gameDrawer) {
        if (!isVisible()) {
            return;
        }
        gameDrawer.setColor(Color.pink);
        gameDrawer.fillRect(this);
    }

    @Override
    public void tick(long deltaTime) {
    }

    public boolean isNotBehindElevator() {
        return !behindElevator;
    }

    public void changeBehindElevator() {
        behindElevator = !behindElevator;
    }
}
