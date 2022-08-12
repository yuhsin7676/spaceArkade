package spacearkade.game.components;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import spacearkade.engine.DynamicComponent;
import spacearkade.engine.Event;

public class Ball extends DynamicComponent {

    public Ball() {
        this.className = "Ball";
    }    
    
    @Override
    public void eventHitListener(Event eventHit) {
        if(eventHit.className.equals("Platform")){
            Vector2D platformLocation = eventHit.location;
            Vector2D platformSize = eventHit.size;
            double vx = 90 * (this.location.getX() - platformLocation.getX())/(platformSize.getX()/2);
            this.velocity = new Vector2D(vx, this.velocity.getY());
        }
        else if(eventHit.className.equals("World")){
            if(this.location.getY() <= 3 + this.r || this.location.getY() >= 597 - this.r)
                this.removed();
        }
    }

    @Override
    public void update() {
        
        if(this.velocity.getY() < 150)
            this.velocity = this.velocity.add(new Vector2D(0, 0.02));
        else if(this.velocity.getY() > 150)
           this.velocity = this.velocity.subtract(new Vector2D(0, 0.02));
        
    }
    
    
      
}