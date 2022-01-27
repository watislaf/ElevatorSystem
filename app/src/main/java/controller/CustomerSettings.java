package controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.awt.*;


@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class CustomerSettings {
    public final double CUSTOMER_SPEED = 10;
    public final Point CUSTOMER_SIZE = new Point(10, 10);
}
