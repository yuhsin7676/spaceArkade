package spacearkade;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.utils.Array;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import spacearkade.game.Player;
import spacearkade.game.World;
import spacearkade.websocket.WebSocketHandler;

@Component
public class GameLoop extends ApplicationAdapter{
    private final WebSocketHandler socketHandler;
    private Array<String> events = new Array<String>();

    public GameLoop(WebSocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }
    
    @Override
    public void create() {
        socketHandler.setConnectListener(session -> {
            // Перекинуть в Global
            Player player = new Player();
            Global.mapPlayer.put(session.getId(), player);
            for(Map.Entry<Integer, World> entry : Global.mapWorld.entrySet()){
                int numbPlayer = entry.getValue().addPlayer(player);
                if(numbPlayer != 0){
                    player.worldNumber = entry.getKey();
                    player.worldPointer = entry.getValue();
                    events.add("Player add to world: " + entry.getKey() + ", and get number: " + player.playerNumber);
                    return;
                }
            }
            int worldId = Global.createWorld();
            Global.mapWorld.get(worldId).addPlayer(player);
            player.worldNumber = worldId;
            player.worldPointer = Global.mapWorld.get(worldId);
            events.add("Player add to world: " + worldId + ", and get number: " + player.playerNumber);
        });
        socketHandler.setDisconnectListener(session -> {
            events.add(session.getId() + " just disconnect");
            World world = Global.mapPlayer.get(session.getId()).worldPointer;
            if(world.player1 == null && world.player2 == null)
                Global.mapWorld.remove(world);
            Global.mapPlayer.remove(session.getId());
        });
        socketHandler.setMessageListener(((session, message) -> {
            events.add(session.getId() + " said " + message);
        }));
    }

    @Override
    public void render() {
        for (WebSocketSession session : socketHandler.getSessions()){
            try{
                Player player = Global.mapPlayer.get(session.getId());
                World world = player.worldPointer;
                if(world.player1 != null && world.player2 != null)
                    session.sendMessage(new TextMessage("yes"));
                else
                    session.sendMessage(new TextMessage("no"));
                for (String event : events)
                    session.sendMessage(new TextMessage(event));
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        events.clear();
    }
    
}
