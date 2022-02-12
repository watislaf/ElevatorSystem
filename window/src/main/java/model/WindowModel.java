package model;

import connector.protocol.CreaturesData;
import connector.protocol.SettingsData;
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
import java.util.concurrent.atomic.AtomicReference;


public class WindowModel {
    public final ColorSettings COLOR_SETTINGS = new ColorSettings();

    @Getter
    private SettingsData settings;
    @Getter
    @Setter
    private long lastServerRespondTime = 0;

    private LinkedList<DrawableElevator> elevators = new LinkedList<>();
    private LinkedList<DrawableCustomer> customers = new LinkedList<>();

    private LinkedList<Button> buttons;
    private LinkedList<ElevatorBorder> border;
    private LinkedList<BlackSpace> blackSpaces;
    private LinkedList<FlyingText> flyingTexts = new LinkedList<>();
    private LinkedList<HidingWall> hidingWall;
    private boolean needToInitialize = true;

    public void updateData(CreaturesData data) {
        this.applyArryvedData(data.ELEVATORS, elevators);
        this.applyArryvedData(data.CUSTOMERS, customers);

        // Add
        data.ELEVATORS.forEach(
                creatureA -> {
                    if (elevators.stream()
                            .noneMatch(creatureB -> creatureA.getId() == creatureB.getId())) {
                        elevators.add(
                                new DrawableElevator(creatureA, settings.ELEVATOR_OPEN_CLOSE_TIME,
                                        COLOR_SETTINGS.ELEVATOR_BACKGROUND_COLOR, COLOR_SETTINGS.DOORS_COLOR,
                                        COLOR_SETTINGS.BORDER_COLOR));
                    }
                }
        );

        data.CUSTOMERS.forEach(
                creatureA -> {
                    if (customers.stream()
                            .noneMatch(creatureB -> creatureA.getId() == creatureB.getId())) {
                        customers.add(new DrawableCustomer(creatureA,
                                COLOR_SETTINGS.CUSTOMER_SKIN_COLOR));
                    }
                }
        );
        if (isNeedToInitialise()) {
            initialiseFirstData();
        }
    }

    private void initialiseFirstData() {
        var wallSize = settings.BUILDING_SIZE.y / settings.FLOORS_COUNT;
        double distanceBetweenElevators = ((double) settings.BUILDING_SIZE.x)
                / (settings.ELEVATORS_COUNT + 1);
        for (int i = 0; i < settings.FLOORS_COUNT; i++) {
            hidingWall.add(new HidingWall(
                    new Vector2D(settings.BUILDING_SIZE.x / 2., wallSize * i + settings.ELEVATOR_SIZE.y),
                    new Point(settings.BUILDING_SIZE.x, (wallSize - settings.ELEVATOR_SIZE.y)),
                    COLOR_SETTINGS.WALL_COLOR
            ));
            for (int j = 0; j < settings.ELEVATORS_COUNT; j++) {
                buttons.add(new Button(new Vector2D(
                        distanceBetweenElevators * (j + 1) + settings.BUTTON_RELATIVE_POSITION,
                        i * wallSize + settings.CUSTOMER_SIZE.y + 4),
                        new Point(5, 5),
                        COLOR_SETTINGS.BUTTON_ON_COLOR, COLOR_SETTINGS.BUTTON_OF_COLOR));

                border.add(new ElevatorBorder(
                        new Vector2D(distanceBetweenElevators * (j + 1), i * wallSize),
                        elevators.get(j),
                        wallSize, COLOR_SETTINGS.BORDER_COLOR, COLOR_SETTINGS.NUMBER_COLOR));

                blackSpaces.add(new BlackSpace(
                        new Vector2D(distanceBetweenElevators * (j + 1), i * wallSize),
                        elevators.get(j), COLOR_SETTINGS.BLACK_SPACE_COLOR, border.get(0).BORDER_SIZE));
            }
        }
        needToInitialize = false;
    }

    private <T extends Creature> void applyArryvedData(LinkedList<Creature> creatures_came, LinkedList<T> creatures_to_apply) {
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
        elevators.forEach(elevator -> elevatorDoors.add(elevator.DOORS));
        return elevatorDoors;
    }

    public void addMovingDrawable(FlyingText click) {
        flyingTexts.add(click);
    }

    public void clearDead() {
        flyingTexts.removeIf(MovingObject::isDead);
    }

    public DrawableElevator getElevator(long id) {
        AtomicReference<DrawableElevator> tmp = new AtomicReference<>();
        elevators.stream().filter(elevator -> elevator.getId() == id).findFirst().ifPresent(tmp::set);
        return tmp.get();
    }


    public void setSettings(SettingsData settings) {
        this.settings = settings;
        this.buttons = new LinkedList<>();
        this.blackSpaces = new LinkedList<>();
        this.border = new LinkedList<>();
        this.hidingWall = new LinkedList<>();
        needToInitialize = true;
    }

    public Button getNearestButton(Vector2D data) {
        return buttons.stream()
                .reduce(null, (buttonA, buttonB) -> {
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

    public void changeBehindElevatorForCustomer(long id) {
        customers.stream().filter(drawableCustomer -> drawableCustomer.getId() == id).findFirst().ifPresent(
                DrawableCustomer::changeBehindElevator);
    }

    public boolean isNeedToInitialise() {
        return needToInitialize;
    }

    public void clear() {
        border = null;
    }
}
