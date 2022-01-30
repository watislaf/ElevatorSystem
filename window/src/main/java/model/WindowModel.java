package model;

import connector.protocol.ApplicationCreatures;
import connector.protocol.ApplicationSettings;
import drawable.ColorSettings;
import drawable.Drawable;
import drawable.drawableObjects.*;
import drawable.drawableObjects.Button;
import lombok.Getter;
import lombok.Setter;
import model.objects.MovingObject.Creature;
import model.objects.MovingObject.MovingObject;
import model.objects.MovingObject.Vector2D;

import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;


public class WindowModel {
    @Setter
    @Getter
    private ColorSettings colorSettings;

    @Getter
    private ApplicationSettings settings;
    LinkedList<DrawableElevator> elevators;
    LinkedList<DrawableCustomer> customers;

    LinkedList<Button> buttons;
    LinkedList<ElevatorBorder> border;
    LinkedList<BlackSpace> blackSpaces;
    LinkedList<FlyingText> flyingTexts;
    LinkedList<HidingWall> hidingWall;
    @Getter
    @Setter
    long lastServerRespondTime = 0;

    public void updateData(ApplicationCreatures data) {
        this.apply(data.getElevators(), elevators);
        this.apply(data.getCustomers(), customers);

        // Add
        data.getElevators().forEach(
                creatureA -> {
                    if (elevators.stream()
                            .noneMatch(creatureB -> creatureA.getId() == creatureB.getId())
                    ) {
                        elevators.add(
                                new DrawableElevator(creatureA, settings.ELEVATOR_OPEN_CLOSE_TIME,
                                        colorSettings.ELEVATOR_BACKGROUND_COLOR, colorSettings.DOORS_COLOR,
                                        colorSettings.BORDER_COLOR));
                    }
                }
        );

        data.getCustomers().forEach(
                creatureA -> {
                    if (customers.stream()
                            .noneMatch(creatureB -> creatureA.getId() == creatureB.getId())
                    ) {
                        customers.add(new DrawableCustomer(creatureA,
                                colorSettings.CUSTOMER_SKIN_COLOR));
                    }
                }
        );
        if (border == null) {
            initialiseFirstData();
        }
    }

    private void initialiseFirstData() {
        this.buttons = new LinkedList<>();
        this.blackSpaces = new LinkedList<>();
        this.border = new LinkedList<>();
        this.hidingWall = new LinkedList<>();
        var wallSize = settings.BUILDING_SIZE.y / settings.FLOORS_COUNT;
        double distanceBetweenElevators = ((double) settings.BUILDING_SIZE.x)
                / (settings.ELEVATORS_COUNT + 1);
        for (int i = 0; i < settings.FLOORS_COUNT; i++) {
            hidingWall.add(new HidingWall(
                    new Vector2D(settings.BUILDING_SIZE.x / 2, wallSize * i + settings.ELEVATOR_SIZE.y),
                    new Point(settings.BUILDING_SIZE.x, (wallSize - settings.ELEVATOR_SIZE.y)),
                    colorSettings.WALL_COLOR
            ));
            for (int j = 0; j < settings.ELEVATORS_COUNT; j++) {
                buttons.add(
                        new Button(
                                new Vector2D(
                                        distanceBetweenElevators * (j + 1) + settings.BUTTON_RELATIVE_POSITION,
                                        i * wallSize + settings.CUSTOMER_SIZE.y + 4),
                                new Point(5, 5),
                                colorSettings.BUTTON_ON_COLOR, colorSettings.BUTTON_OF_COLOR));
                border.add(new ElevatorBorder(
                        new Vector2D(distanceBetweenElevators * (j + 1), i * wallSize),
                        elevators.get(j),
                        wallSize, colorSettings.BORDER_COLOR, colorSettings.NUMBER_COLOR));

                blackSpaces.add(new BlackSpace(
                        new Vector2D(distanceBetweenElevators * (j + 1), i * wallSize),
                        elevators.get(j), colorSettings.BLACK_SPACE_COLOR, border.get(0).BORDER_SIZE));
            }
        }
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
        elevators.forEach(elevator -> elevator.setServerRespondTime(lastServerRespondTime));
        customers.forEach(elevator -> elevator.setServerRespondTime(lastServerRespondTime));
        LinkedList<Drawable> drawables = new LinkedList<>();
        drawables.addAll(blackSpaces);
        drawables.addAll(elevators);

        drawables.addAll(customers.stream().filter(DrawableCustomer::isNotBehindElevator).toList());
        drawables.addAll(getElevatorDoors());
        drawables.addAll(hidingWall);
        drawables.addAll(border);
        drawables.addAll(buttons);
        drawables.addAll(customers.stream().filter(DrawableCustomer::isBehindElevator).toList());
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

        this.flyingTexts = new LinkedList<>();
    }

    public Button getNearestButton(Vector2D data) {
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

    public DrawableCustomer getCustomer(long id) {
        return customers.stream().filter(drawableCustomer -> drawableCustomer.getId() == id).findFirst().get();
    }

    public boolean isInitialised() {
        return border != null;
    }
}
