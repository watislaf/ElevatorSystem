package connector.protocol;

import controller.customerController.CustomerSettings;
import controller.elevatorSystemController.ElevatorSystemSettings;

import java.awt.*;
import java.io.Serializable;

public class ApplicationSettings implements Serializable {
    public final Point BUILDING_SIZE;
    public final Point ELEVATOR_SIZE;
    public final int FLOORS_COUNT;
    public final double BUTTON_RELATIVE_POSITION;
    public final Point CUSTOMER_SIZE;

    public ApplicationSettings(ElevatorSystemSettings settingsElevator, CustomerSettings settingsCustomer) {
        BUILDING_SIZE = settingsElevator.BUILDING_SIZE;
        ELEVATOR_SIZE = settingsElevator.ELEVATOR_SIZE;
        BUTTON_RELATIVE_POSITION = settingsElevator.BUTTON_RELATIVE_POSITION;
        CUSTOMER_SIZE = settingsCustomer.CUSTOMER_SIZE;
        FLOORS_COUNT = settingsElevator.FLOORS_COUNT;
    }
}
