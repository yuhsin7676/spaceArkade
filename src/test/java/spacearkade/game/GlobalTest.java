package spacearkade.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GlobalTest {

    /**
     * Тестирование добавления игрока в пустой мап игроков. Проверяется только корректность добавления
     */
    @Test
    public void testCreatePlayer() {
        System.out.println("createPlayer");
        
        Global.mapPlayer.clear();
        Global.createPlayer("1");
        assertNotNull(Global.mapPlayer.get("1"));
        assertNull(Global.mapPlayer.get("0"));
        
    }

    /**
     * Тестирование добавления игрока в какой-нибудь мир. Добавленный игрок, к которому можно обратиться по id, должен содержать непустую ссылку на мир.
     * Мир же должен содержать хотя бы одного игрока.
     */
    @Test
    public void testAddPlayerToWorld() {
        System.out.println("addPlayerToWorld");

        Global.createPlayer("1");
        Global.addPlayerToWorld("1");
        assertNotNull(Global.mapPlayer.get("1").worldPointer);
        ArkadeWorld arkadeWorld = Global.mapPlayer.get("1").worldPointer;
        assertTrue(arkadeWorld.havePlayers());   
    }
    
    /**
     * Тестирование последовательного добавления 2-х игроков мир. 
     * Игроки должны оказаться в одном мире (содержать одинаковые worldPointer) и иметь номера 1 и 2.
     * Мир же должен быть полностью заполненным игроками
     */
    @Test
    public void testAddTwoPlayerToWorldWhereNotWorlds() {
        System.out.println("addTwoPlayerToWorldWhereNotWorlds");

        Global.mapWorld.clear();
        Global.createPlayer("1");
        Global.addPlayerToWorld("1");
        Global.createPlayer("2");
        Global.addPlayerToWorld("2");
        ArkadeWorld arkadeWorld1 = Global.mapPlayer.get("1").worldPointer;
        ArkadeWorld arkadeWorld2 = Global.mapPlayer.get("2").worldPointer;
        
        assertTrue(arkadeWorld1 == arkadeWorld2);
        assertTrue((Global.mapPlayer.get("1").playerNumber == 1 && Global.mapPlayer.get("2").playerNumber == 2) ||
                   (Global.mapPlayer.get("1").playerNumber == 2 && Global.mapPlayer.get("2").playerNumber == 1));
        assertTrue(arkadeWorld1.haveAllPlayers());
    }

    /**
     * Тестирование удаления игрока. После выполнения метода игрока не должно быть в мапе,
     * а мир, в котором он был до этого, не должен быть заполненным.
     */
    @Test
    public void testRemovePlayer() {
        System.out.println("removePlayer");
        
        Global.createPlayer("1");
        Global.addPlayerToWorld("1");
        ArkadeWorld arkadeWorld = Global.mapPlayer.get("1").worldPointer;
        
        Global.removePlayer("1");
        assertNull(Global.mapPlayer.get("1"));
        assertFalse(arkadeWorld.haveAllPlayers());
        
    }

    /**
     * Тестирование удаления игрока из мира. После выполнения метода игрок не должен ссылаться ни на какой мир, а мир,
     * из которого он удалился, не должен быть заполненным.
     */
    @Test
    public void testRemovePlayerFromWorld() {
        System.out.println("removePlayerFromWorld");
        
        Global.createPlayer("1");
        Global.addPlayerToWorld("1");
        ArkadeWorld arkadeWorld = Global.mapPlayer.get("1").worldPointer;
        
        Global.removePlayerFromWorld("1");
        assertNull(Global.mapPlayer.get("1").worldPointer);
        assertFalse(arkadeWorld.haveAllPlayers());
    }

    /**
     * Тестирование метода update(), который должен запускать update() во всех мирах и удалять их, если там нет игроков.
     * В данном тесте проверяется лишь второе.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        
        Global.mapWorld.clear();
        Global.createPlayer("1");
        Global.addPlayerToWorld("1");
        ArkadeWorld arkadeWorld = Global.mapPlayer.get("1").worldPointer;
        assertTrue(arkadeWorld.havePlayers());
        assertFalse(arkadeWorld.haveAllPlayers());
        assertEquals(Global.mapWorld.size(), 1);
        
        Global.update();
        assertEquals(Global.mapWorld.size(), 1);
        
        Global.removePlayerFromWorld("1");
        Global.update();
        assertEquals(Global.mapWorld.size(), 0);
        
    }
    
}
