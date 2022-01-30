package drawable.drawableObjects;

import drawable.Drawable;
import model.objects.MovingObject.Creature;
import model.objects.MovingObject.Vector2D;
import tools.GameDrawer;

import java.awt.*;

public class HidingWall extends Creature implements Drawable{
        Color color ;
        public HidingWall(Vector2D position, Point size, Color backGroundColor) {
            super(position,size);
            color = backGroundColor;
        }


        @Override
        public void draw(GameDrawer gameDrawer) {
            gameDrawer.setColor(color);

            gameDrawer.fillRect(this);
        }

        public void tick(long delta_time) {
        }

    }



