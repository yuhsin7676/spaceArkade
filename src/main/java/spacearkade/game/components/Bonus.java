package spacearkade.game.components;

import java.util.Map;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import spacearkade.engine.Component;
import spacearkade.engine.EnableRelation;
import spacearkade.engine.Event;
import spacearkade.game.ArkadeWorld;

public class Bonus extends Component {

    public Map<Integer, Component> balls;
    
    public Bonus() {
        this.isStaticComponent = false;
        this.velocity = new Vector2D(0, 50);
        this.className = "Bonus";
        this.enableRelation = EnableRelation.ONLY_INTERSECTION;
        this.isCircle = true;
        this.r = 15;
    }
    
    @Override
    public void eventIntersectionListener(Event eventIntersection) {
        if(eventIntersection.className.equals("Platform") || eventIntersection.className.equals("World"))
            this.removed();
    }
    
    public Bonus setComponentsPointer(Map<Integer, Component> balls){
        this.balls = balls;
        return this;
    }
      
}

    
