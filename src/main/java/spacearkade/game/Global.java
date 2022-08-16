package spacearkade.game;

import spacearkade.game.components.Ball;
import spacearkade.game.components.Platform;
import spacearkade.game.components.Tile;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import spacearkade.engine.Component;
import spacearkade.game.components.Tile1;
import spacearkade.game.components.Tile2;
import spacearkade.game.components.Tile3;

public class Global {
    
    public static Map<Integer, ArkadeWorld> mapWorld = new HashMap<Integer, ArkadeWorld>();
    public static Map<String, Player> mapPlayer = new HashMap<String, Player>();
    public static int primary = 0;
    public static String[][][] beginCompnents = {{ 
        {"Tile1", "Tile1", "Tile1", "Tile1", "     ", "     ", "Tile1", "Tile1", "Tile1", "Tile1"},
        {"Tile1", "Tile1", "Tile1", "Tile1", "     ", "     ", "Tile1", "Tile1", "Tile1", "Tile1"},
        {"Tile2", "Tile2", "Tile2", "Tile2", "     ", "     ", "Tile2", "Tile2", "Tile2", "Tile2"},
        {"Tile2", "Tile2", "Tile2", "Tile2", "     ", "     ", "Tile2", "Tile2", "Tile2", "Tile2"},
        {"Tile3", "Tile3", "Tile3", "Tile3", "     ", "     ", "Tile3", "Tile3", "Tile3", "Tile3"},
        {"Tile3", "Tile3", "Tile3", "Tile3", "     ", "     ", "Tile3", "Tile3", "Tile3", "Tile3"},
        {"     ", "Tile1", "Tile1", "Tile1", "     ", "     ", "Tile1", "Tile1", "Tile1", "     "},
        {"     ", "Tile1", "Tile1", "Tile1", "     ", "     ", "Tile1", "Tile1", "Tile1", "     "},
        {"     ", "Tile1", "Tile1", "     ", "     ", "     ", "     ", "Tile1", "Tile1", "     "},
        {"     ", "     ", "     ", "     ", "     ", "     ", "     ", "     ", "     ", "     "},
        {"     ", "     ", "     ", "     ", "     ", "     ", "     ", "     ", "     ", "     "},
        {"     ", "Tile1", "Tile1", "     ", "     ", "     ", "     ", "Tile1", "Tile1", "     "},
        {"     ", "Tile1", "Tile1", "Tile1", "     ", "     ", "Tile1", "Tile1", "Tile1", "     "},
        {"     ", "Tile1", "Tile1", "Tile1", "     ", "     ", "Tile1", "Tile1", "Tile1", "     "},
        {"Tile3", "Tile3", "Tile3", "Tile3", "     ", "     ", "Tile3", "Tile3", "Tile3", "Tile3"},
        {"Tile3", "Tile3", "Tile3", "Tile3", "     ", "     ", "Tile3", "Tile3", "Tile3", "Tile3"},
        {"Tile2", "Tile2", "Tile2", "Tile2", "     ", "     ", "Tile2", "Tile2", "Tile2", "Tile2"},
        {"Tile2", "Tile2", "Tile2", "Tile2", "     ", "     ", "Tile2", "Tile2", "Tile2", "Tile2"},
        {"Tile1", "Tile1", "Tile1", "Tile1", "     ", "     ", "Tile1", "Tile1", "Tile1", "Tile1"},
        {"Tile1", "Tile1", "Tile1", "Tile1", "     ", "     ", "Tile1", "Tile1", "Tile1", "Tile1"}
    },
        { 
        {"Tile1", "Tile1", "Tile1", "Tile1", "     ", "     ", "Tile1", "Tile1", "Tile1", "Tile1"},
        {"Tile1", "Tile1", "Tile1", "Tile1", "     ", "     ", "Tile1", "Tile1", "Tile1", "Tile1"},
        {"     ", "     ", "     ", "Tile1", "     ", "     ", "Tile1", "     ", "Tile2", "     "},
        {"     ", "     ", "     ", "Tile1", "     ", "     ", "Tile1", "     ", "Tile2", "     "},
        {"     ", "     ", "     ", "Tile1", "     ", "     ", "Tile1", "     ", "Tile2", "     "},
        {"     ", "     ", "     ", "Tile1", "     ", "     ", "Tile1", "Tile2", "Tile2", "     "},
        {"Tile3", "Tile3", "Tile3", "Tile1", "     ", "     ", "Tile1", "Tile2", "Tile2", "     "},
        {"Tile3", "Tile3", "Tile3", "Tile1", "     ", "     ", "Tile1", "     ", "     ", "     "},
        {"Tile3", "Tile3", "Tile3", "Tile1", "     ", "     ", "Tile1", "     ", "     ", "     "},
        {"     ", "     ", "     ", "Tile1", "     ", "     ", "Tile1", "     ", "     ", "     "},
        {"     ", "     ", "     ", "Tile1", "     ", "     ", "Tile1", "     ", "     ", "     "},
        {"     ", "     ", "     ", "Tile1", "     ", "     ", "Tile1", "Tile3", "Tile3", "Tile3"},
        {"     ", "     ", "     ", "Tile1", "     ", "     ", "Tile1", "Tile3", "Tile3", "Tile3"},
        {"     ", "Tile2", "Tile2", "Tile1", "     ", "     ", "Tile1", "Tile3", "Tile3", "Tile3"},
        {"     ", "Tile2", "Tile2", "Tile1", "     ", "     ", "Tile1", "     ", "     ", "     "},
        {"     ", "Tile2", "     ", "Tile1", "     ", "     ", "Tile1", "     ", "     ", "     "},
        {"     ", "Tile2", "     ", "Tile1", "     ", "     ", "Tile1", "     ", "     ", "     "},
        {"     ", "Tile2", "     ", "Tile1", "     ", "     ", "Tile1", "     ", "     ", "     "},
        {"Tile1", "Tile1", "Tile1", "Tile1", "     ", "     ", "Tile1", "Tile1", "Tile1", "Tile1"},
        {"Tile1", "Tile1", "Tile1", "Tile1", "     ", "     ", "Tile1", "Tile1", "Tile1", "Tile1"}
    },
        { 
        {"Tile3", "Tile3", "Tile3", "Tile3", "     ", "     ", "Tile3", "Tile3", "Tile3", "Tile3"},
        {"Tile3", "Tile3", "Tile3", "Tile3", "     ", "     ", "Tile3", "Tile3", "Tile3", "Tile3"},
        {"Tile2", "Tile2", "Tile2", "Tile3", "     ", "     ", "Tile2", "Tile2", "Tile2", "Tile2"},
        {"Tile2", "Tile2", "Tile2", "Tile3", "     ", "     ", "Tile2", "Tile2", "Tile2", "Tile2"},
        {"     ", "     ", "     ", "Tile3", "     ", "     ", "Tile3", "     ", "     ", "     "},
        {"Tile1", "Tile1", "     ", "Tile3", "     ", "     ", "Tile3", "Tile1", "     ", "     "},
        {"Tile2", "Tile2", "     ", "Tile3", "     ", "     ", "     ", "Tile1", "Tile1", "     "},
        {"     ", "     ", "     ", "Tile3", "     ", "     ", "     ", "     ", "     ", "     "},
        {"     ", "Tile2", "Tile2", "Tile3", "     ", "     ", "     ", "     ", "     ", "Tile2"},
        {"     ", "Tile2", "Tile2", "Tile3", "     ", "     ", "     ", "     ", "     ", "     "},
        {"     ", "     ", "     ", "     ", "     ", "     ", "Tile3", "Tile2", "Tile2", "     "},
        {"Tile2", "     ", "     ", "     ", "     ", "     ", "Tile3", "Tile2", "Tile2", "     "},
        {"     ", "     ", "     ", "     ", "     ", "     ", "Tile3", "     ", "     ", "     "},
        {"     ", "Tile1", "Tile1", "     ", "     ", "     ", "Tile3", "     ", "Tile2", "Tile2"},
        {"     ", "     ", "Tile1", "Tile3", "     ", "     ", "Tile3", "     ", "Tile1", "Tile1"},
        {"     ", "     ", "     ", "Tile3", "     ", "     ", "Tile3", "     ", "     ", "     "},
        {"Tile2", "Tile2", "Tile2", "Tile2", "     ", "     ", "Tile3", "Tile2", "Tile2", "Tile2"},
        {"Tile2", "Tile2", "Tile2", "Tile2", "     ", "     ", "Tile3", "Tile2", "Tile2", "Tile2"},
        {"Tile3", "Tile3", "Tile3", "Tile3", "     ", "     ", "Tile3", "Tile3", "Tile3", "Tile3"},
        {"Tile3", "Tile3", "Tile3", "Tile3", "     ", "     ", "Tile3", "Tile3", "Tile3", "Tile3"}
    }
    };

    
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
        
        Global.addBeginCompnents(world);
        Global.mapWorld.put(worldId, world);
        Global.primary++;
        return worldId;
    };
    
    // Добавляет компоненты исходя из двумерного массива
    private static void addBeginCompnents(ArkadeWorld world){
        int k = new Random().nextInt(3);
        for(int i = 0; i < beginCompnents[k].length; i++){
            for(int j = 0; j < beginCompnents[k][i].length; j++){
                switch(beginCompnents[k][i][j]){
                    case "Tile1":
                        world.addComponent(new Tile1().setLocation(40 + j*80, 110 + i*20));
                        break;
                    case "Tile2":
                        world.addComponent(new Tile2().setLocation(40 + j*80, 110 + i*20));
                        break;
                    case "Tile3":
                        world.addComponent(new Tile3().setLocation(40 + j*80, 110 + i*20));
                        break;
                }
            }
        }
    }
           
}
