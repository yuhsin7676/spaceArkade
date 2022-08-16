package spacearkade.game.components;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import spacearkade.engine.Event;

public class Bonus2 extends Bonus {

    public Bonus2() {
        this.className = "Bonus2";
    }
    
    @Override
    public void eventIntersectionListener(Event eventIntersection) {
        
        if(eventIntersection.className.equals("Platform")){
            if(this.balls.size() < 2)
                this.world.addComponentToQueue(new Ball()).setLocation(this.getLocation().subtract(new Vector2D(50, 0))).setVelocity(50, 90);
            if(this.balls.size() < 3)
                this.world.addComponentToQueue(new Ball()).setLocation(this.getLocation().add(new Vector2D(50, 0))).setVelocity(-50, 90);
        }
        
        super.eventIntersectionListener(eventIntersection);

    }
      
}

    
