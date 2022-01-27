package model.objects.custumer;

import model.objects.MovingObject;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

import lombok.Getter;

public class Customer extends MovingObject {
    private final int FLOOR_START;
    private final int FLOOR_END;

    @Getter
    private CustomerState state = CustomerState.GO_TO_BUTTON;


    public Customer(int floor_start, int floor_end, Point2D.Double position, Point size) {
        super(position, new Random()
                .doubles(5., 10.)
                .findAny()
                .getAsDouble(), size);

        this.FLOOR_END = floor_end;
        this.FLOOR_START = floor_start;
    }

}
