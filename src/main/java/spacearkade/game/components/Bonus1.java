package spacearkade.game.components;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import spacearkade.engine.Component;
import spacearkade.engine.Event;

public class Bonus1 extends Bonus {

    public Bonus1() {
        this.className = "Bonus1";
    }

    @Override
    public void eventIntersectionListener(Event eventIntersection) {
        if(eventIntersection.className.equals("Platform")){
            Component platform = this.world.getComponent(eventIntersection.id);
            if(platform.getSize().getX() < 120)
                platform.setSize(platform.getSize().add(new Vector2D(20, 0)));
        }
                       
        super.eventIntersectionListener(eventIntersection);
    }
    
    
      
}

    
