package drawable.drawableObjects;

import drawable.Drawable;
import model.objects.MovingObject.MovingObject;
import model.objects.MovingObject.Vector2D;
import tools.GameDrawer;
import tools.Timer;

import java.awt.*;

public class FlyingText extends MovingObject implements Drawable  {
    private final int FONT_SIZE;
    private final String TEXT;
    private final Timer TIMER;

    public FlyingText(String TEXT, Vector2D position_start, Vector2D vector_to_fly,
                      int font_size, double speed, long life_time) {
        super(position_start, speed);
        this.FONT_SIZE = font_size;
        this.destination = position_start.add(vector_to_fly);
        this.TEXT = TEXT;
        TIMER = new Timer();
        TIMER.restart(life_time);
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.setColor(Color.ORANGE);

        gameDrawer.setFont("TimesRoman", Font.PLAIN, FONT_SIZE );
        gameDrawer.drawString(TEXT, position);
    }

    @Override
    public void tick(long delta_time) {
        super.tick(delta_time);
        TIMER.tick(delta_time);
        if (TIMER.isReady()) {
            isDead = true;
        }
    }
}
