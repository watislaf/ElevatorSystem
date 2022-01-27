package controller;

import connector.Client;
import connector.ConnectionSettings;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        var client = new Client(ConnectionSettings.HOST, controller);
        controller.setClient(client);

        try {
            controller.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
