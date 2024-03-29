package model.objects.customer;

import model.objects.movingObject.MovingObject;
import model.objects.movingObject.Vector2D;
import model.objects.elevator.Elevator;
import lombok.Setter;
import lombok.Getter;
import tools.Timer;

import java.awt.Point;


@Getter
public class Customer extends MovingObject {
    public Timer MAIN_TIMER = new Timer();
    private final int FLOOR_TO_END;
    @Setter
    private int currentFlor;
    private Elevator currentElevator;
    private double lastCurrentElevatorXPosition;
    private CustomerState state = CustomerState.GO_TO_BUTTON;
    private final CustomerStatisticCreator WAIT_STATISTIC = new CustomerStatisticCreator();

    public Customer(int currentFlor, int floorEnd, Vector2D position, double speed, Point size) {
        super(position, speed, size);
        this.FLOOR_TO_END = floorEnd;
        this.currentFlor = currentFlor;
    }

    @Override
    public void tick(long deltaTime) {
        super.tick(deltaTime);
        MAIN_TIMER.tick(deltaTime);
        WAIT_STATISTIC.tick(deltaTime);
        if (currentElevator != null) {
            position.y = currentElevator.getPosition().y;
            if (lastCurrentElevatorXPosition != currentElevator.getPosition().x) {
                position.x -= (lastCurrentElevatorXPosition - currentElevator.getPosition().x);
                lastCurrentElevatorXPosition = currentElevator.getPosition().x;
            }
            if (currentElevator.isInMotion()) {
                setCurrentFlor(currentElevator.getCurrentFloor());
                setVisible(false);
            } else {
                setVisible(true);
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

    public void setState(CustomerState state) {
        this.state = state;
        if (state == CustomerState.WAIT_UNTIL_ARRIVED) {
            WAIT_STATISTIC.buttonClicked();
        }
        if (state == CustomerState.STAY_IN) {
            WAIT_STATISTIC.getInToElevator();
        }
        if (state == CustomerState.GET_OUT) {
            WAIT_STATISTIC.getOutOfElevator();
        }
    }

    public CustomerWaitStatistic getStatistic() {
        return WAIT_STATISTIC.STATISTIC;
    }
}
