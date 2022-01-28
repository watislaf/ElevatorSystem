package model;

import connector.protocol.ApplicationCreatures;
import connector.protocol.ApplicationSettings;
import drawable.Drawable;
import drawable.objects.DrawableCustomer;
import drawable.objects.DrawableElevator;
import lombok.Getter;
import lombok.Setter;
import model.objects.MovingObject.Creature;

import java.util.Arrays;
import java.util.LinkedList;


public class ViewModel {
    @Getter
    @Setter
    private ApplicationSettings settings;

    LinkedList<Creature> elevators = new LinkedList<>();
    LinkedList<Creature> customers = new LinkedList<>();

    public void updateData(ApplicationCreatures data) {
        this.apply(data.getElevators(), elevators);
        this.apply(data.getCustomers(), customers);
    }

    private void apply(Creature[] creatures_came, LinkedList<Creature> creatures_to_apply) {
        // erease
        creatures_to_apply.removeIf(
                creatureA -> Arrays.stream(creatures_came).anyMatch(
                        creatureB -> creatureA.getId() == creatureB.getId()));
        // update
        creatures_to_apply.forEach(
                creatureA -> {
                    creatureA.set(Arrays.stream(creatures_came).filter(
                            creatureB -> creatureA.getId() == creatureB.getId()
                    ).findFirst().get());
                }
        );
        // Add
        Arrays.stream(creatures_came).forEach(
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

    public LinkedList<Drawable> getDrawable() {
        LinkedList<Drawable> drawables = new LinkedList<>();
        elevators.forEach(creature -> drawables.add(new DrawableElevator(creature)));
        customers.forEach(creature -> drawables.add(new DrawableCustomer(creature)));
        return drawables;
    }
}
