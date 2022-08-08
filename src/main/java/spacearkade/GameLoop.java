package spacearkade;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.utils.Array;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import spacearkade.engine.Player;
import spacearkade.engine.World;
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
            Global.addPlayerToWorld(session.getId());
            //events.add("Player add to world: " + player.worldNumber + ", and get number: " + player.playerNumber);
        });
        socketHandler.setDisconnectListener(session -> {
            Global.removePlayer(session.getId());
            //events.add(session.getId() + " just disconnect");
        });
        socketHandler.setMessageListener(((session, message) -> {
            Type type = new TypeToken<Vector2D>(){}.getType();
            Vector2D velocity = new Gson().fromJson(message, type);
            
            Player player = Global.mapPlayer.get(session.getId());
            player.object.setVelocity(velocity);
            //events.add(session.getId() + " said " + message);
        }));
    }

    @Override
    public void render() {
        for (WebSocketSession session : socketHandler.getSessions()){
            try{
                Player player = Global.mapPlayer.get(session.getId());
                World world = player.worldPointer;
                if(world != null){
                    if(world.player1 == true && world.player2 == true){
                        world.update();
                        String json = new Gson().toJson(world);
                        session.sendMessage(new TextMessage(json));
                    }
                    else
                        session.sendMessage(new TextMessage("no"));
                }
                //for(String event : events)
                    //session.sendMessage(new TextMessage(event));
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        events.clear();
    }
    
}
