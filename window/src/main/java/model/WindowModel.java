package model;

import connector.protocol.ApplicationCreatures;
import connector.protocol.ApplicationSettings;
import drawable.Drawable;
import drawable.objects.DrawableCustomer;
import drawable.objects.DrawableElevator;
import drawable.objects.FlyingText;
import lombok.Getter;
import lombok.Setter;
import model.objects.MovingObject.Creature;
import model.objects.MovingObject.MovingObject;
import model.objects.custumer.Customer;

import java.util.Collection;
import java.util.LinkedList;


public class WindowModel {
    @Getter
    @Setter
    private ApplicationSettings settings;

    LinkedList<DrawableElevator> elevators = new LinkedList<>();
    LinkedList<DrawableCustomer> customers = new LinkedList<>();


    LinkedList<FlyingText> flyingTexts = new LinkedList<>();

    public void updateData(ApplicationCreatures data) {
        LinkedList<Customer> elevatorCreatures =        LinkedList<Customer> ( elevators);
        LinkedList<Customer>  customerCreatures =           LinkedList<Customer>(customers);
        this.apply(data.getElevators(), elevatorCreatures);


        elevators = new LinkedList<>();
        elevatorCreatures.forEach(elevator -> {
            try {
                elevators.add((DrawableElevator) elevator);
                System.out.println("Added old");
            } catch (java.lang.ClassCastException e) {
                elevators.add(new DrawableElevator(elevator));
            }
            elevators.getLast().getDoors().setOpenCloseDoorsTime(settings.getElevatorOpenCloseTime());
        });


        this.apply(data.getCustomers(), customerCreatures);
        customers = new LinkedList<>();
        customerCreatures.forEach(customer -> {
            try {
                customers.add((DrawableCustomer) customer);
            } catch (java.lang.ClassCastException e) {
                customers.add(new DrawableCustomer(customer));
            }
            ;
        });

    }

    private void apply(LinkedList<Creature> creatures_came, LinkedList<Creature> creatures_to_apply) {
        // erease
        creatures_to_apply.removeIf(
                creatureA -> creatures_came.stream().anyMatch(
                        creatureB -> creatureA.getId() == creatureB.getId()));
        // update
        creatures_to_apply.forEach(
                creatureA -> {
                    creatureA.set(creatures_came.stream().filter(
                            creatureB -> creatureA.getId() == creatureB.getId()
                    ).findFirst().get());
                }
        );
        // Add
        creatures_came.forEach(
                creatureA -> {
                    if (creatures_to_apply.stream()
                            .noneMatch(
                                    creatureB -> creatureA.getId() == creatureB.getId())
                    ) {
                        creatures_to_apply.add(new Creature(creatureA));
                    }
                }
        );
    }

    public LinkedList<Drawable> getDrawableOjects() {
        if (elevators.size() != 0) {
//            System.out.println(elevators.get(0));
        }
        LinkedList<Drawable> drawables = new LinkedList<>();
        drawables.addAll(elevators);
        drawables.addAll(customers);

        drawables.addAll(getElevatorDoors());
        drawables.addAll(flyingTexts);
        return drawables;
    }

    private Collection<Drawable> getElevatorDoors() {
        LinkedList<Drawable> elevatorDoors = new LinkedList<>();
        elevators.forEach(elevator -> elevatorDoors.add(elevator.getDoors()));
        return elevatorDoors;
    }

    public void addMovingDrawable(FlyingText click) {
        flyingTexts.add(click);
    }

    public void clearDead() {
        flyingTexts.removeIf(MovingObject::isDead);
    }

    public DrawableElevator getElevator(long id) {
        return elevators.stream().filter(elevator -> elevator.getId() == id).findFirst().get();
    }

}
