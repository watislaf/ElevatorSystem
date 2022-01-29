package controller;

import connector.DataClient;
import connector.OnSocketEvent;
import connector.Server;
import connector.protocol.ApplicationSettings;
import connector.protocol.Protocol;
import connector.protocol.ProtocolMessage;
import controller.customerController.CustomersController;
import controller.elevatorSystemController.ElevatorSystemController;
import lombok.Getter;
import lombok.Setter;
import model.Model;
import tools.Timer;

import java.util.concurrent.TimeUnit;

public class Controller implements OnSocketEvent {
    static private final int TPS = 50;

    public final ElevatorSystemController ELEVATOR_SYSTEM_CONTROLLER;
    private final CustomersController CUSTOMER_CONTROLLER;

    public final Model MODEL;

    @Setter
    public Server server;

    @Override
    public void onReceive(ProtocolMessage message) {
        System.out.println("RECIEVED");
    }

    @Override
    public void onNewConnection(DataClient client) {
        System.out.println("CONNECTED");
        server.Send(client,
                new ProtocolMessage(
                        Protocol.APPLICATION_SETTINGS,
                        new ApplicationSettings(
                                ELEVATOR_SYSTEM_CONTROLLER.SETTINGS, CUSTOMER_CONTROLLER.SETTINGS)));
    }

    public Controller(Model model) {
        this.MODEL = model;
        ELEVATOR_SYSTEM_CONTROLLER = new ElevatorSystemController(this);
        CUSTOMER_CONTROLLER = new CustomersController(this);
    }


    public void start() throws InterruptedException {
        long lastTime = System.currentTimeMillis();
        Timer timer = new Timer();
        while (true) {
            TimeUnit.MILLISECONDS.sleep(Math.round(1000. / TPS));
            long deltaTime = System.currentTimeMillis() - lastTime;

            timer.tick(deltaTime);
            if (timer.isReady()) {
                timer.restart(Math.round(1000. / TPS) * 50);
                server.Send(new ProtocolMessage(Protocol.UPDATE_DATA, MODEL.getDataToSent()));
            }
            lastTime += deltaTime;

            CUSTOMER_CONTROLLER.tick(deltaTime);
            ELEVATOR_SYSTEM_CONTROLLER.tick(deltaTime);
        }
    }

}
