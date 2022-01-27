package controller;

import lombok.AllArgsConstructor;
import model.Model;
import model.objects.custumer.Customer;
import model.objects.custumer.CustomerState;
import model.objects.elevator.ElevatorRequest;

import java.awt.*;
import java.util.Random;

@AllArgsConstructor
public class CustomersController {
    private final Model MODEL;
    private final ElevatorSystemController elevatorSystemController;

    public void tick(long deltaTime) {
        if (MODEL.getCustomers().isEmpty()) {
            CreateCustomer(0, 4, new CustomerSettings().CUSTOMER_SIZE,
                    new Random()
                            .doubles(5., 10.)
                            .findAny()
                            .getAsDouble());
        }

        for (var customer : MODEL.getCustomers()) {
            switch (customer.getState()) {
                case GO_TO_BUTTON -> {
                    CustomerGoToButton(customer);
                }
                case WAIT_UNTIL_ARRIVED -> {
                    CustomerWaitUntilArrived(customer);
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

    private void CustomerWaitUntilArrived(Customer customer) {

        var elevatorPosition = MODEL.getBuilding()
                .getNearestOpenedElevatorOnFloor(customer.getPosition(), customer.getCurrentFlor());
        if (elevatorPosition != null) {
            customer.setDestination(elevatorPosition);
        }
    }

    private void CustomerGoToButton(Customer customer) {
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
        var customer = new Customer(
                floorStart, floorEnd,
                MODEL.getBuilding().getStartPosition(floorStart), speed,
                customer_size);

        customer.setState(CustomerState.GO_TO_BUTTON);
        MODEL.getCustomers().add(customer);
    }
}
