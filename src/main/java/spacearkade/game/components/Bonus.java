package spacearkade.game.components;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import spacearkade.engine.DynamicComponent;
import spacearkade.engine.EnableRelation;

public class Bonus extends DynamicComponent {

    public Bonus() {
        this.velocity = new Vector2D(0, 50);
        this.className = "Bonus";
        this.enableRelation = EnableRelation.ONLY_INTERSECTION;
        this.isCircle = true;
        this.r = 15;
    }
    
    @Override
    public void eventIntersectionListener() {
        for(int i = 0; i < this.eventIntersection.size(); i++)
            if(this.eventIntersection.get(i).className.equals("Platform") || this.eventIntersection.get(i).className.equals("World"))
                this.removed();
    }
      
}

    
