package model;

import connector.protocol.ApplicationCreatures;
import connector.protocol.ApplicationSettings;
import drawable.Drawable;
import drawable.objects.DrawableButton;
import drawable.objects.DrawableCustomer;
import drawable.objects.DrawableElevator;
import drawable.objects.FlyingText;
import lombok.Getter;
import model.objects.MovingObject.Creature;
import model.objects.MovingObject.MovingObject;
import model.objects.MovingObject.Vector2D;
import model.objects.elevator.Elevator;
import model.objects.elevator.ElevatorRequest;

import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;


public class WindowModel {
    @Getter
    private ApplicationSettings settings;

    LinkedList<DrawableElevator> elevators;
    LinkedList<DrawableCustomer> customers;

    LinkedList<DrawableButton> buttons;
    LinkedList<FlyingText> flyingTexts;

    public void updateData(ApplicationCreatures data) {
        this.apply(data.getElevators(), elevators);
        this.apply(data.getCustomers(), customers);

        // Add
        data.getElevators().forEach(
                creatureA -> {
                    if (elevators.stream()
                            .noneMatch(creatureB -> creatureA.getId() == creatureB.getId())
                    ) {
                        elevators.add(new DrawableElevator(creatureA, settings.ELEVATOR_OPEN_CLOSE_TIME));
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
        drawables.addAll(buttons);

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

    public void setSettings(ApplicationSettings settings) {

        this.settings = settings;
        this.elevators = new LinkedList<>();
        this.customers = new LinkedList<>();

        this.buttons = new LinkedList<>();
        this.flyingTexts = new LinkedList<>();

        var floorHeight = settings.BUILDING_SIZE.y / settings.FLOORS_COUNT;
        double distanceBetweenElevators = ((double) settings.BUILDING_SIZE.x)
                / (settings.ELEVATORS_COUNT + 1);
        for (int i = 0; i < settings.FLOORS_COUNT; i++) {
            for (int j = 0; j < settings.ELEVATORS_COUNT; j++) {
                buttons.add(new DrawableButton(
                        new Vector2D(distanceBetweenElevators * (j + 1) + settings.BUTTON_RELATIVE_POSITION,
                                i * floorHeight + settings.ELEVATOR_SIZE.y / 2.),
                        new Point(4, 4)));
            }
        }
    }

    public DrawableButton getNearestButton(Vector2D data) {
        return buttons.stream()
                .reduce(
                        null,
                        (buttonA, buttonB) -> {
                            if (buttonA == null) {
                                return buttonB;
                            }
                            if (buttonB == null) {
                                return buttonA;
                            }
                            if (data.getNearest(buttonA.getPosition(), buttonB.getPosition())
                                    .equals(buttonA.getPosition())) {
                                return buttonA;
                            }
                            return buttonB;
                        });
    }
}
