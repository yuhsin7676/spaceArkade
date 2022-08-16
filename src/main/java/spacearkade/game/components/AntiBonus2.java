package spacearkade.game.components;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import spacearkade.engine.Component;
import spacearkade.engine.Event;

public class AntiBonus2 extends Bonus {

    public AntiBonus2() {
        this.className = "AntiBonus2";
    }

    @Override
    public void eventIntersectionListener(Event eventIntersection) {
        if(eventIntersection.className.equals("Platform")){
            if(this.balls.size() < 3)
                this.world.addComponent(new Ball()).setLocation(this.getLocation()).setVelocity(50, 90);
            if(this.balls.size() < 3)
                this.world.addComponent(new Ball()).setLocation(this.getLocation()).setVelocity(-50, 90);
        }
    }
      
}

    
