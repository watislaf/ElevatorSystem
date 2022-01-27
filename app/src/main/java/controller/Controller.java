package controller;

import connector.OnReceive;
import model.Model;
import model.objects.building.Building;

import java.util.concurrent.TimeUnit;

public class Controller implements OnReceive {
    static private final int TPS = 300;
    private final Building DEFAULT_BUILDING = new Building(new ElevatorSystemSettings(), 5, 4);

    private final ElevatorSystemController ELEVATOR_SYSTEM_CONTROLLER;
    private final CustomersController CUSTUMER_CONTROLLER;
    private final Model MODEL;

    public Controller(Model model) {
        this.MODEL = model;
        ELEVATOR_SYSTEM_CONTROLLER = new ElevatorSystemController(model);
        CUSTUMER_CONTROLLER = new CustomersController(model, ELEVATOR_SYSTEM_CONTROLLER);

        model.Initialize(DEFAULT_BUILDING);
    }

    @Override
    public void onReceive(String message) {
        System.out.println(message);
    }

    public void start() throws InterruptedException {
        long lastTime = System.currentTimeMillis();
        while (true) {
            TimeUnit.MILLISECONDS.sleep(Math.round(1000. / TPS));
            long deltaTime = System.currentTimeMillis() - lastTime;
            lastTime += deltaTime;

            CUSTUMER_CONTROLLER.tick(deltaTime);
            ELEVATOR_SYSTEM_CONTROLLER.tick(deltaTime);
        }
    }
}
