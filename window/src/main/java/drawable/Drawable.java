package drawable;

import tools.GameDrawer;

import java.awt.*;

public interface Drawable {
    void draw(GameDrawer gameDrawer);
    void tick(long deltaTime);
}
