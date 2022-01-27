package controller;

import lombok.AllArgsConstructor;
import model.Model;
import model.objects.building.BuildingSettings;
import model.objects.custumer.Customer;
@AllArgsConstructor

public class CustomersController {
    private final Model MODEL;

    public void tick(long delta_time) {
        if (MODEL.getCustomers().isEmpty()) {
            CreateCustomer(0, 4);
        }

        for (var customer : MODEL.getCustomers()) {
            switch (customer.getState()) {
                case GO_TO_BUTTON -> {

                }
                case WAIT_UNTIL_ARRIVED -> {

                }
                case GET_IN -> {

                }
                case STAY_IN -> {

                }
                case GET_OUT -> {

                }
            }
            customer.tick(delta_time);
        }
    }

    private void CreateCustomer(int floor_start, int floor_end) {
        Customer customer = new Customer(
                floor_start, floor_end,
                MODEL.getBuilding().getStartPosition(floor_start),
                BuildingSettings.CUSTOMER_SIZE);
        MODEL.getCustomers().add(customer);
    }
}
