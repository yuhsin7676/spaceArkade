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
            
}
