package drawable.drawableObjects;

import drawable.Drawable;
import model.objects.MovingObject.Creature;
import model.objects.MovingObject.Vector2D;
import tools.GameDrawer;
import tools.Timer;

import java.awt.*;

public class ElevatorDoors extends Creature implements Drawable {

    private final long OPEN_CLOSE_DOORS_TIME;
    private final Timer DOORS_TIMER = new Timer();
    private final Creature PARENT_ELEVATOR;

    private boolean isCLosed = true;
    private final Color DOORS_COLOR;
    private final Color DOORS_BORDER;

    public ElevatorDoors(Creature creatureA, long elevatorOpenCloseTime, Color doorsColor, Color doorsBorder) {
        super(creatureA);
        size.x += 7;
        PARENT_ELEVATOR = creatureA;
        OPEN_CLOSE_DOORS_TIME = elevatorOpenCloseTime;
        this.DOORS_COLOR = doorsColor;
        this.DOORS_BORDER = doorsBorder;
    }


    public void changeDoorsState() {
        DOORS_TIMER.restart(OPEN_CLOSE_DOORS_TIME / 2);
        isCLosed = !isCLosed;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        double percentage = DOORS_TIMER.getPercent();
        if (!isCLosed) {
            percentage = 1 - percentage;
        }
        Double openedGap = percentage * size.x / 2;

        gameDrawer.setColor(DOORS_COLOR);
        gameDrawer.fillRect(position.add(new Vector2D(-size.x / 2, 0)),
                new Point((int) (size.x / 2 - openedGap), size.y), DOORS_BORDER, 2);


        gameDrawer.setColor(DOORS_COLOR);
        gameDrawer.fillRect(position.add(new Vector2D(openedGap, 0)),
                new Point((int) (size.x / 2 - openedGap), size.y), DOORS_BORDER, 2);

    }

    public void tick(long delta_time) {
        position = PARENT_ELEVATOR.getPosition();
        DOORS_TIMER.tick(delta_time);
    }

    public boolean isClosed() {
        return isCLosed && DOORS_TIMER.isReady();
    }
}
