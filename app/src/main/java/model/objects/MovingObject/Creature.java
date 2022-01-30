package model.objects.MovingObject;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.Serializable;

public class Creature implements Serializable {
    static Integer next_id = 0;

    @Getter
    long id;

    @Getter
    protected Vector2D position;
    @Getter
    @Setter
    boolean isVisible = true;

    @Getter
    protected Point size = new Point();


    public Creature(Creature creatureA) {
        this.id = creatureA.id;
        this.position = new Vector2D(creatureA.position);
        this.size = new Point(creatureA.size);
        this.isVisible = creatureA.isVisible;
    }

    public Creature(Vector2D position) {
        id = next_id++;
        this.position = position;
    }

    public Creature(Vector2D position, Point size) {
        id = next_id++;
        this.position = position;
        this.size = size;
    }

    public void set(Creature creature) {
        this.position = creature.position;
        this.size = creature.size;
        this.isVisible = creature.isVisible;
    }


}
