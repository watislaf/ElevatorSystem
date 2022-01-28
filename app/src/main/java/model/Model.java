package model;

import connector.protocol.ApplicationCreatures;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.objects.MovingObject.Creature;
import model.objects.building.Building;
import model.objects.custumer.Customer;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Model {
    private List<Customer> customers;
    private Building building;

    public void Initialize(Building building) {
        this.building = building;
        this.customers = new LinkedList<>();
    }

    public ApplicationCreatures getDataToSent() {
        Creature[] customers_ = new Creature[customers.size()];
        Creature[] elevators_ = new Creature[building.getElevators().length];
        // slice information
        int i = 0;
        for (var customer_ : customers) {
            customers_[i++] = new Creature(customer_);
        }
        for (i = 0; i < elevators_.length; i++) {
            elevators_[i] = new Creature(building.getElevators()[i]);
        }

        return new ApplicationCreatures(customers_, elevators_);
    }
}
