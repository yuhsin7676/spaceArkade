package spacearkade.game.components;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import spacearkade.engine.Component;
import spacearkade.engine.Event;

public class AntiBonus1 extends Bonus {

    public AntiBonus1() {
        this.className = "AntiBonus1";
    }

    @Override
    public void eventIntersectionListener(Event eventIntersection) {
        if(eventIntersection.className.equals("Platform")){
            Component platform = this.arkadeWorld.components.get(eventIntersection.id);
            if(platform.getSize().getX() > 80)
                platform.setSize(platform.getSize().subtract(new Vector2D(20, 0)));
        }
                       
        super.eventIntersectionListener(eventIntersection);
    }
    
    
      
}

    
