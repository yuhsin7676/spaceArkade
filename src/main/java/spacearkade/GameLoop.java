package spacearkade;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.utils.Array;
import com.google.gson.Gson;
import java.io.IOException;
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
            Player player = Global.addPlayerToWorld(session.getId());
            //events.add("Player add to world: " + player.worldNumber + ", and get number: " + player.playerNumber);
        });
        socketHandler.setDisconnectListener(session -> {
            Global.removePlayer(session.getId());
            //events.add(session.getId() + " just disconnect");
        });
        socketHandler.setMessageListener(((session, message) -> {
            //events.add(session.getId() + " said " + message);
        }));
    }

    @Override
    public void render() {
        for (WebSocketSession session : socketHandler.getSessions()){
            try{
                Player player = Global.mapPlayer.get(session.getId());
                World world = player.worldPointer;
                if(world.player1 == true && world.player2 == true){
                    world.update();
                    String json = new Gson().toJson(world);
                    session.sendMessage(new TextMessage(json));
                }
                else
                    session.sendMessage(new TextMessage("no"));
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
