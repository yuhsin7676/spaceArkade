package spacearkade.game;

import spacearkade.game.components.Ball;
import spacearkade.game.components.Platform;
import spacearkade.game.components.Tile;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import spacearkade.game.components.Tile1;
import spacearkade.game.components.Tile2;
import spacearkade.game.components.Tile3;

public class Global {
    
    public static Map<Integer, ArkadeWorld> mapWorld = new HashMap<Integer, ArkadeWorld>();
    public static Map<String, Player> mapPlayer = new HashMap<String, Player>();
    public static int primary = 0;
    
    // Добавляет игрока
    public static void createPlayer(String sessionId){
        Player player = new Player();
        Global.mapPlayer.put(sessionId, player);
    };
    
    // Добавляет игрока в мир
    public static void addPlayerToWorld(String sessionId){
        Player player = Global.mapPlayer.get(sessionId);
        if(player.worldPointer == null){
            for(Map.Entry<Integer, ArkadeWorld> entry : Global.mapWorld.entrySet()){
                int numbPlayer = entry.getValue().addPlayer(player);
                if(numbPlayer != 0){
                    player.worldNumber = entry.getKey();
                    player.worldPointer = entry.getValue();
                    return;
                }
            }
            int worldId = Global.createWorld();
            Global.mapWorld.get(worldId).addPlayer(player);
            player.worldNumber = worldId;
            player.worldPointer = Global.mapWorld.get(worldId);
        }
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
            if(!world.havePlayers())
                Global.mapWorld.remove(world.id);// Возможно, можно удалить
        }
        
    }
    
    // Обновляет все миры
    public static void update(){
        
        // Удаляем через явный итератор, иначе будет ошибка ConcurrentModificationException
        Iterator<Entry<Integer, ArkadeWorld>> i = mapWorld.entrySet().iterator();
        while(i.hasNext())
            if(i.next().getValue().canBeRemove)
                i.remove();
        
        // вызовем update() у миров
        for(Map.Entry<Integer, ArkadeWorld> entry : mapWorld.entrySet())
            entry.getValue().update();
        
    }
    
    // Создает мир и заполняет его плитками и платформами. Возвращает id созданного мира
    private static int createWorld(){
        ArkadeWorld world = new ArkadeWorld(Global.primary, 800, 600);
        int worldId = Global.primary;
        world.addComponent(new Ball().setWorldPointer(world).setLocation(400, 300).setVelocity(150, -150));
        world.addComponent(new Platform().setLocation(400, 11));
        world.addComponent(new Platform().setLocation(400, 589));
        
        // Заполнение плитками лучше переписать в мапы
        for(int i = 0; i < 2; i++)
            for(int j = 0; j < 17; j++)
                world.addComponent(new Tile3().setWorldPointer(world).setLocation(25 + i*50, 120 + j*20));
        
        for(int i = 2; i < 4; i++)
            for(int j = 0; j < 17; j++)
                world.addComponent(new Tile2().setWorldPointer(world).setLocation(25 + i*50, 120 + j*20));
        
        for(int i = 4; i < 6; i++)
            for(int j = 0; j < 17; j++)
                world.addComponent(new Tile1().setWorldPointer(world).setLocation(25 + i*50, 120 + j*20));
        
        for(int i = 10; i < 12; i++)
            for(int j = 0; j < 17; j++)
                world.addComponent(new Tile1().setWorldPointer(world).setLocation(25 + i*50, 120 + j*20));
        
        for(int i = 12; i < 14; i++)
            for(int j = 0; j < 17; j++)
                world.addComponent(new Tile2().setWorldPointer(world).setLocation(25 + i*50, 120 + j*20));
        
        for(int i = 14; i < 16; i++)
            for(int j = 0; j < 17; j++)
                world.addComponent(new Tile3().setWorldPointer(world).setLocation(25 + i*50, 120 + j*20));
        
        Global.mapWorld.put(worldId, world);
        Global.primary++;
        return worldId;
    };
           
}
