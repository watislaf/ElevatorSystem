package controller;

import connector.ConnectionSettings;
import connector.DataClient;
import connector.OnSocketEvent;
import connector.protocol.ApplicationCreatures;
import connector.protocol.ApplicationSettings;
import connector.protocol.Protocol;
import connector.protocol.ProtocolMessage;
import drawable.objects.FlyingText;
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
    static private final int TPS = 55;

    public WindowController(WindowModel windowModel) {
        windowMODEL = windowModel;
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
            case UPDATE_DATA -> {
                windowMODEL.updateData((ApplicationCreatures) message.getData());
            }
            case ELEVATOR_BUTTON_CLICK -> {
                windowMODEL.addMovingDrawable(new FlyingText("Click", (Vector2D) message.getData(),
                        new Vector2D(0, 100), 15, 30., 1500));
            }
            case ELEVATOR_OPEN_CLOSE -> {
                System.out.println((long) message.getData());
                windowMODEL.getElevator((long) message.getData()).getDoors().changeDoorsState();
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

            windowMODEL.getDrawableOjects().forEach(object -> object.tick(deltaTime));
            windowMODEL.clearDead();


            gui.repaint();

        }
    }
}
