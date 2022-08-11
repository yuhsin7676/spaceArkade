package spacearkade.game.components;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import spacearkade.engine.DynamicComponent;

public class Platform extends DynamicComponent{
    
    public int health = 1;
    
    public Platform() {
    }

    @Override
    public void eventHitListener() {
        
    }
    
    @Override
    public String getClassName() {
        return "Platform";
    }
    
}
