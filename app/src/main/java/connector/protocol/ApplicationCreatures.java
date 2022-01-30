package connector.protocol;

import lombok.Data;
import model.objects.MovingObject.Creature;
import model.objects.custumer.Customer;
import model.objects.elevator.Elevator;

import java.io.Serializable;
import java.util.LinkedList;


/**
 * Contains all data about creatures to send.
 * <p>
 * This data is sending SSPS times at second
 *
 * @see connector.ConnectionSettings
 * </p>
 */

public class ApplicationCreatures implements Serializable {
    public final LinkedList<Creature> CUSTOMERS = new LinkedList<>();
    public final LinkedList<Creature> ELEVATORS = new LinkedList<>();

    public ApplicationCreatures(LinkedList<Customer> customers, LinkedList<Elevator> elevators) {
        customers.forEach(customer -> this.CUSTOMERS.add(new Creature(customer)));
        elevators.forEach(elevator -> this.ELEVATORS.add(new Creature(elevator)));
    }
}
