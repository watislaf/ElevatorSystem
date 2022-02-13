package drawable;

import model.objects.movingObject.Vector2D;
import view.GameDrawer;

public interface Drawable {
    void draw(GameDrawer gameDrawer);
    void tick(long deltaTime);
    Vector2D interpolatedPosition = null;
}
