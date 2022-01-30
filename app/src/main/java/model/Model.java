package model;

import connector.protocol.ApplicationCreatures;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.objects.MovingObject.Creature;
import model.objects.MovingObject.MovingObject;
import model.objects.building.Building;
import model.objects.custumer.Customer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Model {
    private LinkedList<Customer> customers;
    private Building building;

    public void Initialize(Building building) {
        this.building = building;
        this.customers = new LinkedList<>();
    }

    public void clearDead() {
        customers.removeIf(MovingObject::isDead);
    }

    public ApplicationCreatures getDataToSent() {
        return new ApplicationCreatures(customers, building.ELEVATORS);
    }
}
