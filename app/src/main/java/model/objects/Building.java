package model.objects;

import java.awt.*;
import java.lang.reflect.Field;

public class Building {
    private final int FLOORS_COUNT;
    private final int ELEVATORS_COUNT;
    private Point floor_size;


    public Building(BuildingBuilder builder) {
        this.FLOORS_COUNT = builder.FLOORS_COUNT;
        this.ELEVATORS_COUNT = builder.ELEVATORS_COUNT;
        this.floor_size = builder.floor_size;
    }

    public Object cloneObj() {
        try {
            Object clone = this.getClass().newInstance();
            for (Field field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                field.set(clone, field.get(this));
            }
            return clone;
        } catch (Exception e) {
            System.err.println("Cant clone the object");
            return null;
        }
    }
}


