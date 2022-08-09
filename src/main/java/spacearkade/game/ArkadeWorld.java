package spacearkade.game;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import spacearkade.engine.Component;
import spacearkade.engine.World;

public class ArkadeWorld extends World{
    
    private boolean player1 = false;
    private boolean player2 = false;
    protected boolean canBeRemove = false; //Если true, то в следующем update компонент будет удален
    public Map<Integer, Component> balls = new HashMap<Integer, Component>();
    
    // Конструктор
    public ArkadeWorld(int id, int width, int height) {
        super(id, width, height);
    }

    @Override
    public Component addComponent(Component component) {
        component = super.addComponent(component);
        if (component.getClassName().equals("Ball"))
            balls.put(component.getId(), component);
        return component;
    }

    @Override
    public void update() {
        // Выполним обновление мира если оба игрока присутствуют
        if(player1 && player2)
            super.update();
        
        // Удаляем через явный итератор, иначе будет ошибка ConcurrentModificationException
        Iterator<Map.Entry<Integer, Component>> i = balls.entrySet().iterator();
        while(i.hasNext())
            if(!components.containsKey(i.next().getKey()))
                i.remove();
        
        // Если шаров не осталось, удаляем игроков из мира, так как они проиграли
        if(balls.isEmpty()){
            removePlayer1();
            removePlayer2();
        }
        
    }
    
    /**
     * Добавляет игрока Player в мир. Возвращает номер игрока в данном мире или 0, если места для игрока нет.
     */
    public int addPlayer(Player player){
        if(this.player1 == false){
            player.playerNumber = 1;
            player.object = this.components.get(1);
            this.player1 = true;
            return 1;
        }
        else if(this.player2 == false){
            player.playerNumber = 2;
            player.object = this.components.get(2);
            this.player2 = true;
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
        player1 = false;
        if(!player2)
            this.canBeRemove = true;
    }
    
    /**
     * Удаляет второго игрока из мира.
     */
    public void removePlayer2(){
        player2 = false;
        if(!player1)
            this.canBeRemove = true;
    }
    
    /**
     * Проверяет, все ли игроки есть в мире.
     */
    public boolean haveAllPlayers(){
        return (player1 && player2);
    }
    
    /**
     * Проверяет, есть ли хотя бы один игрок в мире.
     */
    public boolean havePlayers(){
        return (player1 || player2);
    }
    
}
