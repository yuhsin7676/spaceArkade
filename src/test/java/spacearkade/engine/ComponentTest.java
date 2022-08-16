/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package spacearkade.engine;

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
public class ComponentTest {

    /**
     * Test of removed method, of class Component.
     */
    @Test
    public void testRemoved() {
        System.out.println("removed");
        
        World world = new World(1, 800, 600);
        Component component = new Component();
        
        world.addComponent(component);
        assertEquals(world.components.size(), 1);
        
        component.removed();
        world.update();
        assertEquals(world.components.size(), 0);
    }

    /**
     * Test of getLocation method, of class Component.
     */
    @Test
    public void testSetGetLocation() {
        System.out.println("setGetLocation");
        Component component = new Component().setLocation(20, 20);
        assertEquals(component.getLocation(), new Vector2D(20, 20));
    }

    /**
     * Test of getVelocity method, of class Component.
     */
    @Test
    public void testSetGetVelocity() {
        System.out.println("setGetVelocity");
        Component component = new Component().setVelocity(20, 20);
        assertEquals(component.getVelocity(), new Vector2D(20, 20));
    }

    /**
     * Test of getSize method, of class Component.
     */
    @Test
    public void testSetGetSize() {
        System.out.println("setGetSize");
        Component component = new Component().setSize(20, 20);
        assertEquals(component.getSize(), new Vector2D(20, 20));
    }

    /**
     * Test of getRadius method, of class Component.
     */
    @Test
    public void testSetRadius() {
        System.out.println("setGetRadius");
        Component component = new Component().setRadius(20);
        assertEquals(component.getRadius(), 20);
    }

    /**
     * Test of getId method, of class Component.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        Component component = new Component();
        component.id = 0;
        assertEquals(component.getId(), 0);
    }

    /**
     * Test of getIsInfinityMass method, of class Component.
     */
    @Test
    public void testSetGetIsInfinityMass() {
        System.out.println("setGetIsInfinityMass");
        Component component = new Component().setInfinityMass(true);
        assertEquals(component.getIsInfinityMass(), true);
    }

    /**
     * Test of setIsCircle method, of class Component.
     */
    @Test
    public void testSetIsCircle() {
        System.out.println("setIsCircle");
        Component component = new Component().setIsCircle(true);
        assertEquals(component.isCircle, true);
    }
    
}
