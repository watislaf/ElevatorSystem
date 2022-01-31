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
    private Elevator currentElevator;
    @Setter
    private int currentFlor;

    public Customer(int currentFlor, int floorEnd, Vector2D position, double speed, Point size) {
        super(position, speed, size);
        this.FLOOR_TO_END = floorEnd;
        this.currentFlor = currentFlor;
    }

    @Override
    public void tick(long deltaTime) {
        TIMER.tick(deltaTime);
        super.tick(deltaTime);

        if (currentElevator != null && currentElevator.isInMotion()) {
            position.y = currentElevator.getPosition().y;
            setCurrentFlor(currentElevator.getCurrentFloor());
            setVisible(false);
            return;
        }
        setVisible(true);
    }

    public boolean wantsGoUp() {
        return currentFlor < FLOOR_TO_END;
    }
}
