package controller;

import connector.ConnectionSettings;
import connector.OnReceive;
import view.SwingWindow;
import connector.Client;

import java.util.concurrent.TimeUnit;

public class Controller implements OnReceive {

    @Override
    public void onReceive(String message) {
        System.out.println(message);
    }

    public void start() throws InterruptedException {
        Client client = new Client(ConnectionSettings.HOST, this);
        SwingWindow gui = new SwingWindow();
        gui.startWindow();

        while (true) {
            gui.repaint();
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
