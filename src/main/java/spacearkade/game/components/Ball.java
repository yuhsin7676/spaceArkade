package spacearkade.game.components;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import spacearkade.engine.DynamicComponent;

public class Ball extends DynamicComponent {

    public Ball() {
        this.className = "Ball";
    }    
    
    @Override
    public void eventHitListener() {
        for(int i = 0; i < this.eventHit.size(); i++){
            if(this.eventHit.get(i).className.equals("Platform")){
                Vector2D platformLocation = this.eventHit.get(i).location;
                Vector2D platformSize = this.eventHit.get(i).size;
                double vx = 90 * (this.location.getX() - platformLocation.getX())/(platformSize.getX()/2);
                this.velocity = new Vector2D(vx, this.velocity.getY());
            }
            else if(this.eventHit.get(i).className.equals("World")){
                if(this.location.getY() <= 3 + this.r || this.location.getY() >= 597 - this.r)
                    this.removed();
            }
        }

    }
      
}