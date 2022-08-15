package spacearkade.game.components;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import spacearkade.engine.Event;
import spacearkade.engine.StaticComponent;
import spacearkade.game.ArkadeWorld;

public class Tile extends StaticComponent{
    
    public ArkadeWorld arkadeWorld;
    public int health = 1;
    public String soundBreake = "breakTile";
    public String soundDamage = "damageTile";
    
    public Tile(){
        this.className = "Tile";
        this.isCircle = false;
        this.size = new Vector2D(50, 20);
    }

    public Tile setWorldPointer(ArkadeWorld arkadeWorld){
        this.arkadeWorld = arkadeWorld;
        return this;
    }
    
    @Override
    public void eventHitListener(Event eventHit) {
        if(eventHit.className.equals("Ball")){
            this.health -= 1;
            if(this.health <= 0){
                this.removed();
                this.arkadeWorld.addSound(this.soundBreake);
            }
            else{
                this.arkadeWorld.addSound(this.soundDamage);
            }
        }
    }
      
}
