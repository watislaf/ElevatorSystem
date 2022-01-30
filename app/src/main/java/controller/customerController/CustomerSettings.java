package controller.customerController;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.awt.*;
import java.io.Serializable;


@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class CustomerSettings implements Serializable {
    public final double CUSTOMER_SPEED = 100;
    public final Point CUSTOMER_SIZE = new Point(30, 60);
}
