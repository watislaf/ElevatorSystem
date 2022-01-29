package drawable.objects;

import drawable.Drawable;
import lombok.Getter;
import model.objects.MovingObject.Creature;
import tools.GameDrawer;

import java.awt.*;

public class DrawableElevator extends Creature implements Drawable {
    @Getter
    private final DrawableElevatorDoors doors;

    public DrawableElevator(Creature creature, long elevatorOpenCloseTime) {
        super(creature);
        doors = new DrawableElevatorDoors(this,elevatorOpenCloseTime);
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.setColor(Color.GRAY);
        gameDrawer.setColor(Color.white);
        gameDrawer.fillRect(this);
    }

    @Override
    public void tick(long delta_time) {
        doors.tick(delta_time);

    }

}
