package spacearkade.game.components;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import spacearkade.engine.DynamicComponent;
import spacearkade.engine.Event;
import spacearkade.game.ArkadeWorld;

public class Ball extends DynamicComponent {

    public ArkadeWorld arkadeWorld;
    
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
            this.arkadeWorld.addSound("impactPlatform");
        }
        else if(eventHit.className.equals("World")){
            this.arkadeWorld.addSound("impactWorld");
            if(this.location.getY() <= this.r/2 + this.r || this.location.getY() >= 600 - this.r/2 - this.r)
                this.removed();
        }
    }
    
    public Ball setWorldPointer(ArkadeWorld arkadeWorld){
        this.arkadeWorld = arkadeWorld;
        return this;
    }

    @Override
    public void update() {
        
        if(this.velocity.getNorm() < 225)
            this.velocity = this.velocity.scalarMultiply(1.002);
        else if(this.velocity.getNorm() > 225)
           this.velocity = this.velocity.scalarMultiply(0.998);
        
    }
    
    
      
}