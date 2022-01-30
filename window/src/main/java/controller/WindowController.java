package controller;

import connector.ConnectionSettings;
import connector.DataClient;
import connector.OnSocketEvent;
import connector.protocol.ApplicationCreatures;
import connector.protocol.ApplicationSettings;
import connector.protocol.Protocol;
import connector.protocol.ProtocolMessage;
import drawable.ColorSettings;
import drawable.drawableObjects.FlyingText;
import lombok.Setter;
import model.WindowModel;
import model.objects.MovingObject.Vector2D;
import view.SwingWindow;
import connector.Client;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class WindowController implements OnSocketEvent {
    @Setter
    Client client;
    private final WindowModel windowMODEL;
    private long currentTime;
    SwingWindow gui;
    static private final int TPS = 30;
    LinkedList<ProtocolMessage> messages = new LinkedList<ProtocolMessage>();

    public WindowController(WindowModel windowModel) {
        windowMODEL = windowModel;
        ColorSettings colorSettings = new ColorSettings();
        windowMODEL.setColorSettings(colorSettings);
    }

    @Override
    public void onReceive(ProtocolMessage message) {
        if (windowMODEL.getSettings() == null && message.protocol() != Protocol.APPLICATION_SETTINGS) {
            return;
        }
        synchronized (messages) {
            messages.add(message);
        }
    }

    @Override
    public void onNewConnection(DataClient message) {
        System.out.println("Connected");
    }

    public void start() throws InterruptedException {
        gui = new SwingWindow();
        gui.startWindow(windowMODEL);
        long lastTime = System.currentTimeMillis();

        while (true) {
            if (client.isClosed()) {
                System.out.println("CONNECT");
                client.connect(ConnectionSettings.HOST, this);
            }

            TimeUnit.MILLISECONDS.sleep(Math.round(1000. / TPS));
            updateMessages();
            long deltaTime = System.currentTimeMillis() - lastTime;
            lastTime += deltaTime;
            currentTime += deltaTime;

            if (windowMODEL.isInitialised()) {
                windowMODEL.getDrawableOjects().forEach(object -> object.tick(deltaTime));
                windowMODEL.clearDead();
            }
            gui.repaint();
        }
    }

    private void updateMessages() {
        synchronized (messages) {
            messages.removeIf(this::processMessage);

        }
    }

    private boolean processMessage(ProtocolMessage message) {
        if (message.protocol() != Protocol.APPLICATION_SETTINGS && message.protocol() != Protocol.UPDATE_DATA) {
            if (message.timeStump() + 2 * windowMODEL.getLastServerRespondTime() > currentTime) {
                return false;
            }
        }
        switch (message.protocol()) {
            case APPLICATION_SETTINGS -> {
                ApplicationSettings settings = (ApplicationSettings) message.data();
                windowMODEL.setSettings(settings);
                currentTime = message.timeStump();
            }
            case UPDATE_DATA -> {
                windowMODEL.updateData((ApplicationCreatures) message.data());
                windowMODEL.setLastServerRespondTime(2 * (message.timeStump() - currentTime));
            }
            case ELEVATOR_BUTTON_CLICK -> {
                windowMODEL.addMovingDrawable(
                        new FlyingText("Click", ((Vector2D) message.data())
                                .add(new Vector2D(0, windowMODEL.getSettings().CUSTOMER_SIZE.y)),
                                new Vector2D(0, 100), 15, 30., 1500,
                                windowMODEL.getColorSettings().TEXT_COLOR));
                windowMODEL.getNearestButton((Vector2D) message.data()).buttonClick();
            }
            case ELEVATOR_OPEN -> {
                windowMODEL.getElevator((long) message.data()).getDoors().changeDoorsState(false);
            }
            case ELEVATOR_CLOSE -> {
                windowMODEL.getElevator((long) message.data()).getDoors().changeDoorsState(true);
            }
            case CUSTOMER_GET_IN_OUT -> {
                windowMODEL.getCustomer((long) message.data()).changeBehindElevator();
            }
        }
        return true;
    }
}
