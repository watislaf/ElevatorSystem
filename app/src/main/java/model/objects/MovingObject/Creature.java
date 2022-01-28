package model.objects.MovingObject;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;
import java.io.Serializable;

public class Creature implements Serializable {
    static Integer next_id = 4;

    @Getter
    long id;

    @Getter
    protected Vector2D position;

    @Getter
    protected Point size;

    public Creature(Creature creatureA) {
        this.id = creatureA.id;
        this.position = creatureA.position;
        this.size = creatureA.size;
    }

    public Creature(Vector2D position, Point size) {
        id = next_id++;
        this.position = position;
        this.size = size;
    }

    public void set(Creature creature) {
        this.position = creature.position;
        this.size = creature.size;
    }
}
