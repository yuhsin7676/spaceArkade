package spacearkade.game;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Tile extends StaticComponent{
    
    public int health = 1;
    
    public Tile() {
    }

    @Override
    public void eventHitListener() {
        this.health -= 1;
        if(this.health <= 0)
            this.canBeRemove = true;
    }
    
    
    
}
