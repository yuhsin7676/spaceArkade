package spacearkade.game.components;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import spacearkade.engine.Component;
import spacearkade.engine.Event;

public class Tile extends Component{
    
    public int health = 1;
    public String soundBreake = "breakTile";
    public String soundDamage = "damageTile";
    
    public Tile(){
        this.isStaticComponent = true;
        this.infinityMass = true;
        this.className = "Tile";
        this.isCircle = false;
        this.size = new Vector2D(80, 20);
    }
    
    @Override
    public void eventHitListener(Event eventHit) {
        if(eventHit.className.equals("Ball")){
            this.health -= 1;
            if(this.health <= 0){
                this.removed();
                this.world.addSound(this.soundBreake);
            }
            else{
                this.world.addSound(this.soundDamage);
            }
        }
    }
      
}
