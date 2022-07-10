package spacearkade.game;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public abstract class Object {
    
    private Vector2D location;
    private int id;

    public Object(Vector2D location, int id) {
        this.location = location;
    }

    public Vector2D getLocation() {
        return location;
    }

    public void setLocation(Vector2D location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
}
