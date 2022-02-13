package model.objects.customer;

import tools.Timer;

public class CustomerStatisticCreator {
    public final CustomerWaitStatistic STATISTIC = new CustomerWaitStatistic();

    private final Timer ELEVATOR_WAIT_TIMER = new Timer();
    private boolean buttonWasClicked = false;

    public void tick(long deltaTime) {
        ELEVATOR_WAIT_TIMER.tick(deltaTime);
    }

    public void buttonClicked() {
        if (!buttonWasClicked) {
            buttonWasClicked = true;
            ELEVATOR_WAIT_TIMER.restart(0);
        }
    }

    public void getInToElevator() {
        STATISTIC.setWaitToGetInTime(-ELEVATOR_WAIT_TIMER.getCurrentTimer());
        ELEVATOR_WAIT_TIMER.restart(0);
    }

    public void getOutOfElevator() {
        STATISTIC.setWaitToGetOutTime(-ELEVATOR_WAIT_TIMER.getCurrentTimer());
    }
}
