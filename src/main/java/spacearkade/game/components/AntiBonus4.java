package spacearkade.game.components;

import java.util.Map;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import spacearkade.engine.Component;
import spacearkade.engine.Event;

public class AntiBonus4 extends Bonus {

    public AntiBonus4() {
        this.className = "AntiBonus4";
    }

    @Override
    public void eventIntersectionListener(Event eventIntersection) {
        if(eventIntersection.className.equals("Platform"))
            for(Map.Entry<Integer, Component> entry : this.balls.entrySet())
                if(entry.getValue().getRadius() > 8)
                    entry.getValue().setRadius(entry.getValue().getRadius() - 2);
                       
        super.eventIntersectionListener(eventIntersection);
    }
    
    
      
}

    
