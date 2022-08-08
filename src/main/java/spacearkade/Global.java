package spacearkade;

import spacearkade.game.World;

import java.util.Map;
import java.util.HashMap;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import spacearkade.game.DynamicComponent;
import spacearkade.game.Player;
import spacearkade.game.Tile;

public class Global {
    
    public static Map<Integer, World> mapWorld = new HashMap<Integer, World>();
    public static Map<String, Player> mapPlayer = new HashMap<String, Player>();
    public static int primary = 0;
    
    public static int createWorld(){
        World world = new World(800, 600);
        int worldId = Global.primary;
        world.addComponent(new DynamicComponent().setLocation(400, 578).setVelocity(40, -40));
        world.addComponent(new DynamicComponent().setLocation(400, 11).setVelocity(0, 0)).setSize(50, 20).setIsCircle(false).setInfinityMass(true);
        world.addComponent(new DynamicComponent().setLocation(400, 589).setVelocity(0, 0)).setSize(50, 20).setIsCircle(false).setInfinityMass(true);
        
        for(int i = 0; i < 16; i++)
            for(int j = 0; j < 17; j++)
                world.addComponent(new Tile().setLocation(25 + i*50, 120 + j*20).setSize(49, 19).setIsCircle(false));
        
        Global.mapWorld.put(worldId, world);
        Global.primary++;
        return worldId;
    };
    
    public static Player addPlayerToWorld(String sessionId){
        Player player = new Player();
        Global.mapPlayer.put(sessionId, player);
        for(Map.Entry<Integer, World> entry : Global.mapWorld.entrySet()){
            int numbPlayer = entry.getValue().addPlayer(player);
            if(numbPlayer != 0){
                player.worldNumber = entry.getKey();
                player.worldPointer = entry.getValue();
                return player;
            }
        }
        int worldId = Global.createWorld();
        Global.mapWorld.get(worldId).addPlayer(player);
        player.worldNumber = worldId;
        player.worldPointer = Global.mapWorld.get(worldId);
        return player;
    }
    
    public static void removePlayer(String sessionId){
        Player player = Global.mapPlayer.remove(sessionId);
        World world = player.worldPointer;
        if(player.playerNumber == 1)
            world.removePlayer1();
        else if(player.playerNumber == 2)
            world.removePlayer2();
        if(world.player1 == false && world.player2 == false)
            Global.mapWorld.remove(world);
    }
            
}
