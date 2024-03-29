package drawable.drawableObjects;

import model.objects.movingObject.Creature;
import model.objects.movingObject.Vector2D;
import drawable.Drawable;
import view.GameDrawer;
import tools.Timer;

import java.awt.*;

public class ElevatorDoors extends Creature implements Drawable {
    private final long OPEN_CLOSE_DOORS_TIME;
    private final DrawableElevator PARENT_ELEVATOR;
    private final Timer DOORS_TIMER = new Timer();

    private boolean isCLosed = true;
    private final Color DOORS_COLOR;
    private final Color DOORS_BORDER;

    public ElevatorDoors(DrawableElevator parentElevator, long elevatorOpenCloseTime, Color doorsColor, Color doorsBorder) {
        super(parentElevator);
        size.x += 7;
        PARENT_ELEVATOR = parentElevator;
        OPEN_CLOSE_DOORS_TIME = elevatorOpenCloseTime;
        this.DOORS_COLOR = doorsColor;
        this.DOORS_BORDER = doorsBorder;
    }

    public void changeDoorsState(boolean newState) {
        if (isCLosed == newState) {
            return;
        }
        DOORS_TIMER.restart(OPEN_CLOSE_DOORS_TIME / 2);
        isCLosed = !isCLosed;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        double percentage = DOORS_TIMER.getPercent();
        if (!PARENT_ELEVATOR.isVisible()) {
            return;
        }
        if (!isCLosed) {
            percentage = 1 - percentage;
        }
        var openedGap = percentage * size.x / 2.;

        gameDrawer.setColor(DOORS_COLOR);
        gameDrawer.fillRect(position.add(new Vector2D(-size.x / 2., 0)),
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
