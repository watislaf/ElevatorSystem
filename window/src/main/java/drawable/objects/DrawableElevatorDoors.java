package drawable.objects;

import drawable.Drawable;
import model.objects.MovingObject.Creature;
import model.objects.MovingObject.Vector2D;
import tools.GameDrawer;
import tools.Timer;

import java.awt.*;

public class DrawableElevatorDoors extends Creature implements Drawable {

    private final long OPEN_CLOSE_DOORS_TIME;
    private final Timer DOORS_TIMER = new Timer();
    private final Creature PARENT_ELEVATOR;
    boolean isCLosed = true;

    public DrawableElevatorDoors(Creature creatureA, long elevatorOpenCloseTime) {
        super(creatureA);
        PARENT_ELEVATOR = creatureA;
        OPEN_CLOSE_DOORS_TIME = elevatorOpenCloseTime;
    }


    public void changeDoorsState() {
        DOORS_TIMER.restart(OPEN_CLOSE_DOORS_TIME);
        isCLosed = !isCLosed;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.setColor(Color.ORANGE);
        double percentage = DOORS_TIMER.getPercent();
        if (!isCLosed) {
            percentage = 1 - percentage;
        }
        Double openedGap = percentage * size.x / 2;

        gameDrawer.fillRect(position.add(new Vector2D(-size.x / 2, 0)),
                new Point((int) (size.x / 2 - openedGap), size.y));
        gameDrawer.fillRect(position.add(new Vector2D( openedGap, 0)),
                new Point((int) (size.x / 2 - openedGap), size.y));
    }

    public void tick(long delta_time) {
        position = PARENT_ELEVATOR.getPosition();
        DOORS_TIMER.tick(delta_time);
    }

}
