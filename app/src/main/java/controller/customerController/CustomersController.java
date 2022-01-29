package controller.customerController;

import controller.Controller;
import controller.elevatorSystemController.ElevatorSystemController;
import lombok.Getter;
import model.objects.elevator.ElevatorRequest;
import model.objects.MovingObject.Vector2D;
import model.objects.custumer.Customer;
import model.objects.custumer.CustomerState;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

public class CustomersController {


    @Getter
    public final CustomerSettings SETTINGS = new CustomerSettings();
    private final ElevatorSystemController elevatorSystemController;
    private final Controller CONTROLLER;

    public CustomersController(Controller CONTROLLER) {
        this.elevatorSystemController = CONTROLLER.ELEVATOR_SYSTEM_CONTROLLER;
        this.CONTROLLER = CONTROLLER;
    }

    public void tick(long deltaTime) {
        if (CONTROLLER.MODEL.getCustomers().isEmpty()) {
            CreateCustomer(1, 4, SETTINGS.CUSTOMER_SIZE,
                    new Random()
                            .doubles(
                                    SETTINGS.CUSTOMER_SPEED
                                            - SETTINGS.CUSTOMER_SPEED / 4,
                                    SETTINGS.CUSTOMER_SPEED
                                            + SETTINGS.CUSTOMER_SPEED / 4)
                            .findAny()
                            .getAsDouble());
        }

        for (var customer : CONTROLLER.MODEL.getCustomers()) {
            switch (customer.getState()) {
                case GO_TO_BUTTON -> processGotOButton(customer);
                case WAIT_UNTIL_ARRIVED -> processWaitUntillArrived(customer);
                case GET_IN -> processGetIn(customer);
                case STAY_IN -> processStayIn(customer);
                case GET_OUT -> processGetOut(customer);
            }
            customer.tick(deltaTime);
        }
    }

    private void processGetOut(Customer customer) {
        if (!customer.isReachedDestination()) {
            return;
        }
        if (customer.getPosition().x < 0 ||
                customer.getPosition().x > elevatorSystemController.SETTINGS.BUILDING_SIZE.x) {
            customer.setDead(true);
        } else {
            customer.setDestination(getStartPositionFOrmCustomer(customer.getCurrentFlor()));
        }
    }

    private void processStayIn(Customer customer) {
        if (customer.getCurrentElevator().isInMotion()) {
            customer.setVisible(false);
        } else {
            if (!customer.isVisible()) {
                customer.teleportToElevator();
            }
            customer.setVisible(true);
            customer.setCurrentFlor(customer.getCurrentElevator().getCurrentFloor());
        }
        if (customer.getCurrentElevator().isOpened()) {
            if (customer.getCurrentFlor() == customer.getFloorEnd()) {
                customer.setState(CustomerState.GET_OUT);
                elevatorSystemController.getOutFromElevator(customer.getCurrentElevator());
                customer.setDestination(customer.getCurrentElevator().getPosition());
                customer.setCurrentElevator(null);
            }
        }
    }

    private void processGetIn(Customer customer) {
        var nearestOpenedElevator = CONTROLLER.MODEL.getBuilding()
                .getNearestOpenedElevatorOnFloor(customer.getPosition(), customer.getCurrentFlor());
        if (nearestOpenedElevator == null) {
            customer.setState(CustomerState.GO_TO_BUTTON);
            return;
        }
        if (customer.isReachedDestination()) {
            elevatorSystemController.getIntoElevator(nearestOpenedElevator);
            customer.setCurrentElevator(nearestOpenedElevator);
            customer.setDestination(
                    customer.getPosition().add(
                            new Vector2D(new Random()
                                    .doubles(-customer.getSize().x / 2.,
                                            customer.getSize().x / 2.)
                                    .findAny().getAsDouble()
                                    , 0)));
            customer.setState(CustomerState.STAY_IN);
            elevatorSystemController.setFloorToReach(customer.getCurrentElevator(), customer.getFloorEnd());
        }
    }

    private void processWaitUntillArrived(Customer customer) {
        var nearestOpenedElevatorOnFloor = CONTROLLER.MODEL.getBuilding()
                .getNearestOpenedElevatorOnFloor(customer.getPosition(), customer.getCurrentFlor());
        if (nearestOpenedElevatorOnFloor != null) {
            customer.setDestination(nearestOpenedElevatorOnFloor.getPosition());
            customer.setState(CustomerState.GET_IN);
        }
    }

    private void processGotOButton(Customer customer) {
        var buttonPosition = CONTROLLER.MODEL.getBuilding()
                .getNearestButtonOnFloor(customer.getPosition(), customer.getCurrentFlor());
        if (buttonPosition == null) {
//            customer.setState(CustomerState.); // TODO add walking mehanick
        }
        customer.setDestination(buttonPosition);
        if (customer.isReachedDestination()) {
            elevatorSystemController.buttonClick(
                    new ElevatorRequest(customer.getPosition(), customer.wantsGoUp()));
            customer.setState(CustomerState.WAIT_UNTIL_ARRIVED);
        }
    }

    private void CreateCustomer(int floorStart, int floorEnd, Point customer_size, double speed) {
        var startPosition = getStartPositionFOrmCustomer(floorStart);
        var customer = new Customer(
                floorStart, floorEnd, startPosition, speed,
                customer_size);

        customer.setState(CustomerState.GO_TO_BUTTON);

        CONTROLLER.MODEL.getCustomers().add(customer);
    }

    private Vector2D getStartPositionFOrmCustomer(int floorStart) {
        Vector2D start_position = CONTROLLER.MODEL.getBuilding().getStartPositionAfterBuilding(floorStart);
        // So u can't see customer whet it spawns
        if (start_position.x == 0) {
            start_position.x -= SETTINGS.CUSTOMER_SIZE.x / 2.;
        } else {
            start_position.x += SETTINGS.CUSTOMER_SIZE.x / 2.;
        }
        return start_position;
    }
}
