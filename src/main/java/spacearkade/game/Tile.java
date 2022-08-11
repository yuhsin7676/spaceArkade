package spacearkade.game;

import spacearkade.engine.StaticComponent;

public class Tile extends StaticComponent{
    
    public int health = 1;
    
    public Tile(){
        this.className = "Tile";
    }

    @Override
    public void eventHitListener() {
        this.health -= 1;
        if(this.health <= 0)
            this.canBeRemove = true;
    }
    
    @Override
    public String getClassName() {
        return this.className;
    }
      
}
