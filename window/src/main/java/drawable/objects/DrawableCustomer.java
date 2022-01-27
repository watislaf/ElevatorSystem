package drawable.objects;

import drawable.Drawable;

import model.objects.custumer.Customer;

import java.awt.*;

public class DrawableCustomer extends Customer implements Drawable {

    public DrawableCustomer(Point position) {
        super(position);
    }

    @Override
    public void Draw(Graphics g) {
        System.out.println("draw");
    }
}
