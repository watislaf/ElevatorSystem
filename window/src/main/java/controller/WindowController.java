package controller;

import connector.clientServer.SocketEventListener;
import connector.clientServer.SocketCompactData;
import drawable.drawableObjects.FlyingText;
import model.objects.MovingObject.Vector2D;
import connector.protocol.ProtocolMessage;
import connector.protocol.CreaturesData;
import connector.protocol.SettingsData;
import connector.clientServer.Client;
import connector.protocol.Protocol;
import model.WindowModel;
import view.SwingWindow;
import lombok.Setter;

import java.util.concurrent.TimeUnit;
import java.util.LinkedList;

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

    public WindowController(WindowModel windowModel) {
        WINDOW_MODEL = windowModel;
        GUI = new SwingWindow();
    }

    @Override
    public void onReceiveSocket(ProtocolMessage message) {
        if (WINDOW_MODEL.getSettings() == null && message.protocol() != Protocol.APPLICATION_SETTINGS) {
            return;
        }
        synchronized (MESSAGE) {
            MESSAGE.add(message);
        }
    }

    @Override
    public void onNewSocketConnection(SocketCompactData message) {
        System.out.println("Connected");
    }

    public void start() throws InterruptedException {
        GUI.startWindow(WINDOW_MODEL, this);
        long lastTime = System.currentTimeMillis();

        while (true) {
            clientConnectReadWrite();

            long deltaTime = System.currentTimeMillis() - lastTime;
            lastTime += deltaTime;
            currentTime += deltaTime;

            if (WINDOW_MODEL.isInitialised()) {
                if (!GUI.resized()) {
                    GUI.updateButtonsAndSliders(WINDOW_MODEL);
                }
                WINDOW_MODEL.getDrawableOjects().forEach(object -> object.tick(deltaTime));
                WINDOW_MODEL.clearDead();
                GUI.updateButtonsAndSliders(WINDOW_MODEL);
            }
            GUI.repaint();
            TimeUnit.MILLISECONDS.sleep(Math.round(1000. / TPS));
        }
    }

    private void clientConnectReadWrite() throws InterruptedException {
        client.reconect();
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
                WINDOW_MODEL.setSettings(settings);
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
            case CUSTOMER_GET_IN_OUT -> WINDOW_MODEL.getCustomer((long) message.data()).changeBehindElevator();

        }
        return true;
    }

    public void clickedButtonWithNumber(int newButtonCLickedNumber) {
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
}
