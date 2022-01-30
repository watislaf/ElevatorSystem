package drawable.drawableObjects;

import drawable.Drawable;

import lombok.Getter;
import lombok.Setter;
import model.objects.MovingObject.Creature;
import model.objects.MovingObject.Vector2D;
import tools.GameDrawer;

import java.awt.*;

public class DrawableCustomer extends Creature implements Drawable {
    Vector2D interpolationPosition;

    @Getter
    @Setter
    private double serverRespondTime = 1;

    public DrawableCustomer(Creature creature, Color[] color) {
        super(creature);
        this.color = color[(int) (getId() % color.length)];
        interpolationPosition = new Vector2D(creature.getPosition());
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
        gameDrawer.fillRect(this.interpolationPosition, this.size, Color.DARK_GRAY, 2);
    }

    @Override
    public void tick(long deltaTime) {
        if (!isVisible()) {
            interpolationPosition = position;
            return;
        }
        interpolationPosition = interpolationPosition.trendTo(position,
                position.getVectorTo(interpolationPosition).getLength()
                        * deltaTime / serverRespondTime);

    }

    public boolean isNotBehindElevator() {
        return !behindElevator;
    }

    public void changeBehindElevator() {
        behindElevator = !behindElevator;
    }
}
