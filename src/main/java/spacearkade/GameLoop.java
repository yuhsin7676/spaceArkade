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
            
            // Если игрок находится в мире
            if(player.worldPointer != null){
                try{
                    Type type = new TypeToken<Vector2D>(){}.getType();
                    Vector2D velocity = new Gson().fromJson(message, type);

                    player.object.setVelocity(velocity);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            
            // Иначе ждем команду play от клиента
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
        Global.update();
        for (WebSocketSession session : socketHandler.getSessions()){
            try{
                Player player = Global.mapPlayer.get(session.getId());
                if(player != null){
                    
                    ArkadeWorld world = player.worldPointer;
                    if(world != null){

                        // Если в мире есть оба игрока, значит игра идет, отошлем клиенту состояние мира
                        if(world.haveAllPlayers()){
                            String json = new Gson().toJson(player);
                            session.sendMessage(new TextMessage(json));
                        }

                        // Если в мире нет ни одного игрока, значит игрок вне игры. Удалим ссылку на этот мир у игрока
                        else if(!world.havePlayers()){
                            Global.removePlayerFromWorld(session.getId());
                            session.sendMessage(new TextMessage("noPlay"));
                        }

                        // Иначе игрок ожидает другого игрока
                        else
                            session.sendMessage(new TextMessage("wait"));

                    }
                    else{
                        session.sendMessage(new TextMessage("noPlay"));
                    }
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        events.clear();
    }
    
}
