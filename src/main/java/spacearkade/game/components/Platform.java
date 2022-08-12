package spacearkade.game.components;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import spacearkade.engine.DynamicComponent;

public class Platform extends DynamicComponent{
    
    public int health = 1;
    
    public Platform() {
        this.className = "Platform";
        this.size = new Vector2D(100, 20);
        this.isCircle = false;
        this.infinityMass = true;
        this.velocity = new Vector2D(0, 0);
    }
    
}
