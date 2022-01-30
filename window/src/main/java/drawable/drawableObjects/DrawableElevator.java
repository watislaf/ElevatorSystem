package drawable.drawableObjects;

import drawable.Drawable;
import lombok.Getter;
import lombok.Setter;
import model.objects.MovingObject.Creature;
import model.objects.MovingObject.Vector2D;
import tools.GameDrawer;

import java.awt.*;

public class DrawableElevator extends Creature implements Drawable {
    @Getter
    private final ElevatorDoors doors;
    @Getter
    @Setter
    private  double serverRespondTime = 1;
    @Getter
    private Vector2D interpolationPosition;
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
        interpolationPosition = new Vector2D(creature.getPosition());
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        if (!doors.isClosed()) {
            gameDrawer.setColor(backGroundColor);
            gameDrawer.fillRect(interpolationPosition.sub(new Vector2D(this.size.x / 2, 0)),
                    this.size);
        }
    }

    @Override
    public void tick(long delta_time) {
        interpolationPosition = interpolationPosition.trendTo(position,
                position.getVectorTo(interpolationPosition).getLength()
                        * delta_time / serverRespondTime);
    }

}
