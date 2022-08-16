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
    /*
    @org.junit.jupiter.api.BeforeAll
    public static void setUpClass() throws Exception {
    }

    @org.junit.jupiter.api.AfterAll
    public static void tearDownClass() throws Exception {
    }

    @org.junit.jupiter.api.BeforeEach
    public void setUp() throws Exception {
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }*/
    
    /**
     * Test of addComponent method, 
     * when add 3 new Components.
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
     * Test of addComponentToQueue and commitAddQueue methods, 
     * when add 3 new Components into Queue.
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
     * Test of getComponent method when world doesn't contain Component with choosed id.
     */
    @org.junit.jupiter.api.Test
    public void testGetUnexistentComponent() {
        System.out.println("getComponentIfNull");
        
        World world = new World(1, 800, 600);
        assertNull(world.getComponent(444)); // any id
    }

    /**
     * Test of deleteComponent method, of class World.
     */
    @org.junit.jupiter.api.Test
    public void testAddGetDeleteComponent() {
        System.out.println("getComponentIfNull");
        
        World world = new World(1, 800, 600);
        
        Component component = new Component();
        
        world.addComponent(component);
        assertEquals(world.components.size(), 1);
        assertEquals(world.getComponent(0), component);
        
        world.deleteComponent(0);
        assertEquals(world.components.size(), 0);
        assertNull(world.getComponent(0));
    }

    /**
     * Test of clearWorld method, of class World.
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
     * Test of addSound method, of class World. Проверяется, что приложение не упадет
     */
    @org.junit.jupiter.api.Test
    public void testAddSound() {
        System.out.println("addSound");
        
        World world = new World(1, 800, 600);
        world.addSound("mySound");  
    }

    /**
     * Test of clearSounds method, of class World. Проверяется, что приложение не упадет
     */
    @org.junit.jupiter.api.Test
    public void testClearSounds() {
        System.out.println("clearSounds");
        
        World world = new World(1, 800, 600);
        world.clearSounds();
    }

    /**
     * Test of update method, of class World.
     */
    @org.junit.jupiter.api.Test
    public void testUpdate() {
        System.out.println("update");
        
        World world = new World(1, 800, 600);
        world.frame = 1;
        
        Component ball1 = new Component().setLocation(395, 300).setRadius(10).setVelocity(20, 0);
        Component ball2 = new Component().setLocation(420, 300).setRadius(10).setVelocity(0, 0);
        ball1.isStaticComponent = ball2.isStaticComponent = false;
        ball1.m = ball2.m = 1;
        
        world.addComponent(ball1);
        world.addComponent(ball2);
        
        world.update();
        assertEquals(ball1.getLocation(), new Vector2D(400, 300));
        assertEquals(ball1.getVelocity(), new Vector2D(0, 0));
        assertEquals(ball2.getLocation(), new Vector2D(435, 300));
        assertEquals(ball2.getVelocity(), new Vector2D(20, 0));
    }
    
}
