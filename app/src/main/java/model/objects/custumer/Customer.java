package model.objects.custumer;

import model.objects.MovingObject.MovingObject;

import java.awt.*;
import java.util.Random;

import lombok.Getter;
import model.objects.MovingObject.Vector2D;


public class Customer extends MovingObject {
    @Getter
    private int currentFlor;
    private int floorEnd;

    public void setState(CustomerState state) {
        if (state.equals(state)) {
            return;
        }
        switch (state) {
            case GO_TO_BUTTON, GET_OUT, GET_IN -> {
                setReachedDestination(false);
            }

        }
        this.state = state;
    }

    @Getter
    private CustomerState state;


    public Customer(int currentFlor, int floorEnd, Vector2D position, double speed ,Point size) {
        super(position, speed, size);
        this.currentFlor = currentFlor;
        this.floorEnd = floorEnd;
    }

    public boolean wantsGoUp() {
        return currentFlor < floorEnd;
    }
}
