package controller;

import connector.OnReceive;
import connector.Server;
import model.Model;
import model.objects.Building;
import model.objects.BuildingBuilder;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Controller implements OnReceive {
    static private final int TPS = 300;
    private final Building DEFAULT_BUILDING = new BuildingBuilder(5, 4).build();

    private ElevatorSystemController elevatorSystemController;
    private Model model;

    public Controller(Model model) {
        this.model = model;
        model.Initialize(DEFAULT_BUILDING);
        elevatorSystemController = new ElevatorSystemController(model);
    }

    @Override
    public void onReceive(String message) {
        System.out.println(message);
    }

    public void start() throws InterruptedException {
        while (true) {
            TimeUnit.MILLISECONDS.sleep(Math.round(1000. / TPS));
        }
    }
}
