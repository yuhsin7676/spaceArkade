package spacearkade;

import spacearkade.game.Global;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.utils.Array;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import spacearkade.game.Player;
import spacearkade.game.ArkadeWorld;
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
            Global.createPlayer(session.getId());
        });
        socketHandler.setDisconnectListener(session -> {
            Global.removePlayer(session.getId());
        });
        socketHandler.setMessageListener(((session, message) -> {
            Player player = Global.mapPlayer.get(session.getId());
            
            if(player.worldPointer != null){
                Type type = new TypeToken<Vector2D>(){}.getType();
                Vector2D velocity = new Gson().fromJson(message, type);

                player.object.setVelocity(velocity);
            }
            else{
                switch(message){
                    case "play":
                        Global.addPlayerToWorld(session.getId());
                        break;
                }
            }
            
        }));
    }

    @Override
    public void render() {
        //System.out.println(Global.mapWorld.size());
        for (WebSocketSession session : socketHandler.getSessions()){
            try{
                Player player = Global.mapPlayer.get(session.getId());
                ArkadeWorld world = player.worldPointer;
                if(world != null){
                    //System.out.println("1");
                    if(world.player1 == true && world.player2 == true){
                        world.update();
                        String json = new Gson().toJson(world);
                        session.sendMessage(new TextMessage(json));
                    }
                    else if(world.player1 == false && world.player2 == false){
                        Global.removePlayerFromWorld(String.valueOf(session.getId()));
                        session.sendMessage(new TextMessage("lose"));
                    }
                    else{
                        session.sendMessage(new TextMessage("wait"));
                    }
                }
                else{
                    session.sendMessage(new TextMessage("lose"));
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        events.clear();
    }
    
}
