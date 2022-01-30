package drawable.drawableObjects;

import drawable.Drawable;
import lombok.Getter;
import lombok.Setter;
import model.objects.MovingObject.Creature;
import tools.GameDrawer;

import java.awt.*;

public class DrawableElevator extends Creature implements Drawable {
    @Getter
    private final ElevatorDoors doors;
    @Getter
    @Setter
    private Color backGroundColor = new Color(193, 191, 255, 171);

    public DrawableElevator(Creature creature, long elevatorOpenCloseTime) {
        super(creature);
        doors = new ElevatorDoors(this, elevatorOpenCloseTime);
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        if(!doors.isCLosed()) {
            gameDrawer.setColor(backGroundColor);
            gameDrawer.fillRect(this);
        }
    }

    @Override
    public void tick(long delta_time) {
    }

}
