package spacearkade.game.components;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import spacearkade.engine.StaticComponent;

public class Tile extends StaticComponent{
    
    public int health = 1;
    
    public Tile(){
        this.className = "Tile";
        this.isCircle = false;
        this.size = new Vector2D(50, 20);
    }

    @Override
    public void eventHitListener() {
        this.health -= 1;
        if(this.health <= 0)
            this.canBeRemove = true;
    }
      
}
