/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package spacearkade.game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import spacearkade.websocket.WebSocketHandler;

/**
 *
 * @author ilya
 */
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
