package controller;

import connector.clientServer.Server;
import model.Model;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        Server server = new Server(controller);
        controller.setServer(server);
        controller.start();
    }
}
