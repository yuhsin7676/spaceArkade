package spacearkade.game.components;

import java.util.Map;
import spacearkade.engine.Component;
import spacearkade.engine.Event;

public class Bonus4 extends Bonus {

    public Bonus4() {
        this.className = "Bonus4";
    }
      
    @Override
    public void eventIntersectionListener(Event eventIntersection) {
        if(eventIntersection.className.equals("Platform")){
            for(Map.Entry<Integer, Component> entry : this.balls.entrySet())
                if(entry.getValue().getRadius() < 14)
                    entry.getValue().setRadius(entry.getValue().getRadius() + 2);
        }
                       
        super.eventIntersectionListener(eventIntersection);
    }
    
}

    
