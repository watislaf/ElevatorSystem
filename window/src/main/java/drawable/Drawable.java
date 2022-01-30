package drawable;

import model.objects.MovingObject.Vector2D;
import tools.GameDrawer;

import java.awt.*;

public interface Drawable {
    void draw(GameDrawer gameDrawer);
    void tick(long deltaTime);
    Vector2D interpolatedPosition = null;
}
