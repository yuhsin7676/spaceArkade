package spacearkade.game;

import spacearkade.engine.World;

import java.util.Map;
import java.util.HashMap;
import spacearkade.engine.DynamicComponent;
import spacearkade.game.Player;
import spacearkade.game.Ball;
import spacearkade.game.ArkadeWorld;
import spacearkade.game.Platform;
import spacearkade.game.Tile;

public class Global {
    
    public static Map<Integer, ArkadeWorld> mapWorld = new HashMap<Integer, ArkadeWorld>();
    public static Map<String, Player> mapPlayer = new HashMap<String, Player>();
    public static int primary = 0;
    
    // Создает мир и заполняет его плитками и платформами
    public static int createWorld(){
        ArkadeWorld world = new ArkadeWorld(Global.primary, 800, 600);
        int worldId = Global.primary;
        world.addComponent(new Ball().setLocation(400, 568).setVelocity(40, -40));
        world.addComponent(new Platform().setLocation(400, 11).setVelocity(0, 0)).setSize(100, 20).setIsCircle(false).setInfinityMass(true);
        world.addComponent(new Platform().setLocation(400, 589).setVelocity(0, 0)).setSize(100, 20).setIsCircle(false).setInfinityMass(true);
        
        for(int i = 0; i < 16; i++)
            for(int j = 0; j < 17; j++)
                world.addComponent(new Tile().setLocation(25 + i*50, 120 + j*20).setSize(49, 19).setIsCircle(false));
        
        Global.mapWorld.put(worldId, world);
        Global.primary++;
        return worldId;
    };
    
    // Добавляет игрока
    public static void createPlayer(String sessionId){
        Player player = new Player();
        Global.mapPlayer.put(sessionId, player);
    };
    
    // Добавляет игрока в мир
    public static Player addPlayerToWorld(String sessionId){
        Player player = Global.mapPlayer.get(sessionId);
        for(Map.Entry<Integer, ArkadeWorld> entry : Global.mapWorld.entrySet()){
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
    
    // Удаляет игрока из мира и сессии.
    public static void removePlayer(String sessionId){
        Global.removePlayerFromWorld(sessionId);
        Global.mapPlayer.remove(sessionId);
    }
    
    // Удаляет игрока из мира. Если мир не имеет игроков, удаляет и мир
    public static void removePlayerFromWorld(String sessionId){
        Player player = Global.mapPlayer.get(sessionId);
        ArkadeWorld world = player.worldPointer;
        if(world != null){
            if(player.playerNumber == 1)
                world.removePlayer1();
            else if(player.playerNumber == 2)
                world.removePlayer2();
            if(world.player1 == false && world.player2 == false){
                Global.mapWorld.remove(world.id);
                player.worldPointer = null;
            }
        }
        
    }
           
}
