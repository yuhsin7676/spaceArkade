package spacearkade;

import spacearkade.game.World;

import java.util.Map;
import java.util.HashMap;
import spacearkade.game.Player;

public class Global {
    
    public static Map<Integer, World> mapWorld = new HashMap<Integer, World>();
    public static Map<String, Player> mapPlayer = new HashMap<String, Player>();
    public static int primary = 0;
    
    public static int createWorld(){
        World world = new World(800, 600);
        int worldId = Global.primary;
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
        if(world.player1 == null && world.player2 == null)
            Global.mapWorld.remove(world);
    }
            
}
