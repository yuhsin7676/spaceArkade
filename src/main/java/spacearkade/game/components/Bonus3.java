package spacearkade.game.components;

import java.util.Map;
import spacearkade.engine.Component;
import spacearkade.engine.Event;

public class Bonus3 extends Bonus {

    public Bonus3() {
        this.className = "Bonus3";
    }
    
    @Override
    public void eventIntersectionListener(Event eventIntersection) {
        if(eventIntersection.className.equals("Platform")){
            for(Map.Entry<Integer, Component> entry : this.balls.entrySet())
                if(entry.getValue().getVelocity().getY() >= 120)
                    entry.getValue().setVelocity(entry.getValue().getVelocity().scalarMultiply(0.8));
        }
                       
        super.eventIntersectionListener(eventIntersection);
    }
      
}

    
