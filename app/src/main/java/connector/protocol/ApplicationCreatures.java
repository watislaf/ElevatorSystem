package connector.protocol;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.objects.MovingObject.Creature;
import model.objects.custumer.Customer;
import model.objects.elevator.Elevator;

import java.io.Serializable;
import java.util.LinkedList;

@NoArgsConstructor
@Getter
@Setter
public class ApplicationCreatures implements Serializable {
    public ApplicationCreatures(LinkedList<Customer> customers, LinkedList<Elevator> elevators) {
        this.customers = new LinkedList<>();
        this.elevators = new LinkedList<>();

        customers.forEach(customer -> this.customers.add(new Creature(customer)));
        elevators.forEach(elevator -> this.elevators.add(new Creature(elevator)));
    }

    @Getter
    private LinkedList<Creature> customers = new LinkedList<>();
    @Getter
    private LinkedList<Creature> elevators = new LinkedList<>();
}
