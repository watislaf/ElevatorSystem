package connector.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import model.objects.MovingObject.Creature;
import model.objects.custumer.Customer;
import model.objects.elevator.Elevator;

import java.io.Serializable;
import java.util.List;

public class ApplicationCreatures implements Serializable {
    public ApplicationCreatures(Creature[] customers, Creature[] elevators) {
        this.customers = customers;
        this.elevators = elevators;
    }

    @Getter
    private Creature[] customers;
    @Getter
    private Creature[] elevators;
}
