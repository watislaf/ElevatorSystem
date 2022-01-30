package drawable.drawableObjects;

import drawable.Drawable;
import model.objects.MovingObject.Creature;
import model.objects.MovingObject.Vector2D;
import tools.GameDrawer;
import java.awt.*;

public class ElevatorBorder extends Creature implements Drawable {
    private final Creature PARENT_ELEVATOR;
    private final int WALL_SIZE;
    private final int BORDER_SIZE = 3;
    public ElevatorBorder(Vector2D position, Creature parentElevator, int WALL_SIZE) {
        super(position, parentElevator.getSize());
        size = new Point(size.x + BORDER_SIZE*2-1, size.y + BORDER_SIZE);
        PARENT_ELEVATOR = parentElevator;
        this.WALL_SIZE = WALL_SIZE;
    }


    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.setColor(new Color(12, 135, 161));

        gameDrawer.drawRect(this.position,this.size,BORDER_SIZE+4);

        gameDrawer.setFont("TimesRoman", Font.PLAIN, 15);
        var positionOfText = position;

        gameDrawer.setColor(new Color(16, 255, 0));
        gameDrawer.drawString(
                ((int) (PARENT_ELEVATOR.getPosition().y / WALL_SIZE +2)) + "", positionOfText.add(
                        new Vector2D(-2, size.y-BORDER_SIZE)));
    }

    public void tick(long delta_time) {
    }

}
