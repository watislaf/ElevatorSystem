package controller;

import connector.Client;
import view.SwingWindow;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        try {
            controller.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
