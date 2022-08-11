package spacearkade.game.components;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import spacearkade.engine.DynamicComponent;

public class Bonus extends DynamicComponent {

    public Bonus() {
        this.velocity = new Vector2D(0, 50);
        this.className = "Bonus";
        this.enableCollision = false;
        this.isCircle = true;
        this.r = 15;
    }
     
    @Override
    public String getClassName() {
        return "Bonus";
    }
    
      
}

    
