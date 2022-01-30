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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class Controller implements OnSocketEvent {
    static private final int TPS = 40;

    public final ElevatorSystemController ELEVATOR_SYSTEM_CONTROLLER;
    private final CustomersController CUSTOMER_CONTROLLER;

    public final Model MODEL;
    private long currentTime;
    @Setter
    private Server server;
    private LinkedList<ProtocolMessage> messages = new LinkedList<>();

    @Override
    public void onReceive(ProtocolMessage message) {
        synchronized (messages) {
            messages.add(message);
        }
    }

    @Override
    public void onNewConnection(DataClient client) {
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
        currentTime = System.currentTimeMillis();
        Timer timer = new Timer();
        server.start();
        while (true) {
            TimeUnit.MILLISECONDS.sleep(Math.round(1000. / TPS));
            long deltaTime = System.currentTimeMillis() - currentTime;

            timer.tick(deltaTime);
            currentTime += deltaTime;

            if (timer.isReady()) {
                timer.restart(Math.round(1000. / ConnectionSettings.SSPS));
                server.Send(new ProtocolMessage(Protocol.UPDATE_DATA, MODEL.getDataToSent(), currentTime));
            }

            synchronized(messages) {
                messages.forEach(this::processMessage);
                messages.clear();
            }
            CUSTOMER_CONTROLLER.tick(deltaTime);
            ELEVATOR_SYSTEM_CONTROLLER.tick(deltaTime);
            MODEL.clearDead();
        }
    }

    private void processMessage(ProtocolMessage protocolMessage) {
        switch (protocolMessage.protocol()) {
            case CREATE_CUSTOMER -> {
                if(protocolMessage.data() instanceof LinkedList ) {
                    LinkedList<Integer> floors = (LinkedList<Integer>) protocolMessage.data();
                    CUSTOMER_CONTROLLER.CreateCustomer(floors.get(1), floors.get(0));
                }

            }
        }
    }

    public void Send(Protocol protocol, Serializable data) {
        server.Send(new ProtocolMessage(protocol, data, currentTime));
    }
}
