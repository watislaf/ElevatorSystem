package model.objects.custumer;

import lombok.Setter;
import model.objects.MovingObject.MovingObject;

import java.awt.*;
import java.util.Random;

import lombok.Getter;
import model.objects.MovingObject.Vector2D;
import model.objects.elevator.Elevator;


public class Customer extends MovingObject {
    @Getter
    @Setter
    private int currentFlor;
    @Getter
    private int floorEnd;
    @Getter
    @Setter
    private Elevator currentElevator;

    public void setState(CustomerState state) {
        this.state = state;
    }

    @Getter
    private CustomerState state = CustomerState.GO_TO_BUTTON;

    @Override
    public void tick(long deltaTime) {
        if (currentElevator != null) {
            if (currentElevator.isInMotion()) {
                setVisible(false);
                position.y = currentElevator.getPosition().y;
            } else {
                setCurrentFlor(currentElevator.getCurrentFloor());
                setVisible(true);
            }
        }
        super.tick(deltaTime);
    }

    public Customer(int currentFlor, int floorEnd, Vector2D position, double speed, Point size) {
        super(position, speed, size);
        this.currentFlor = currentFlor;
        this.floorEnd = floorEnd;
    }

    public boolean wantsGoUp() {
        return currentFlor < floorEnd;
    }

}
