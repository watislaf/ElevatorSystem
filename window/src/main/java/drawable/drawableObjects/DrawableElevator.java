package drawable.drawableObjects;

import model.objects.MovingObject.Creature;
import model.objects.MovingObject.Vector2D;
import drawable.Drawable;
import view.GameDrawer;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
public class DrawableElevator extends Creature implements Drawable {
    public final Color BACK_GROUND_COLOR;
    public final ElevatorDoors DOORS;

    @Setter
    private double serverRespondTime;
    private Vector2D interpolationPosition;

    public DrawableElevator(Creature creature, long elevatorOpenCloseTime,
                            Color elevatorBackGround,
                            Color elevatorDoors,
                            Color elevatorBorderDoors) {
        super(creature);
        BACK_GROUND_COLOR = elevatorBackGround;
        DOORS = new ElevatorDoors(this, elevatorOpenCloseTime,
                elevatorDoors, elevatorBorderDoors);
        interpolationPosition = new Vector2D(creature.getPosition());
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        if (!isVisible()) {
            return;
        }
        if (DOORS.isClosed()) {
            return;
        }
        gameDrawer.setColor(BACK_GROUND_COLOR);
        gameDrawer.fillRect(position.sub(new Vector2D(this.size.x / 2., 0)), this.size);
    }

    @Override
    public void tick(long delta_time) {
//        interpolationPosition = interpolationPosition
        //               .trendTo(position, position.getVectorTo(interpolationPosition)
        //                      .getLength() * delta_time / serverRespondTime);
    }

}
