package controller;

import connector.ConnectionSettings;
import connector.DataClient;
import connector.OnSocketEvent;
import connector.protocol.ApplicationCreatures;
import connector.protocol.ApplicationSettings;
import connector.protocol.ProtocolMessage;
import lombok.Setter;
import model.ViewModel;
import view.SwingWindow;
import connector.Client;

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
        switch (message.getProtocol()) {
            case APPLICATION_SETTINGS -> {
                ApplicationSettings settings = (ApplicationSettings) message.getData();
                ViewMODEL.setSettings(settings);
            }
            case UPDATE_DATA -> {
                if (ViewMODEL.getSettings() == null) {
                    break;
                }
                 ViewMODEL.updateData((ApplicationCreatures) message.getData());
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
            if(!client.isConnected()){
                client.connect(ConnectionSettings.HOST, this);
            }
            TimeUnit.MILLISECONDS.sleep(Math.round(1000. / TPS));
            long deltaTime = System.currentTimeMillis() - lastTime;
            lastTime += deltaTime;
            gui.repaint();
        }
    }
}
