package connector.protocol;

import controller.customerController.CustomerSettings;
import controller.elevatorSystemController.ElevatorSystemSettings;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.*;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class ApplicationSettings implements Serializable {
    public Point buildingSize;
    public Point elevatorSize;
    public int floorsCount;
    public double buttonRelativePosition;
    public Point customerSize;
    public long elevatorOpenCloseTime;

    public ApplicationSettings(ElevatorSystemSettings settingsElevator, CustomerSettings settingsCustomer) {
        buildingSize = settingsElevator.BUILDING_SIZE;
        elevatorSize = settingsElevator.ELEVATOR_SIZE;
        buttonRelativePosition = settingsElevator.BUTTON_RELATIVE_POSITION;
        customerSize = settingsCustomer.CUSTOMER_SIZE;
        floorsCount = settingsElevator.FLOORS_COUNT;
        elevatorOpenCloseTime = settingsElevator.ELEVATOR_OPEN_CLOSE_TIME;
    }
}
