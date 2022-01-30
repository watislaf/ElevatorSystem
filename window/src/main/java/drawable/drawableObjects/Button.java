package drawable.drawableObjects;

import drawable.Drawable;
import model.objects.MovingObject.Creature;
import model.objects.MovingObject.Vector2D;
import tools.GameDrawer;
import tools.Timer;

import java.awt.*;


public class Button extends Creature implements Drawable {
    private final long BUTTON_ON_TIME = 500;
    Timer buttonOnTimer = new Timer();
    Color color = Color.GREEN;

    public Button(Vector2D position, Point size) {
        super(position, size);
    }

    public void buttonClick() {
        buttonOnTimer.restart(BUTTON_ON_TIME);
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.setColor(color);
        gameDrawer.fillRect(this);
    }

    @Override
    public void tick(long deltaTime) {
        buttonOnTimer.tick(deltaTime);
        if (buttonOnTimer.isReady()) {
            color = Color.green;
        } else {
            color = Color.red;
        }
    }
}
