package controller;

import connector.clientServer.*;
import drawable.drawableObjects.FlyingText;
import model.objects.movingObject.Vector2D;
import connector.protocol.ProtocolMessage;
import connector.protocol.CreaturesData;
import connector.protocol.SettingsData;
import connector.protocol.Protocol;
import model.WindowModel;
import view.SwingWindow;
import lombok.Setter;

import java.util.concurrent.TimeUnit;
import java.util.LinkedList;
import java.util.logging.Logger;

/*
 * control window, created with Swing
 * @see SwingWindow
 */
public class WindowController implements SocketEventListener {
    static private final int TPS = 50;

    private final LinkedList<ProtocolMessage> MESSAGE = new LinkedList<>();
    private final WindowModel WINDOW_MODEL;
    private final SwingWindow GUI;

    @Setter
    private Client client;
    private long currentTime;
    private int lastFloorButtonClickedNumber = -1;
    private double gameSpeed = 1;

    private final Logger LOGGER = Logger.getLogger(WindowController.class.getName());

    public WindowController(WindowModel windowModel) {
        WINDOW_MODEL = windowModel;
        GUI = new SwingWindow();
    }

    @Override
    public void onReceiveSocket(ProtocolMessage message) {
        if (WINDOW_MODEL.getSettings() == null && message.protocol() != Protocol.APPLICATION_SETTINGS) {
            return;
        }
        if (WINDOW_MODEL.isNeedToInitialise() && message.protocol() != Protocol.APPLICATION_SETTINGS
                && message.protocol() != Protocol.UPDATE_DATA) {
            return;
        }
        synchronized (MESSAGE) {
            MESSAGE.add(message);
        }
    }

    @Override
    public void onNewSocketConnection(SocketCompactData message) {
        LOGGER.info("Connected");
    }

    public void start() throws InterruptedException {
        GUI.startWindow(WINDOW_MODEL, this);
        long lastTime = System.currentTimeMillis();

        while (true) {
            clientConnectReadWrite();
            long deltaTime = System.currentTimeMillis() - lastTime;
            lastTime += deltaTime;
            currentTime += deltaTime;

            if (!WINDOW_MODEL.isNeedToInitialise()) {
                if (!GUI.resized()) {
                    GUI.updateButtonsAndSliders(WINDOW_MODEL);
                }
                WINDOW_MODEL.getDrawableOjects().forEach(object -> object.tick((long) (deltaTime * gameSpeed)));
                WINDOW_MODEL.clearDead();
                GUI.updateButtonsAndSliders(WINDOW_MODEL);
            }
            GUI.repaint();
            TimeUnit.MILLISECONDS.sleep(Math.round(1000. / TPS));
        }
    }

    private void clientConnectReadWrite() {
        if (client.isClosed()) {
            WINDOW_MODEL.clear();
            client.reconnect();
        }
        synchronized (MESSAGE) {
            MESSAGE.removeIf(this::processMessage);
        }
    }

    private boolean processMessage(ProtocolMessage message) {
        if (message.protocol() != Protocol.APPLICATION_SETTINGS && message.protocol() != Protocol.UPDATE_DATA) {
            //  if (message.timeStump() + 2 * WINDOW_MODEL.getLastServerRespondTime() > currentTime) {
            //      return false;
            //  }
        }
        switch (message.protocol()) {
            case APPLICATION_SETTINGS -> {
                SettingsData settings = (SettingsData) message.data();
                if (ConnectionSettings.VERSION != settings.VERSION) {
                    LOGGER.warning("You have different versions with sever. Your version: %s, server version %s%n"
                            .formatted(ConnectionSettings.VERSION, settings.VERSION));
                    return true;
                }
                WINDOW_MODEL.setSettings(settings);
                gameSpeed = settings.GAME_SPEED;
                currentTime = message.timeStump();
            }
            case UPDATE_DATA -> {
                WINDOW_MODEL.updateData((CreaturesData) message.data());
                WINDOW_MODEL.setLastServerRespondTime(2 * (message.timeStump() - currentTime));
            }
            case ELEVATOR_BUTTON_CLICK -> {
                WINDOW_MODEL.addMovingDrawable(
                        new FlyingText("Click", ((Vector2D) message.data())
                                .add(new Vector2D(0, WINDOW_MODEL.getSettings().CUSTOMER_SIZE.y)),
                                new Vector2D(0, 100), 15, 30., 1500,
                                WINDOW_MODEL.COLOR_SETTINGS.TEXT_COLOR));
                WINDOW_MODEL.getNearestButton((Vector2D) message.data()).buttonClick();
            }
            case ELEVATOR_OPEN -> WINDOW_MODEL.getElevator((long) message.data()).DOORS.changeDoorsState(false);
            case ELEVATOR_CLOSE -> WINDOW_MODEL.getElevator((long) message.data()).DOORS.changeDoorsState(true);
            case CUSTOMER_GET_IN_OUT -> WINDOW_MODEL.changeBehindElevatorForCustomer((long) message.data());
            case CHANGE_GAME_SPEED -> gameSpeed = (double) message.data();
        }
        return true;
    }

    public void clickedAddCustomerButtonWithNumber(int newButtonCLickedNumber) {
        newButtonCLickedNumber = WINDOW_MODEL.getSettings().FLOORS_COUNT - newButtonCLickedNumber - 1;
        if (lastFloorButtonClickedNumber == -1) {
            lastFloorButtonClickedNumber = newButtonCLickedNumber;
            return;
        }
        LinkedList<Integer> data = new LinkedList<>();
        data.push(lastFloorButtonClickedNumber);
        data.push(newButtonCLickedNumber);

        lastFloorButtonClickedNumber = -1;
        client.send(new ProtocolMessage(Protocol.CREATE_CUSTOMER, data, currentTime));
    }

    public void changeElevatorsCount(boolean isAdding) {
        client.send(new ProtocolMessage(Protocol.CHANGE_ELEVATORS_COUNT, isAdding, currentTime));
    }

    public void increaseSpeed() {
        client.send(new ProtocolMessage(Protocol.CHANGE_GAME_SPEED, 1.5, currentTime));
    }

    public void decreesSpeed() {
        client.send(new ProtocolMessage(Protocol.CHANGE_GAME_SPEED, 1 / 1.5, currentTime));
    }
}
