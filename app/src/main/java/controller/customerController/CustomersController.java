package controller.customerController;

import controller.elevatorSystemController.ElevatorSystemController;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import model.Model;
import model.objects.MovingObject.Vector2D;
import model.objects.custumer.Customer;
import model.objects.custumer.CustomerState;
import model.objects.elevator.ElevatorRequest;

import java.awt.*;
import java.util.Random;

@RequiredArgsConstructor
public class CustomersController {
    private final Model MODEL;

    @Getter
    public final CustomerSettings SETTINGS = new CustomerSettings();
    private final ElevatorSystemController elevatorSystemController;

    public void tick(long deltaTime) {
        if (MODEL.getCustomers().isEmpty()) {
            CreateCustomer(0, 4, SETTINGS.CUSTOMER_SIZE,
                    new Random()
                            .doubles(5., 10.)
                            .findAny()
                            .getAsDouble());
        }

        for (var customer : MODEL.getCustomers()) {
            switch (customer.getState()) {
                case GO_TO_BUTTON -> {
                    processGotOButton(customer);
                }
                case WAIT_UNTIL_ARRIVED -> {
                    processWaitUntillArrived(customer);
                }
                case GET_IN -> {

                }
                case STAY_IN -> {

                }
                case GET_OUT -> {

                }
            }
            customer.tick(deltaTime);
        }
    }

    private void processWaitUntillArrived(Customer customer) {

        var elevatorPosition = MODEL.getBuilding()
                .getNearestOpenedElevatorOnFloor(customer.getPosition(), customer.getCurrentFlor());
        if (elevatorPosition != null) {
            customer.setDestination(elevatorPosition);
        }
    }

    private void processGotOButton(Customer customer) {
        if (!customer.isReachedDestination()) {
            var buttonPosition = MODEL.getBuilding()
                    .getNearestButtonOnFloor(customer.getPosition(), customer.getCurrentFlor());
            customer.setDestination(buttonPosition);
        } else {
            elevatorSystemController.buttonClick(
                    new ElevatorRequest(customer.getCurrentFlor(), customer.wantsGoUp()));
            customer.setState(CustomerState.WAIT_UNTIL_ARRIVED);
        }
    }

    private void CreateCustomer(int floorStart, int floorEnd, Point customer_size, double speed) {
        Vector2D start_position = MODEL.getBuilding().getStartPosition(floorStart);
        // So u can't see customer whet it spawns
        if (start_position.x == 0) {
            start_position.x -= SETTINGS.CUSTOMER_SIZE.x / 2.;
        } else {
            start_position.x += SETTINGS.CUSTOMER_SIZE.x / 2.;
        }
        var customer = new Customer(
                floorStart, floorEnd, start_position, speed,
                customer_size);

        customer.setState(CustomerState.GO_TO_BUTTON);
        MODEL.getCustomers().add(customer);
    }
}
