package drawable.objects;

import drawable.Drawable;

import model.objects.MovingObject.Vector2D;
import model.objects.custumer.Customer;

import java.awt.*;

public class DrawableCustomer extends Customer implements Drawable {
    public DrawableCustomer(int currentFlor, int floorEnd, Vector2D position, double speed, Point size) {
        super(currentFlor, floorEnd, position, speed, size);
    }

//    public DrawableCustomer(Point position) {
//        super(position);
 //   }

    @Override
    public void Draw(Graphics g) {
        System.out.println("draw");
    }
}
