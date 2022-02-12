package model.objects.custumer;

import model.objects.MovingObject.MovingObject;
import model.objects.MovingObject.Vector2D;
import model.objects.elevator.Elevator;
import lombok.Setter;
import lombok.Getter;
import tools.Timer;

import java.awt.Point;


@Getter
public class Customer extends MovingObject {
    private final Timer TIMER = new Timer();
    private final int FLOOR_TO_END;

    @Setter
    private CustomerState state = CustomerState.GO_TO_BUTTON;
    @Setter
    private int currentFlor;

    private Elevator currentElevator;
    private double lastCurrentElevatorXPosition;

    public Customer(int currentFlor, int floorEnd, Vector2D position, double speed, Point size) {
        super(position, speed, size);
        this.FLOOR_TO_END = floorEnd;
        this.currentFlor = currentFlor;
    }

    @Override
    public void tick(long deltaTime) {
        super.tick(deltaTime);
        TIMER.tick(deltaTime);
        if (currentElevator != null) {
            position.y = currentElevator.getPosition().y;
            if (lastCurrentElevatorXPosition != currentElevator.getPosition().x) {
                position.x -= (lastCurrentElevatorXPosition - currentElevator.getPosition().x);
                lastCurrentElevatorXPosition = currentElevator.getPosition().x;
            }
            if (currentElevator.isInMotion()) {
                setCurrentFlor(currentElevator.getCurrentFloor());
                setVisible(false);
            }
            if (!currentElevator.isVisible()) {
                isDead = true;
            }
            return;
        }

        setVisible(true);
    }

    public void setCurrentElevator(Elevator currentElevator) {
        this.currentElevator = currentElevator;
        if (currentElevator != null) {
            lastCurrentElevatorXPosition = currentElevator.getPosition().x;
        }
    }

    public boolean wantsGoUp() {
        return currentFlor < FLOOR_TO_END;
    }
}
