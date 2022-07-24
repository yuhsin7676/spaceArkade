package spacearkade.game;

import java.util.Map;
import java.util.HashMap;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class World {
    
    private int width;
    private int height;
    private int primary = 0;
    public boolean player1 = false;
    public boolean player2 = false;
    public Map<Integer, Object> objects = new HashMap<Integer, Object>();
    public double deltaSeconds = 1;

    public World(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public int createNewObject(Object object){
        objects.put(this.primary, object);
        int worldId = this.primary;
        this.primary++;
        return worldId;
    }
    
    public int addPlayer(Player player){
        if(this.player1 == false){
            player.playerNumber = 1;
            this.player1 = true;
            return 1;
        }
        else if(this.player2 == false){
            player.playerNumber = 2;
            this.player2 = true;
            return 2;
        }
        else{
            return 0;
        }
    }
    
    public void removePlayer1(){
        player1 = false;
    }
    
    public void removePlayer2(){
        player2 = false;
    }
    
    public int deleteObject(int id){
        objects.remove(id);
        return primary;
    }
    
    public void update(){
        for(Map.Entry<Integer, Object> entry : objects.entrySet()){
            relationshipWorld(entry.getValue());
            entry.getValue().update(deltaSeconds);
        }
    }
    
    
    void relationshipWorld(Object dynamicCombonent){
			
        double cx = dynamicCombonent.location.getX();
        double cy = dynamicCombonent.location.getY();
        double vx = dynamicCombonent.velocity.getX() * deltaSeconds;
        double vy = dynamicCombonent.velocity.getY() * deltaSeconds;
        double w,h;
        if(dynamicCombonent.isCircle) w = h = 2*dynamicCombonent.r;
        else{
            w = dynamicCombonent.size.getX();
            h = dynamicCombonent.size.getY();
        }

        if (cx + vx < w/2 ){
            dynamicCombonent.velocity = dynamicCombonent.velocity.add(new Vector2D( -2 * dynamicCombonent.velocity.getX(), 0.));
        }
        else if (cx + vx > this.width - w/2){
            dynamicCombonent.velocity = dynamicCombonent.velocity.add(new Vector2D( -2 * dynamicCombonent.velocity.getX(), 0.));
        }

        if (cy + vy < h/2){
            dynamicCombonent.velocity = dynamicCombonent.velocity.add(new Vector2D( 0., -2 * dynamicCombonent.velocity.getY()));
        }
        else if (cy + vy > this.height - h/2){
            dynamicCombonent.velocity = dynamicCombonent.velocity.add(new Vector2D( 0., -2 * dynamicCombonent.velocity.getY()));
        }
        
}
    
}
