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

import java.util.concurrent.TimeUnit;

public class WindowController implements OnSocketEvent {
    @Setter
    Client client;
    private final WindowModel windowMODEL;
    SwingWindow gui;
    static private final int TPS = 25;

    public WindowController(WindowModel windowModel) {
        windowMODEL = windowModel;
        ColorSettings colorSettings = new ColorSettings();
        windowMODEL.setColorSettings(colorSettings);
    }

    @Override
    public void onReceive(ProtocolMessage message) {
        if (windowMODEL.getSettings() == null && message.getProtocol() != Protocol.APPLICATION_SETTINGS) {
            return;
        }
        switch (message.getProtocol()) {
            case APPLICATION_SETTINGS -> {
                ApplicationSettings settings = (ApplicationSettings) message.getData();
                windowMODEL.setSettings(settings);
            }
            case OK -> {
            }
            case UPDATE_DATA -> {
                windowMODEL.updateData((ApplicationCreatures) message.getData());
            }
            case ELEVATOR_BUTTON_CLICK -> {
                windowMODEL.addMovingDrawable(
                        new FlyingText("Click", ((Vector2D) message.getData())
                                .add(new Vector2D(0, windowMODEL.getSettings().CUSTOMER_SIZE.y)),
                                new Vector2D(0, 100), 15, 30., 1500,
                                windowMODEL.getColorSettings().TEXT_COLOR));
                windowMODEL.getNearestButton((Vector2D) message.getData()).buttonClick();
            }
            case ELEVATOR_OPEN_CLOSE -> {
                windowMODEL.getElevator((long) message.getData()).getDoors().changeDoorsState();
            }
            case CUSTOMER_GET_IN_OUT -> {
                windowMODEL.getCustomer((long) message.getData()).changeBehindElevator();
            }
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

            long deltaTime = System.currentTimeMillis() - lastTime;
            lastTime += deltaTime;

            if (windowMODEL.isInitialised()) {
                windowMODEL.getDrawableOjects().forEach(object -> object.tick(deltaTime));
                windowMODEL.clearDead();
            }
            gui.repaint();
        }
    }
}
