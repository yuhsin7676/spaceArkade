package spacearkade.game;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class StaticComponent extends Object{
    
    public StaticComponent(Vector2D location, int id) {
        super(location, id);
        this.className = "Scene";
        this.typeName = "Scene";
    }
    
}
