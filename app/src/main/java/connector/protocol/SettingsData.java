package connector.protocol;

import controller.customerController.CustomerSettings;
import controller.elevatorSystemController.ElevatorSystemSettings;

import java.awt.Point;
import java.io.Serializable;

/**
 * Contains all information about application
 * <p>
 * This object need to be sent for client first, so that client can initialize his database
 * according to this fields.
 * </p>
 */

public class SettingsData implements Serializable {
    public final double BUTTON_RELATIVE_POSITION;
    public final long ELEVATOR_OPEN_CLOSE_TIME;
    public final Point BUILDING_SIZE;
    public final Point ELEVATOR_SIZE;
    public final Point CUSTOMER_SIZE;
    public final int ELEVATORS_COUNT;
    public final int FLOORS_COUNT;

    public SettingsData(ElevatorSystemSettings settingsElevator, CustomerSettings settingsCustomer) {
        BUTTON_RELATIVE_POSITION = settingsElevator.BUTTON_RELATIVE_POSITION;
        ELEVATOR_OPEN_CLOSE_TIME = settingsElevator.ELEVATOR_OPEN_CLOSE_TIME;
        BUILDING_SIZE = settingsElevator.BUILDING_SIZE;
        ELEVATOR_SIZE = settingsElevator.ELEVATOR_SIZE;
        CUSTOMER_SIZE = settingsCustomer.CUSTOMER_SIZE;
        FLOORS_COUNT = settingsElevator.FLOORS_COUNT;
        ELEVATORS_COUNT = settingsElevator.getElevatorsCount();
    }
}
