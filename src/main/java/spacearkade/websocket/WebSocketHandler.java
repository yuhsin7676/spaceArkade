package spacearkade.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import com.badlogic.gdx.utils.Array;

@Component
public class WebSocketHandler extends AbstractWebSocketHandler{
    private Array<WebSocketSession> sessions = new Array<WebSocketSession>();
    private ConnectListener connectListener;
    private MessageListener messageListener;
    private DisconnectListener disconnectListener;
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        connectListener.handle(session);
    }
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        messageListener.handle(session, message.getPayload());
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.removeValue(session, true);
        disconnectListener.handle(session);
    }    

    public Array<WebSocketSession> getSessions() {
        return sessions;
    }
    
    public void setConnectListener(ConnectListener connectListener) {
        this.connectListener = connectListener;
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public void setDisconnectListener(DisconnectListener disconnectListener) {
        this.disconnectListener = disconnectListener;
    }
    
}
