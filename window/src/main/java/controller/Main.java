package controller;

import connector.Client;
import connector.ConnectionSettings;
import model.ViewModel;

public class Main {
    public static void main(String[] args) {
        ViewModel viewModel = new ViewModel();
        Controller controller = new Controller(viewModel);
        var client = new Client();
        controller.setClient(client);

        try {
            controller.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
