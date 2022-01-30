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
    private Color backGroundColor;

    public DrawableElevator(Creature creature, long elevatorOpenCloseTime,
                            Color elevatorBackGround,
                            Color elevatorDoors,
                            Color elevatorBorderDoors) {
        super(creature);
        backGroundColor = elevatorBackGround;
        doors = new ElevatorDoors(this, elevatorOpenCloseTime,
                elevatorDoors, elevatorBorderDoors);
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        if (!doors.isClosed()) {
            gameDrawer.setColor(backGroundColor);
            gameDrawer.fillRect(this);
        }
    }

    @Override
    public void tick(long delta_time) {
    }

}
