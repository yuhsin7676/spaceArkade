package spacearkade.game;

import spacearkade.game.components.Ball;
import spacearkade.game.components.Tile;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import spacearkade.engine.Component;
import spacearkade.engine.World;
import spacearkade.game.components.Bonus;
import spacearkade.game.components.Platform;
import spacearkade.game.components.Tile3;

public class ArkadeWorld extends World{
    
    private Player player1 = null;
    private Player player2 = null;
    protected boolean canBeRemove = false; //Если true, то в следующем update компонент будет удален
    public enumStatus status = enumStatus.WAIT;
    private Map<Integer, Component> balls = new HashMap<Integer, Component>();
    private Map<Integer, Component> tiles = new HashMap<Integer, Component>();
    private Map<Integer, Component> bonuses = new HashMap<Integer, Component>();
    private Map<Integer, Component> platforms = new HashMap<Integer, Component>();
    
    // Конструктор
    public ArkadeWorld(int id, int width, int height) {
        super(id, width, height);
    }

    @Override
    public Component addComponent(Component component) {
        component = super.addComponent(component);
        if (component instanceof Ball)
            this.balls.put(component.getId(), component);
        else if (component instanceof Tile)
            this.tiles.put(component.getId(), component);
        else if (component instanceof Bonus)
            this.bonuses.put(component.getId(), component);
        else if (component instanceof Platform)
            this.platforms.put(component.getId(), component);
        return component;
    }

    @Override
    public void update() {
        // Выполним обновление мира если оба игрока присутствуют
        if(status == enumStatus.PLAY)
            super.update();
        
        // Удаляем шары через явный итератор, иначе будет ошибка ConcurrentModificationException
        Iterator<Map.Entry<Integer, Component>> i = balls.entrySet().iterator();
        while(i.hasNext())
            if(!components.containsKey(i.next().getKey()))
                i.remove();
        
        // Аналогично поступаем с плитками
        i = tiles.entrySet().iterator();
        while(i.hasNext()){
            int key = i.next().getKey();
            if(!components.containsKey(key)){
                
                // Плитка 3 дает бонусы при разрушении
                if(tiles.get(key).getClass() == Tile3.class){
                    Component tile = tiles.get(key);
                    this.addComponent(new Bonus()).setLocation(tile.getLocation()).setVelocity(0, 50);
                    this.addComponent(new Bonus()).setLocation(tile.getLocation()).setVelocity(0, -50);
                }
                i.remove();
                
            }
        }
        
        // Аналогично поступаем с бонусами
        i = bonuses.entrySet().iterator();
        while(i.hasNext())
            if(!components.containsKey(i.next().getKey()))
                i.remove();
        
        // Смотрим, есть ли пересечения между платформой и бонусом
        for(Map.Entry<Integer, Component> bonus : bonuses.entrySet()){
            for(Map.Entry<Integer, Component> platform : platforms.entrySet()){
                Vector2D bonusLocation = bonus.getValue().getLocation();
                Vector2D platformLocation = platform.getValue().getLocation();
                Vector2D bonusSize = bonus.getValue().getSize();
                Vector2D platformSize = platform.getValue().getSize();
                Vector2D difLocation = bonusLocation.subtract(platformLocation);
                Vector2D sumSize = bonusSize.add(platformSize);
                if(Math.abs(difLocation.getX()) <= sumSize.getX()/2 && Math.abs(difLocation.getY()) <= sumSize.getY()/2){
                    bonus.getValue().removed();
                    platform.getValue().setSize(120, 20);
                }  
            }
        }
        
        // Если шаров не осталось, удаляем игроков из мира, так как они проиграли
        if(balls.isEmpty()){
            this.status = enumStatus.LOSE;
            removePlayer1();
            removePlayer2();
        }
        
        // Если плиток не осталось, удаляем игроков из мира, так как они выиграли
        if(tiles.isEmpty()){
            this.status = enumStatus.WIN;
            removePlayer1();
            removePlayer2();
        }
        
    }
    
    /**
     * Добавляет игрока Player в мир. Возвращает номер игрока в данном мире или 0, если места для игрока нет.
     */
    public int addPlayer(Player player){
        if(this.player1 == null){
            player.playerNumber = 1;
            player.object = this.components.get(1);
            this.player1 = player;
            if(this.player2 != null)
                this.status = enumStatus.PLAY;
            player.status = this.status;
            return 1;
        }
        else if(this.player2 == null){
            player.playerNumber = 2;
            player.object = this.components.get(2);
            this.player2 = player;
            if(this.player1 != null) 
                this.status = enumStatus.PLAY;
            player.status = this.status;
            return 2;
        }
        else{
            return 0;
        }
    }
    
    /**
     * Удаляет первого игрока из мира.
     */
    public void removePlayer1(){
        this.player1.worldPointer = null;
        if(this.status == enumStatus.PLAY || this.status == enumStatus.WAIT){
            this.player1.status = enumStatus.NOPLAY;
            this.status = enumStatus.WAIT;
        }
        else
            this.player1.status = this.status;
        this.player1 = null;
        if(this.player2 == null)
            this.canBeRemove = true;
    }
    
    /**
     * Удаляет второго игрока из мира.
     */
    public void removePlayer2(){
        this.player2.worldPointer = null;
        if(this.status == enumStatus.PLAY || this.status == enumStatus.WAIT){
            this.player2.status = enumStatus.NOPLAY;
            this.status = enumStatus.WAIT;
        }
        else
            this.player2.status = this.status;
        this.player2 = null;
        if(this.player1 == null)
            this.canBeRemove = true;
    }
    
    /**
     * Проверяет, все ли игроки есть в мире.
     */
    public boolean haveAllPlayers(){
        return (player1 != null && player2 != null);
    }
    
    /**
     * Проверяет, есть ли хотя бы один игрок в мире.
     */
    public boolean havePlayers(){
        return (player1 != null || player2 != null);
    }
    
}
