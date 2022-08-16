package spacearkade.game.components;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import spacearkade.engine.Component;
import spacearkade.engine.Event;

public class Ball extends Component {
    
    public Ball() {
        this.isStaticComponent = false;
        this.className = "Ball";
    }    
    
    @Override
    public void eventHitListener(Event eventHit) {
        if(eventHit.className.equals("Platform")){
            Vector2D platformLocation = eventHit.location;
            Vector2D platformSize = eventHit.size;
            double vx = 90 * (this.location.getX() - platformLocation.getX())/(platformSize.getX()/2);
            this.velocity = new Vector2D(vx, this.velocity.getY());
            this.world.addSound("impactPlatform");
        }
        else if(eventHit.className.equals("World")){
            this.world.addSound("impactWorld");
            if(this.location.getY() <= this.r/2 + this.r || this.location.getY() >= 600 - this.r/2 - this.r)
                this.removed();
        }
    }

    @Override
    public void update() {
        
        if(this.velocity.getNorm() < 225)
            this.velocity = this.velocity.scalarMultiply(1.002);
        else if(this.velocity.getNorm() > 225)
           this.velocity = this.velocity.scalarMultiply(0.998);
        
    }
    
    
      
}