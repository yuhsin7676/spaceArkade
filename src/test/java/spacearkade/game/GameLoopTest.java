package spacearkade.game;

import org.junit.jupiter.api.Test;
import spacearkade.websocket.WebSocketHandler;

public class GameLoopTest {

    /**
     * Тестируется метод create(), который просто не должен упасть.
     */
    @Test
    public void testCreate() {
        System.out.println("create");
        
        GameLoop gameLoop = new GameLoop(new WebSocketHandler());
        gameLoop.create();
    }

    /**
     * Тестируется метод render(), который просто не должен упасть.
     */
    @Test
    public void testRender() {
        System.out.println("render");
        
        GameLoop gameLoop = new GameLoop(new WebSocketHandler());
        gameLoop.create();
    }
    
}
