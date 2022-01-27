package controller;

import connector.Server;
import model.Model;

public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model);
        Server server = new Server(controller);
        controller.setServer(server);
        try {
            controller.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
