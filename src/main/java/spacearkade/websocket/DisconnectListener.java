package spacearkade.websocket;

import org.springframework.web.socket.WebSocketSession;

public interface DisconnectListener {
    void handle(WebSocketSession session);
}
