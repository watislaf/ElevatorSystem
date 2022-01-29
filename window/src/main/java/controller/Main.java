package controller;

import connector.Client;
import model.WindowModel;

public class Main {
    public static void main(String[] args) {
        WindowModel windowModel = new WindowModel();
        WindowController controller = new WindowController(windowModel);
        var client = new Client();
        controller.setClient(client);

        try {
            controller.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
