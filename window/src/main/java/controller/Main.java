package controller;

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
