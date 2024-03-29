/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package spacearkade.engine;

import java.util.Map;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author ilya
 */
public class WorldTest {
    
    public WorldTest() {
    }
    
    /**
     * Тестируется добавление 3-х компонентов в мир.
     */
    @org.junit.jupiter.api.Test
    public void testAddThreeComponentsIntoNewWorld() {
        System.out.println("addComponentsIntoNewWorld");
        World world = new World(1, 800, 600);
        world.addComponent(new Component());
        world.addComponent(new Component());
        world.addComponent(new Component());
        assertEquals(world.components.size(), 3);
    }

    /**
     * Тестируется addComponentToQueue() - добавление 3-х компонентов в очередь (не в мир). 
     * После коммита (commitAddQueue()) компоненты должны оказаться в мире.
     */
    @org.junit.jupiter.api.Test
    public void testAddComponentToQueueAndCommit() {
        System.out.println("testAddComponentToQueue");
        
        World world = new World(1, 800, 600);
        world.addComponentToQueue(new Component());
        world.addComponentToQueue(new Component());
        world.addComponentToQueue(new Component());
        assertEquals(world.components.size(), 0);
        world.commitAddQueue();
        assertEquals(world.components.size(), 3);
    }
    
    /**
     * Тестируется взятие несуществующего компонента из мира через getComponent(). Должен вернуть null.
     */
    @org.junit.jupiter.api.Test
    public void testGetUnexistentComponent() {
        System.out.println("getComponentIfNull");
        
        World world = new World(1, 800, 600);
        assertNull(world.getComponent(444)); // any id
    }

    /**
     * Тестируется метод deleteComponent(). Должен удалить компонент.
     */
    @org.junit.jupiter.api.Test
    public void testAddGetDeleteComponent() {
        System.out.println("addGetDeleteComponent");
        
        World world = new World(1, 800, 600);
        
        Component component = new Component();
        
        world.addComponent(component);
        assertEquals(world.components.size(), 1);
        assertEquals(world.getComponent(component.getId()), component);
        
        world.deleteComponent(0);
        assertEquals(world.components.size(), 0);
        assertNull(world.getComponent(component.getId()));
    }

    /**
     * Тестируется метод clearWorld(). Должен удалить все компоненты из мира.
     */
    @org.junit.jupiter.api.Test
    public void testAddThreeComponentsAndClearWorld() {
        System.out.println("clearWorld");
        
        World world = new World(1, 800, 600);
        world.addComponent(new Component());
        world.addComponent(new Component());
        world.addComponent(new Component());
        assertEquals(world.components.size(), 3);
        
        world.clearWorld();
        assertEquals(world.components.size(), 0);
    }

    /**
     * Тестируется метод addSound(). Не должен выдать ошибок
     */
    @org.junit.jupiter.api.Test
    public void testAddSound() {
        System.out.println("addSound");
        
        World world = new World(1, 800, 600);
        world.addSound("mySound");  
    }

    /**
     * Тестируется метод clearSounds(). Не должен выдать ошибок
     */
    @org.junit.jupiter.api.Test
    public void testClearSounds() {
        System.out.println("clearSounds");
        
        World world = new World(1, 800, 600);
        world.clearSounds();
    }

    /**
     * Тестируется метод update().
     * В качестве примера взяты 2 шара, летящие друг на друга.
     * После выполнения тестируемого метода шары должны удариться и изменить скорости и координаты на ожидаемые,
     * а в поле eventHit каждого шара должна быть занесена информация о столкновении.
     */
    @org.junit.jupiter.api.Test
    public void testUpdate() {
        System.out.println("update");
        
        World world = new World(1, 800, 600);
        world.frame = 1;
        
        // Тестируется соударение 2 шаров
        Component ball1 = new Component().setLocation(395, 300).setRadius(10).setVelocity(20, 0);
        Component ball2 = new Component().setLocation(420, 300).setRadius(10).setVelocity(0, 0);
        ball1.isStaticComponent = ball2.isStaticComponent = false;
        ball1.m = ball2.m = 1;
        ball1.className = ball2.className = "ball";
        
        world.addComponent(ball1);
        world.addComponent(ball2);
        
        world.update();
        
        // Проверяем скорости и координаты
        assertEquals(ball1.getLocation(), new Vector2D(400, 300));
        assertEquals(ball1.getVelocity(), new Vector2D(0, 0));
        assertEquals(ball2.getLocation(), new Vector2D(435, 300));
        assertEquals(ball2.getVelocity(), new Vector2D(20, 0));
        
        // Проверяем eventHit 
        assertEquals(ball1.eventHit.size(), 1);
        assertEquals(ball2.eventHit.size(), 1);
        
        Event eventHit1 = ball1.eventHit.get(0);
        assertEquals(eventHit1.className, ball2.className);
        assertEquals(eventHit1.id, ball2.id);
        assertEquals(eventHit1.location, new Vector2D(420, 300));
        assertEquals(eventHit1.velocity, ball2.velocity);
        assertEquals(eventHit1.size, ball2.size);
        
        Event eventHit2 = ball2.eventHit.get(0);
        assertEquals(eventHit2.className, ball1.className);
        assertEquals(eventHit2.id, ball1.id);
        assertEquals(eventHit2.location, new Vector2D(400, 300));
        assertEquals(eventHit2.velocity, ball1.velocity);
        assertEquals(eventHit2.size, ball1.size);

    }
    
}
