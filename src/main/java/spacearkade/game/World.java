package spacearkade.game;

import java.util.Map;
import java.util.HashMap;

public class World {
    
    private int width;
    private int height;
    private int primary = 0;
    public Player player1;
    public Player player2;
    private Map<Integer, Object> Objects = new HashMap<Integer, Object>();

    public World(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public int createNewObject(Object object){
        Objects.put(this.primary, object);
        object.setId(this.primary);
        int worldId = this.primary;
        this.primary++;
        return worldId;
    }
    
    public int addPlayer(Player player){
        if(this.player1 == null){
            player.playerNumber = 1;
            this.player1 = player;
            return 1;
        }
        else if(this.player2 == null){
            player.playerNumber = 2;
            this.player2 = player;
            return 2;
        }
        else{
            return 0;
        }
    }
    
    public void removePlayer1(){
        player1 = null;
    }
    
    public void removePlayer2(){
        player2 = null;
    }
    
    public int deleteObject(int id){
        Objects.remove(id);
        return primary;
    }
    
}
