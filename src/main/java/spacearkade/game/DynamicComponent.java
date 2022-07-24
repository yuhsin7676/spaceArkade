package spacearkade.game;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class DynamicComponent extends Object{
    
    //public Vector2D velocity = new Vector2D(1.0, 2.0);
    //public Vector2D size = new Vector2D(10, 10);
    
    public DynamicComponent(Vector2D location, int id) {
        super(location, id);
        this.velocity = new Vector2D(1.0, 2.0);
    }

    @Override
    public void update(double deltaSeconds) {
        this.location = this.location.add(deltaSeconds, this.velocity);
    } 
    
}
