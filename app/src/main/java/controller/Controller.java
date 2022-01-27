package controller;

import connector.OnReceive;
import model.Model;
import model.objects.building.Building;
import model.objects.building.BuildingBuilder;

import java.util.concurrent.TimeUnit;

public class Controller implements OnReceive {
    static private final int TPS = 300;
    private final Building DEFAULT_BUILDING = new BuildingBuilder(5, 4).build();

    private final ElevatorSystemController ELEVATOR_SYSTEM_CONTROLLER;
    private final CustomersController CUSTUMER_CONTROLLER;
    private final Model MODEL;

    public Controller(Model model) {
        this.MODEL = model;
        CUSTUMER_CONTROLLER = new CustomersController(model);
        ELEVATOR_SYSTEM_CONTROLLER = new ElevatorSystemController(model);

        model.Initialize(DEFAULT_BUILDING);
    }

    @Override
    public void onReceive(String message) {
        System.out.println(message);
    }

    public void start() throws InterruptedException {
        long last_time = System.currentTimeMillis();
        while (true) {
            TimeUnit.MILLISECONDS.sleep(Math.round(1000. / TPS));
            long delta_time = System.currentTimeMillis() - last_time;
            last_time += delta_time;

            CUSTUMER_CONTROLLER.tick(delta_time);
            ELEVATOR_SYSTEM_CONTROLLER.tick(delta_time);
        }
    }
}
