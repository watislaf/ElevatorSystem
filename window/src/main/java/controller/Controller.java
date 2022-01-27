package controller;

import connector.DataClient;
import connector.OnSocketEvent;
import connector.protocol.ProtocolMessage;
import lombok.Setter;
import view.SwingWindow;
import connector.Client;

import java.util.concurrent.TimeUnit;

public class Controller implements OnSocketEvent {
    @Setter
    Client client;

    @Override
    public void onReceive(ProtocolMessage message) {
        System.out.println("RESIEVED");
    }

    @Override
    public void onNewConnection(DataClient message) {
        System.out.println("connecterd");
    }

    public void start() throws InterruptedException {
        SwingWindow gui = new SwingWindow();
        gui.startWindow();

        while (true) {
            gui.repaint();
            TimeUnit.SECONDS.sleep(1);
        }
    }

}
