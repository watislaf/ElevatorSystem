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
    Color currentColor;
    Color colorOn;
    Color colorOff;

    public Button(Vector2D position, Point size, Color colorOn, Color colorOff) {
        super(position, size);
        this.colorOn = colorOn;
        this.colorOff = colorOff;
        this.currentColor = colorOn;
    }

    public void buttonClick() {
        buttonOnTimer.restart(BUTTON_ON_TIME);
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.setColor(currentColor);
        gameDrawer.fillRect(this);
    }

    @Override
    public void tick(long deltaTime) {
        buttonOnTimer.tick(deltaTime);
        if (buttonOnTimer.isReady()) {
            currentColor = Color.green;
        } else {
            currentColor = Color.red;
        }
    }
}
