package drawable.drawableObjects;

import drawable.Drawable;

import lombok.Getter;
import lombok.Setter;
import model.objects.MovingObject.Creature;
import tools.GameDrawer;

import java.awt.*;

public class DrawableCustomer extends Creature implements Drawable {
    public DrawableCustomer(Creature creature, Color color) {
        super(creature);
        this.color = color;
    }

    Color color;
    @Getter
    @Setter
    boolean behindElevator = true;

    @Override
    public void draw(GameDrawer gameDrawer) {
        if (!isVisible()) {
            return;
        }
        gameDrawer.setColor(color);
        gameDrawer.fillRect(this.position, this.size, Color.DARK_GRAY, 2);
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
