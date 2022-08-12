package spacearkade.game.components;

import java.util.Map;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import spacearkade.engine.Component;
import spacearkade.engine.Event;

public class AntiBonus3 extends Bonus {

    public AntiBonus3() {
        this.className = "AntiBonus3";
    }

    @Override
    public void eventIntersectionListener(Event eventIntersection) {
        if(eventIntersection.className.equals("Platform")){
            for(Map.Entry<Integer, Component> entry : this.balls.entrySet())
                if(entry.getValue().getVelocity().getY() <= 180)
                    entry.getValue().setVelocity(entry.getValue().getVelocity().scalarMultiply(1.25));
        }
                       
        super.eventIntersectionListener(eventIntersection);
    }
    
    
      
}

    
