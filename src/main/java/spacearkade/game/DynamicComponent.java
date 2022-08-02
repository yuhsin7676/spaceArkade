package spacearkade.game;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class DynamicComponent extends Object{
    
    //public Vector2D velocity = new Vector2D(1.0, 2.0);
    //public Vector2D size = new Vector2D(10, 10);
    
    
    public DynamicComponent(Vector2D location, int id) {
        super(location, id);
        this.className = "Actor";
        this.typeName = "Actor";
        this.velocity = new Vector2D(1.0, 2.0);
    }
    
}
