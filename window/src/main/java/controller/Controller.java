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
import model.ViewModel;
import model.objects.MovingObject.Vector2D;
import view.SwingWindow;
import connector.Client;

import javax.swing.text.View;
import java.util.concurrent.TimeUnit;

public class Controller implements OnSocketEvent {
    @Setter
    Client client;
    private final ViewModel ViewMODEL;
    SwingWindow gui;
    static private final int TPS = 55;

    public Controller(ViewModel viewModel) {
        ViewMODEL = viewModel;
    }

    @Override
    public void onReceive(ProtocolMessage message) {
        if (ViewMODEL.getSettings() == null && message.getProtocol() != Protocol.APPLICATION_SETTINGS) {
            return;
        }
        switch (message.getProtocol()) {
            case APPLICATION_SETTINGS -> {
                ApplicationSettings settings = (ApplicationSettings) message.getData();
                ViewMODEL.setSettings(settings);
            }
            case UPDATE_DATA -> {
                ViewMODEL.updateData((ApplicationCreatures) message.getData());
            }
            case ELEVATOR_BUTTON_CLICK -> {
                ViewMODEL.addMovingDrawable(new FlyingText("Click", (Vector2D) message.getData(),
                        new Vector2D(0, 100), 15, 30., 1500));
            }
        }
    }

    @Override
    public void onNewConnection(DataClient message) {
        System.out.println("connecterd");
    }

    public void start() throws InterruptedException {
        gui = new SwingWindow();
        gui.startWindow(ViewMODEL);
        long lastTime = System.currentTimeMillis();

        while (true) {
            if (client.isClosed()) {
                System.out.println("CONNECT");
                client.connect(ConnectionSettings.HOST, this);
            }

            TimeUnit.MILLISECONDS.sleep(Math.round(1000. / TPS));

            long deltaTime = System.currentTimeMillis() - lastTime;
            lastTime += deltaTime;

            ViewMODEL.getMovingObjects().forEach(movingObject -> movingObject.tick(deltaTime));
            ViewMODEL.clearDead();


           gui.repaint();

        }
    }
}
