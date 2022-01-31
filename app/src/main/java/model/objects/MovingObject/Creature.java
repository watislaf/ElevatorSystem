package model.objects.MovingObject;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.Serializable;

/*
 * Basic object of all objects in project
 */
@Getter
public class Creature implements Serializable {
    private static Integer next_id = 0;

    @Setter
    protected boolean isVisible = true;
    protected Point size = new Point();
    protected Vector2D position;
    protected long id;

    public Creature(Creature creatureA) {
        this.position = new Vector2D(creatureA.position);
        this.size = new Point(creatureA.size);
        this.isVisible = creatureA.isVisible;
        this.id = creatureA.id;
    }

    public Creature(Vector2D position) {
        this.position = position;
        id = next_id++;
    }

    public Creature(Vector2D position, Point size) {
        this.position = position;
        this.size = size;
        id = next_id++;
    }

    public void set(Creature creature) {
        this.position = creature.position;
        this.isVisible = creature.isVisible;
        this.size = creature.size;
    }
}
