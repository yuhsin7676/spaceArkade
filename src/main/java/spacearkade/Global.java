package spacearkade;

import spacearkade.game.World;

import java.util.Map;
import java.util.HashMap;

public class Global {
    
    public static Map<Integer, World> mapWorld = new HashMap<Integer, World>();
    public static int primary = 0;
    
    public static int createWorld(){
        World world = new World(800, 600);
        int worldId = Global.primary;
        Global.mapWorld.put(worldId, world);
        Global.primary++;
        return worldId;
    };
            
}
