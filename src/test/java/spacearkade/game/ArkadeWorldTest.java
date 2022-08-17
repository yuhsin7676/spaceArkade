package spacearkade.game;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import spacearkade.engine.Component;
import spacearkade.game.components.Ball;
import spacearkade.game.components.Platform;
import spacearkade.game.components.Tile;

/**
 *
 * @author ilya
 */
public class ArkadeWorldTest {

    /**
     * Тестирование конструктора ArkadeWorld. Должен создать внутри себя шар и 2 платформы и не содержать игроков.
     */
    @Test
    public void testConstructor() {
        System.out.println("constructor");
        
        ArkadeWorld arkadeWorld = new ArkadeWorld(1, 800, 600);
        assertFalse(arkadeWorld.haveAllPlayers());
        assertFalse(arkadeWorld.havePlayers());
        assertEquals(arkadeWorld.status, EnumStatus.WAIT);
        assertEquals(arkadeWorld.getComponent(0).getClass(), Ball.class);
        assertEquals(arkadeWorld.getComponent(1).getClass(), Platform.class);
        assertEquals(arkadeWorld.getComponent(2).getClass(), Platform.class);
        
    }
    
    /**
     * Тестирование методов addComponent() и getComponent() (родительский метод addComponent() здесь переписан, поэтому написан тест). 
     */
    @Test
    public void testAddGetComponent() {
        System.out.println("addGetComponent");
        
        ArkadeWorld arkadeWorld = new ArkadeWorld(1, 800, 600);
        Component component = new Component();
        arkadeWorld.addComponent(component);
        assertEquals(arkadeWorld.getComponent(component.getId()), component);
        
    }
    
    /**
     * Тестирование метода addPlayer() на примере добавления 2-х игроков. Проверяется наличие игроков в мире, а также статус последнего.
     */
    @Test
    public void testAddTwoPlayers() {
        System.out.println("addComponent");
        
        ArkadeWorld arkadeWorld = new ArkadeWorld(1, 800, 600);
        
        // Добавим плитку, так как предполагается в нчале игры наличие хотя бы одной.
        arkadeWorld.addComponent(new Tile().setLocation(0, 300));
        
        arkadeWorld.addPlayer(new Player());
        assertEquals(arkadeWorld.status, EnumStatus.WAIT);
        assertFalse(arkadeWorld.haveAllPlayers());
        assertTrue(arkadeWorld.havePlayers());
        
        // Умножение нужно, чтобы ballVelocity был ссылкой на новый вектор, а не на ball.location;
        Vector2D ballLocation = arkadeWorld.getComponent(0).getLocation().scalarMultiply(1);
        
        // В режиме WAIT update НЕ должен пересчитывать координаты
        arkadeWorld.update();
        assertEquals(arkadeWorld.getComponent(0).getLocation(), ballLocation);
        
        arkadeWorld.addPlayer(new Player());
        assertEquals(arkadeWorld.status, EnumStatus.PLAY);
        assertTrue(arkadeWorld.haveAllPlayers());
        assertTrue(arkadeWorld.havePlayers());
        
        // В режиме PLAY update должен пересчитывать координаты
        arkadeWorld.update();
        assertNotEquals(arkadeWorld.getComponent(0).getLocation(), ballLocation);
        assertEquals(arkadeWorld.status, EnumStatus.PLAY);
        
    }
    
    /**
     * Воссоздание ситуации поражения (уничтожены все шары).
     */
    @Test
    public void testPlayAndRemoveBall() {
        System.out.println("playAndRemoveBall");
        
        ArkadeWorld arkadeWorld = new ArkadeWorld(1, 800, 600);
        arkadeWorld.addComponent(new Tile().setLocation(0, 300));
        arkadeWorld.addPlayer(new Player());
        arkadeWorld.addPlayer(new Player());
        assertEquals(arkadeWorld.status, EnumStatus.PLAY);
        
        arkadeWorld.update();
        assertEquals(arkadeWorld.status, EnumStatus.PLAY);
        arkadeWorld.getComponent(0).removed();
        
        // Удаление единственного шара должно менять статус мира на LOSE и удалять игроков
        arkadeWorld.update();
        assertEquals(arkadeWorld.status, EnumStatus.LOSE);
        assertFalse(arkadeWorld.havePlayers());
        
    }
    
    /**
     * Воссоздание ситуации победы (уничтожены все плитки).
     */
    @Test
    public void testPlayAndRemoveTile() {
        System.out.println("playAndRemoveTile");
        
        ArkadeWorld arkadeWorld = new ArkadeWorld(1, 800, 600);
        arkadeWorld.addComponent(new Tile().setLocation(0, 300));
        arkadeWorld.addPlayer(new Player());
        arkadeWorld.addPlayer(new Player());
        assertEquals(arkadeWorld.status, EnumStatus.PLAY);
        
        arkadeWorld.update();
        assertEquals(arkadeWorld.status, EnumStatus.PLAY);
        arkadeWorld.getComponent(3).removed();
        
        // Удаление единственной плитки должно менять статус мира на WIN и удалять игроков
        arkadeWorld.update();
        assertEquals(arkadeWorld.status, EnumStatus.WIN);
        assertFalse(arkadeWorld.havePlayers());
        
    }

    

    /**
     * Тестирование метода removePlayer1(). Должен удалить 1-го игрока, а в случае отсутствия такового не должно случаться ошибок.
     */
    @Test
    public void testRemovePlayer1() {
        System.out.println("removePlayer1");
        
        ArkadeWorld arkadeWorld = new ArkadeWorld(1, 800, 600);
        arkadeWorld.addComponent(new Tile().setLocation(0, 300));
        arkadeWorld.addPlayer(new Player());
        arkadeWorld.addPlayer(new Player());
        assertEquals(arkadeWorld.status, EnumStatus.PLAY);
        assertTrue(arkadeWorld.haveAllPlayers());
        assertTrue(arkadeWorld.havePlayers());
        
        arkadeWorld.removePlayer1();
        assertEquals(arkadeWorld.status, EnumStatus.WAIT);
        assertFalse(arkadeWorld.haveAllPlayers());
        assertTrue(arkadeWorld.havePlayers());
        
        // Повторное удаление 1-го игрока не должно ронять приложение
        arkadeWorld.removePlayer1();
        
    }

    /**
     * Тестирование метода removePlayer2(). Должен удалить 2-го игрока, а в случае отсутствия такового не должно случаться ошибок.
     */
    @Test
    public void testRemovePlayer2() {
        System.out.println("removePlayer2");
        
        ArkadeWorld arkadeWorld = new ArkadeWorld(1, 800, 600);
        arkadeWorld.addComponent(new Tile().setLocation(0, 300));
        arkadeWorld.addPlayer(new Player());
        arkadeWorld.addPlayer(new Player());
        assertEquals(arkadeWorld.status, EnumStatus.PLAY);
        assertTrue(arkadeWorld.haveAllPlayers());
        assertTrue(arkadeWorld.havePlayers());
        
        arkadeWorld.removePlayer2();
        assertEquals(arkadeWorld.status, EnumStatus.WAIT);
        assertFalse(arkadeWorld.haveAllPlayers());
        assertTrue(arkadeWorld.havePlayers());
        
        // Повторное удаление 2-го игрока не должно ронять приложение
        arkadeWorld.removePlayer2();
    }
    
}
