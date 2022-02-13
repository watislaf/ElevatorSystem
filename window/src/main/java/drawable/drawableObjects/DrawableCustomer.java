package drawable.drawableObjects;

import drawable.Drawable;

import model.objects.movingObject.Creature;
import model.objects.movingObject.Vector2D;
import view.GameDrawer;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class DrawableCustomer extends Creature implements Drawable {
    private final Color COLOR_OF_CUSTOMER;

    @Getter
    @Setter
    private boolean behindElevator = true;
    @Getter
    @Setter
    private double serverRespondTime;
    private Vector2D interpolationPosition;

    public DrawableCustomer(Creature creature, Color[] color) {
        super(creature);
        this.COLOR_OF_CUSTOMER = color[(int) (getId() % color.length)];
        interpolationPosition = new Vector2D(creature.getPosition());
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        if (!isVisible()) {
            behindElevator = false;
            return;
        }
        gameDrawer.setColor(COLOR_OF_CUSTOMER);
        gameDrawer.fillRect(this.position, this.size, Color.DARK_GRAY, 2);
    }

    @Override
    public void tick(long deltaTime) {
        if (!isVisible()) {
            interpolationPosition = position;
            return;
        }
//        interpolationPosition = interpolationPosition
 //               .trendTo(position, position.getVectorTo(interpolationPosition).getLength()
  //                      * deltaTime / serverRespondTime);
    }

    public boolean isNotBehindElevator() {
        return !behindElevator;
    }

    public void changeBehindElevator() {
        behindElevator = !behindElevator;
    }
}
