package controller.elevatorSystemController;

import connector.protocol.Protocol;
import connector.protocol.ProtocolMessage;
import controller.Controller;
import model.Model;
import model.objects.MovingObject.Creature;
import model.objects.MovingObject.Vector2D;
import model.objects.building.Building;
import model.objects.custumer.Customer;
import model.objects.elevator.Elevator;
import model.objects.elevator.ElevatorRequest;
import model.objects.elevator.ElevatorState;
import tools.Timer;

import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ElevatorSystemController {
    private final Model MODEL;
    private final Controller CONTROLLER;
    public final ElevatorSystemSettings SETTINGS = new ElevatorSystemSettings();

    LinkedList<ElevatorRequest> pending = new LinkedList<>();

    private final Timer timer = new Timer();

    private final Building DEFAULT_BUILDING = new Building(SETTINGS);

    public ElevatorSystemController(Controller controller) {
        CONTROLLER = controller;
        this.MODEL = CONTROLLER.MODEL;
        MODEL.Initialize(DEFAULT_BUILDING);
    }


    public void tick(long deltaTime) {
        pending.removeIf(this::tryToCallElevator);
        for (var elevator : MODEL.getBuilding().getELEVATORS()) {
            timer.tick(deltaTime);
            switch (elevator.getState()) {
                case WAIT -> processWait(elevator);
                case IN_MOTION -> processInMotion(elevator);
                case OPENING, CLOSING -> processOpeningClosing(elevator);
                case OPENED -> processOpened(elevator);
            }
            elevator.tick(deltaTime);
        }
    }


    private void processOpened(Elevator elevator) {
        if (timer.isReady()) {
            elevator.setState(ElevatorState.CLOSING);
            timer.restart(SETTINGS.ELEVATOR_OPEN_CLOSE_TIME);
        }
    }

    private void processOpeningClosing(Elevator elevator) {
        CONTROLLER.server.Send(new ProtocolMessage(Protocol.ELEVATOR_OPEN_CLOSE, elevator.getId()));
        if (timer.isReady()) {
            if (elevator.getState() == ElevatorState.CLOSING) {
                elevator.setState(ElevatorState.WAIT);
                timer.restart(SETTINGS.ELEVATOR_AFTER_CLOSE_AFK_TIME);
            }
            if (elevator.getState() == ElevatorState.OPENED) {
                elevator.setState(ElevatorState.OPENED);
                elevator.arrived();
                timer.restart(SETTINGS.ELEVATOR_WAIT_AS_OPENED_TIME);
            }

        }
    }

    private void processInMotion(Elevator elevator) {
        if (elevator.isReachedDestination()) {
            elevator.setState(ElevatorState.OPENING);
            timer.restart(SETTINGS.ELEVATOR_OPEN_CLOSE_TIME);
        }
    }

    private void processWait(Elevator elevator) {
        if (!timer.isReady()) {
            return;
        }
        int bestFloor = elevator.findBestFloor();
        if (bestFloor != elevator.UNEXIST_FLOOR) {
            elevator.setFloorDestination(bestFloor);
            elevator.setState(ElevatorState.IN_MOTION);
        }
    }

    public void buttonClick(ElevatorRequest request) {
        CONTROLLER.server.Send(new ProtocolMessage(
                Protocol.ELEVATOR_BUTTON_CLICK, request.button_position()));
        if (!tryToCallElevator(request)) {
            pending.add(request);
        }
    }

    private boolean tryToCallElevator(ElevatorRequest request) {
        // closest, free, and go the same way / or wait
        LinkedList<Elevator> elevators_available = Stream
                .of(MODEL.getBuilding().getELEVATORS())
                .filter(Elevator::isAvailable)
                .collect(Collectors.toCollection(LinkedList::new));

        if (elevators_available.size() == 0) {
            return false;
        }
        Elevator closest_elevator = elevators_available.stream()
                .reduce(
                        null,
                        (elevatorA, elevatorB) -> this.NearestElevator(request, elevatorA, elevatorB)
                );

        var requestFloor = (int) (request.button_position().y / DEFAULT_BUILDING.WALL_SIZE);
        System.out.println(requestFloor);
        closest_elevator.addFloorToPickUp(requestFloor);
        return true;
    }

    private Elevator NearestElevator(ElevatorRequest request, Elevator elevatorA, Elevator elevatorB) {
        if (elevatorA == null) {
            return elevatorB;
        }
        if (elevatorB == null) {
            return elevatorA;
        }
        if (request.button_position().getNearest(elevatorA.getPosition(), elevatorB.getPosition())
                .equals(elevatorA.getPosition())) {
            return elevatorA;
        }
        return elevatorB;
    }

    public void getIntoElevator(Elevator nearestOpenedElevator) {
        nearestOpenedElevator.put();
    }

    public void getOutFromElevator(Elevator currentElevator) {
        currentElevator.remove();
    }
}
