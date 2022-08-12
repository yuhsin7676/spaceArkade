package spacearkade.game;

import spacearkade.game.components.Ball;
import spacearkade.game.components.Tile;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import spacearkade.engine.Component;
import spacearkade.engine.World;
import spacearkade.game.components.AntiBonus1;
import spacearkade.game.components.AntiBonus2;
import spacearkade.game.components.AntiBonus3;
import spacearkade.game.components.AntiBonus4;
import spacearkade.game.components.Bonus1;
import spacearkade.game.components.Bonus2;
import spacearkade.game.components.Bonus3;
import spacearkade.game.components.Bonus4;
import spacearkade.game.components.Tile3;

public class ArkadeWorld extends World{
    
    private Player player1 = null;
    private Player player2 = null;
    protected boolean canBeRemove = false; //Если true, то в следующем update компонент будет удален
    public EnumStatus status = EnumStatus.WAIT;
    private Map<Integer, Component> balls = new HashMap<Integer, Component>();
    private Map<Integer, Component> tiles = new HashMap<Integer, Component>();
    
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
        return component;
    }

    @Override
    public void update() {
        // Выполним обновление мира если оба игрока присутствуют
        if(status == EnumStatus.PLAY)
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
                    int number = 1 + new Random().nextInt(8);
                    if(number == 1){
                        this.addComponent(new Bonus1().setWorldPointer(this)).setLocation(tile.getLocation()).setVelocity(0, 50);
                        this.addComponent(new Bonus1().setWorldPointer(this)).setLocation(tile.getLocation()).setVelocity(0, -50);
                    }
                    else if(number == 2){
                        this.addComponent(new Bonus2().setWorldPointer(this).setComponentsPointer(balls)).setLocation(tile.getLocation()).setVelocity(0, 50);
                        this.addComponent(new Bonus2().setWorldPointer(this).setComponentsPointer(balls)).setLocation(tile.getLocation()).setVelocity(0, -50);
                    }
                    else if(number == 3){
                        this.addComponent(new Bonus3().setWorldPointer(this).setComponentsPointer(balls)).setLocation(tile.getLocation()).setVelocity(0, 50);
                        this.addComponent(new Bonus3().setWorldPointer(this).setComponentsPointer(balls)).setLocation(tile.getLocation()).setVelocity(0, -50);
                    }
                    else if(number == 4){
                        this.addComponent(new Bonus4().setWorldPointer(this).setComponentsPointer(balls)).setLocation(tile.getLocation()).setVelocity(0, 50);
                        this.addComponent(new Bonus4().setWorldPointer(this).setComponentsPointer(balls)).setLocation(tile.getLocation()).setVelocity(0, -50);
                    }
                    else if(number == 5){
                        this.addComponent(new AntiBonus1().setWorldPointer(this)).setLocation(tile.getLocation()).setVelocity(0, 50);
                        this.addComponent(new AntiBonus1().setWorldPointer(this)).setLocation(tile.getLocation()).setVelocity(0, -50);
                    }
                    else if(number == 6){
                        this.addComponent(new AntiBonus3().setWorldPointer(this).setComponentsPointer(balls)).setLocation(tile.getLocation()).setVelocity(0, 50);
                        this.addComponent(new AntiBonus3().setWorldPointer(this).setComponentsPointer(balls)).setLocation(tile.getLocation()).setVelocity(0, -50);
                    }
                    else if(number == 7){
                        this.addComponent(new AntiBonus3().setWorldPointer(this).setComponentsPointer(balls)).setLocation(tile.getLocation()).setVelocity(0, 50);
                        this.addComponent(new AntiBonus3().setWorldPointer(this).setComponentsPointer(balls)).setLocation(tile.getLocation()).setVelocity(0, -50);
                    }
                    else if(number == 8){
                        this.addComponent(new AntiBonus4().setWorldPointer(this).setComponentsPointer(balls)).setLocation(tile.getLocation()).setVelocity(0, 50);
                        this.addComponent(new AntiBonus4().setWorldPointer(this).setComponentsPointer(balls)).setLocation(tile.getLocation()).setVelocity(0, -50);
                    }
                }
                i.remove();
                
            }
        }
        
        // Если шаров не осталось, удаляем игроков из мира, так как они проиграли
        if(balls.isEmpty()){
            this.status = EnumStatus.LOSE;
            removePlayer1();
            removePlayer2();
        }
        
        // Если плиток не осталось, удаляем игроков из мира, так как они выиграли
        if(tiles.isEmpty()){
            this.status = EnumStatus.WIN;
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
                this.status = EnumStatus.PLAY;
            player.status = this.status;
            return 1;
        }
        else if(this.player2 == null){
            player.playerNumber = 2;
            player.object = this.components.get(2);
            this.player2 = player;
            if(this.player1 != null) 
                this.status = EnumStatus.PLAY;
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
        if(this.status == EnumStatus.PLAY || this.status == EnumStatus.WAIT){
            this.player1.status = EnumStatus.NOPLAY;
            this.status = EnumStatus.WAIT;
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
        if(this.status == EnumStatus.PLAY || this.status == EnumStatus.WAIT){
            this.player2.status = EnumStatus.NOPLAY;
            this.status = EnumStatus.WAIT;
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
