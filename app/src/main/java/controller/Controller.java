package controller;

import connector.ConnectionSettings;
import connector.DataClient;
import connector.OnSocketEvent;
import connector.Server;
import connector.protocol.ApplicationSettings;
import connector.protocol.Protocol;
import connector.protocol.ProtocolMessage;
import controller.customerController.CustomersController;
import controller.elevatorSystemController.ElevatorSystemController;
import lombok.Setter;
import model.Model;
import tools.Timer;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class Controller implements OnSocketEvent {
    static private final int TPS = 25;

    public final ElevatorSystemController ELEVATOR_SYSTEM_CONTROLLER;
    private final CustomersController CUSTOMER_CONTROLLER;

    public final Model MODEL;
    private long currentTime;
    @Setter
    private Server server;

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
                                ELEVATOR_SYSTEM_CONTROLLER.SETTINGS, CUSTOMER_CONTROLLER.SETTINGS),
                        currentTime));
    }

    public Controller(Model model) {
        this.MODEL = model;
        ELEVATOR_SYSTEM_CONTROLLER = new ElevatorSystemController(this);
        CUSTOMER_CONTROLLER = new CustomersController(this);
    }


    public void start() throws InterruptedException {
        long lastTime = System.currentTimeMillis();
        Timer timer = new Timer();
        server.start();
        while (true) {
            TimeUnit.MILLISECONDS.sleep(Math.round(1000. / TPS));
            long deltaTime = System.currentTimeMillis() - lastTime;

            timer.tick(deltaTime);
            lastTime += deltaTime;
            currentTime = lastTime;

            if (timer.isReady()) {
                timer.restart(Math.round(1000. / ConnectionSettings.SSPS));
                server.Send(new ProtocolMessage(Protocol.UPDATE_DATA, MODEL.getDataToSent(), currentTime));
            }
            CUSTOMER_CONTROLLER.tick(deltaTime);
            ELEVATOR_SYSTEM_CONTROLLER.tick(deltaTime);
            MODEL.clearDead();
        }
    }

    public void Send(Protocol protocol, Serializable data) {
        server.Send(new ProtocolMessage(protocol, data, currentTime));
    }
}
