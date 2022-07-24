package spacearkade.game;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public abstract class Object {
    
    public Vector2D location;
    public Vector2D size = new Vector2D(10, 10);
    public double r = 10.;
    public boolean isCircle = false;
    public Vector2D velocity = new Vector2D(0., 0.);

    public Object(Vector2D location, int id) {
        this.location = location;
    }

    public Vector2D getLocation() {
        return location;
    }

    public void setLocation(Vector2D location) {
        this.location = location;
    }
    
    public void update(double deltaSeconds){
    }
    
}
