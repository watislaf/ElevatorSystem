package model;

import connector.protocol.ApplicationCreatures;
import connector.protocol.ApplicationSettings;
import drawable.Drawable;
import drawable.objects.DrawableCustomer;
import drawable.objects.DrawableElevator;
import drawable.objects.DrawableElevatorDoors;
import drawable.objects.FlyingText;
import lombok.Getter;
import lombok.Setter;
import model.objects.MovingObject.Creature;
import model.objects.MovingObject.MovingObject;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Supplier;


public class WindowModel {
    @Getter
    @Setter
    private ApplicationSettings settings;

    LinkedList<DrawableElevator> elevators = new LinkedList<>();
    LinkedList<DrawableCustomer> customers = new LinkedList<>();


    LinkedList<FlyingText> flyingTexts = new LinkedList<>();

    public void updateData(ApplicationCreatures data) {

        this.apply(data.getElevators(), elevators);
        this.apply(data.getCustomers(), customers);

        // Add
        data.getElevators().forEach(
                creatureA -> {
                    if (elevators.stream()
                            .noneMatch(creatureB -> creatureA.getId() == creatureB.getId())
                    ) {
                        elevators.add(new DrawableElevator(creatureA,settings.getElevatorOpenCloseTime()));
                    }
                }
        );

        data.getCustomers().forEach(
                creatureA -> {
                    if (customers.stream()
                            .noneMatch(creatureB -> creatureA.getId() == creatureB.getId())
                    ) {
                        customers.add(new DrawableCustomer(creatureA));
                    }
                }
        );
    }

    private <T extends Creature> void apply(LinkedList<Creature> creatures_came, LinkedList<T> creatures_to_apply) {
        // erease
        creatures_to_apply.removeIf(
                creatureA -> creatures_came.stream().noneMatch(
                        creatureB -> creatureA.getId() == creatureB.getId()));
        // update
        creatures_to_apply.forEach(
                creatureA -> {
                    creatureA.set(creatures_came.stream().filter(
                            creatureB -> creatureA.getId() == creatureB.getId()
                    ).findFirst().get());
                }
        );
    }

    public LinkedList<Drawable> getDrawableOjects() {
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
