package spacearkade.engine;

import static java.lang.Math.sqrt;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class World {
    
    // Переменные
    private int width;
    private int height;
    private int primary = 0;
    public boolean player1 = false;
    public boolean player2 = false;
    public Map<Integer, Component> components = new HashMap<Integer, Component>();
    public double n = 1; // Точность расчета коллизий
    public int frame = 60; // Учет количества кадров

    // Конструктор
    public World(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    ////////////////// Функции /////////////////////////

    //
    public Component addComponent(Component component){
        
        component.id = this.primary;
        components.put(this.primary, component);
        this.primary++;
        return component;
        
    }

    //
    public int deleteComponent(int id){
        components.remove(id);
        return primary;
    }
    
    // Удаление всех объектов в мире
    public void clearWorld(){
        components.clear();
    };
    
    //
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
    
    //
    public void removePlayer1(){
        player1 = false;
    }
    
    //
    public void removePlayer2(){
        player2 = false;
    }
    
    /////////////////////////////////////////
    //
    // Ниже идет физика
    //
    /////////////////////////////////////////
    
    
    // Пересчет координат всех объектов и проверка событий мыши
    public void update(){

        // Обнуление eventHit
        for(Map.Entry<Integer, Component> entry : components.entrySet())
            entry.getValue().eventHit = new ArrayList<EventHit>();

        // Проверка столкновений акторов и пересчет координат по закону сохранения импульса
        for(int k = 0; k < n; k++){
            for(Map.Entry<Integer, Component> component : components.entrySet()){
                
                if(!component.getValue().isStaticComponent){
                    relationshipWorld(component.getValue());
                    for(Map.Entry<Integer, Component> component2 : components.entrySet()){
                        if(component2.getValue().isStaticComponent)
                            relationshipObjects(component.getValue(), component2.getValue());
                        else if(!component.getValue().collision && !component.getValue().equals(component2.getValue()))
                            relationshipObjects(component.getValue(), component2.getValue());
                    }
                    component.getValue().location = component.getValue().location.add(component.getValue().d);
                }
                
            }

            // Сброс свойств collision (что это такое, описано ниже)
            for(Map.Entry<Integer, Component> component : components.entrySet())
                component.getValue().collision = false;
        }
        
        // Удаляем через явный итератор, иначе будет ошибка ConcurrentModificationException
        Iterator<Entry<Integer, Component>> i = components.entrySet().iterator();
        while(i.hasNext())
            if(i.next().getValue().canBeRemove)
                i.remove();

    }    
    
    //
    void relationshipWorld(Component dynamicCombonent){
			
        double cx = dynamicCombonent.location.getX();
        double cy = dynamicCombonent.location.getY();
        double vx = dynamicCombonent.velocity.getX()/(n*frame);
        double vy = dynamicCombonent.velocity.getY()/(n*frame);
        double w,h;
        if(dynamicCombonent.isCircle) w = h = 2*dynamicCombonent.r;
        else{
            w = dynamicCombonent.size.getX();
            h = dynamicCombonent.size.getY();
        }

        // Столкновение с краем мира по x
        if (cx + vx < w/2){
            dynamicCombonent.velocity = dynamicCombonent.velocity.subtract(2, new Vector2D(dynamicCombonent.velocity.getX(), 0));
            dynamicCombonent.d = new Vector2D(-vx + w - 2*cx, dynamicCombonent.d.getY());
            dynamicCombonent.collision = true;

            writeEventHitWithWorld(dynamicCombonent);
        }
        else if (cx + vx > this.width - w/2){
            dynamicCombonent.velocity = dynamicCombonent.velocity.subtract(2, new Vector2D(dynamicCombonent.velocity.getX(), 0));
            dynamicCombonent.d = new Vector2D(-vx + 2*(this.width - w/2) - 2*cx, dynamicCombonent.d.getY());
            dynamicCombonent.collision = true;

            writeEventHitWithWorld(dynamicCombonent);
        }
        else{
            dynamicCombonent.d = new Vector2D(vx, dynamicCombonent.d.getY());
        }

        // Столкновение с краем мира по y
        if (cy + vy < h/2){
            dynamicCombonent.velocity = dynamicCombonent.velocity.subtract(2, new Vector2D(0, dynamicCombonent.velocity.getY()));
            dynamicCombonent.d = new Vector2D(dynamicCombonent.d.getX(), -vy + h - 2*cy);
            dynamicCombonent.collision = true;

            writeEventHitWithWorld(dynamicCombonent);
        }
        else if (cy + vy > this.height - h/2){
            dynamicCombonent.velocity = dynamicCombonent.velocity.subtract(2, new Vector2D(0, dynamicCombonent.velocity.getY()));
            dynamicCombonent.d = new Vector2D(dynamicCombonent.d.getX(), -vy + 2*(this.height - h/2) - 2*cy);
            dynamicCombonent.collision = true;

            writeEventHitWithWorld(dynamicCombonent);
        }
        else{
            dynamicCombonent.d = new Vector2D(dynamicCombonent.d.getX(), vy);
        }
        
    }
    
    // Коллизии объектов друг с другом
    void relationshipObjects(Component a1, Component a2){
        if (!a1.collision && (!a2.collision || a2.isStaticComponent) && a1.enableCollision && a2.enableCollision){

            // Столкновение 2 окружностей
            if (a1.isCircle && a2.isCircle)
                circleCircleRelation(a1, a2);

            // Столкновение окружности и прямоугольника
            else if ((a1.isCircle && !a2.isCircle) || (!a1.isCircle && a2.isCircle))
                squareCircleRelation(a1, a2);

            // Столкновение 2 прямоугольников
            else if (!a1.isCircle && !a2.isCircle)
                squareSquareRelation(a1, a2);
        }
    }
    
    /////////////////
    
    private void writeEventHits(Component a1, Component a2){
        EventHit eventHit1 = new EventHit();
        EventHit eventHit2 = new EventHit();
        eventHit1.id = a2.id;
        eventHit2.id = a1.id;
        eventHit1.typeName = a2.typeName;
        eventHit2.typeName = a1.typeName;
        eventHit1.className = a2.getClassName();
        eventHit2.className = a1.getClassName();
        eventHit1.velocity = a2.velocity;
        eventHit2.velocity = a1.velocity;
        eventHit1.location = a2.location;
        eventHit2.location = a1.location;
        eventHit1.size = a2.size;
        eventHit2.size = a1.size;
        a1.eventHit.add(eventHit1);
        a2.eventHit.add(eventHit2);
        a1.eventHitListener();
        a2.eventHitListener();
    }
    
    private void writeEventHitWithWorld(Component a){
        EventHit eventHit = new EventHit();
        eventHit.id = 0;
        eventHit.typeName = "World";
        eventHit.className = "World";
        a.eventHit.add(eventHit);
    }
    
    private void changeVelocityAndCalcD(Component a1, Component a2, Vector2D v1, Vector2D v2, double part){
        
        Vector2D v10 = a1.velocity.scalarMultiply(1/n/frame);
        Vector2D v20 = a2.velocity.scalarMultiply(1/n/frame);
        a1.velocity = v1.scalarMultiply(n*frame);
        a2.velocity = v2.scalarMultiply(n*frame);

        a1.d = v10.scalarMultiply(part).add(1-part, v1);
        a2.d = v20.scalarMultiply(part).add(1-part, v2);
        a1.collision = true;
        a2.collision = true;
    }
    
    private double[] howAxisIntersection(Component a1, Component a2){
    
        Vector2D location10 = a1.location;
        Vector2D location20 = a2.location; 
        Vector2D velocity10 = a1.velocity.scalarMultiply(1/n/frame); 
        Vector2D velocity20 = a2.velocity.scalarMultiply(1/n/frame); 
        double w1 = a1.size.getX();
        double w2 = a2.size.getX();
        double h1 = a1.size.getY();
        double h2 = a2.size.getY();
        
        double partX = 1; 
        double partY = 1;
        
        /////// По какой оси пересечение раньше? ////////////
        // Слева или справа
        if( location10.getX() <= location20.getX() )
            partX = (w1/2 + w2/2 + location10.getX() - location20.getX())/(velocity20.getX() - velocity10.getX());
        else if( location10.getX() > location20.getX() )
            partX = (w1/2 + w2/2 + location20.getX() - location10.getX())/(velocity10.getX() - velocity20.getX());

        // Либо сверху или снизу
        if( location10.getY() <= location20.getY() )
            partY = (h1/2 + h2/2 + location10.getY() - location20.getY())/(velocity20.getY() - velocity10.getY());
        else if( location10.getY() >= location20.getY() )
            partY = (h1/2 + h2/2 + location20.getY() - location10.getY())/(velocity10.getY() - velocity20.getY());
        
        // Обозначим возможные точки соударения
        Vector2D c2x = location20.add(partX, velocity20);
        Vector2D c1x = location10.add(partX, velocity10);

        Vector2D c2y = location20.add(partY, velocity20);
        Vector2D c1y = location10.add(partY, velocity10);

        // Там ли произошли пересечения?
        if (!((c2x.getY() + h2/2 >= c1x.getY() - h1/2) && (c2x.getY() - h2/2 <= c1x.getY() + h1/2))) partX = 2;
        if (!((c2y.getX() + w2/2 >= c1y.getX() - w1/2) && (c2y.getX() - w2/2 <= c1y.getX() + w1/2))) partY = 2;
        
        double[] parts = {partX, partY};
        return parts;
        
    }
    
    private Vector2D[] newVelocitiesByHorizontalIntersection(Component a1, Component a2){
        
        double m1 = a1.m, m2 = a2.m;
        Vector2D v10 = a1.velocity.scalarMultiply(1/n/frame);
        Vector2D v20 = a2.velocity.scalarMultiply(1/n/frame); 
        
        // Изменяем скорости
        Vector2D v1 = new Vector2D(0, 0);
        Vector2D v2 = new Vector2D(0, 0);

        if(a1.infinityMass && a2.infinityMass && !a2.isStaticComponent){
            v1 = new Vector2D(v20.getX(), v10.getY());
            v2 = new Vector2D(v10.getX(), v20.getY());
        }
        else if(a1.infinityMass && !a2.infinityMass){
            v1 = v10;
            v2 = new Vector2D(2*v10.getX() - v20.getX(), v20.getY());
        }
        else if(!a1.infinityMass && a2.infinityMass){
            v1 = new Vector2D(2*v20.getX() - v10.getX(), v10.getY());
            v2 = v20;
        }
        else if(!a1.infinityMass && !a2.infinityMass){
            v1 = new Vector2D((v10.getX()*(m1/m2-1) + v20.getX()*2)/(1+m1/m2), v10.getY());
            v2 = new Vector2D((v20.getX()*(m2/m1-1) + v10.getX()*2)/(1+m2/m1), v20.getY());
        }
        
        Vector2D[] newVelocities = {v1, v2};
        return newVelocities;
        
    }
    
    private Vector2D[] newVelocitiesByVerticalIntersection(Component a1, Component a2){
        
        double m1 = a1.m, m2 = a2.m;
        Vector2D v10 = a1.velocity.scalarMultiply(1/n/frame);
        Vector2D v20 = a2.velocity.scalarMultiply(1/n/frame); 
        
        // Изменяем скорости
        Vector2D v1 = new Vector2D(0, 0);
        Vector2D v2 = new Vector2D(0, 0);

        if(a1.infinityMass && a2.infinityMass && !a2.isStaticComponent){
            v1 = new Vector2D(v10.getX(), v20.getY());
            v2 = new Vector2D(v20.getX(), v10.getY());
        }
        else if(a1.infinityMass && !a2.infinityMass){
            v1 = v10;
            v2 = new Vector2D(v20.getX(), 2*v10.getY() - v20.getY());
        }
        else if(!a1.infinityMass && a2.infinityMass){
            v1 = new Vector2D(v10.getX(), 2*v20.getY() - v10.getY());
            v2 = v20;
        }
        else{
            v1 = new Vector2D(v10.getX(), (v10.getY()*(m1/m2-1) + v20.getY()*2)/(1+m1/m2));
            v2 = new Vector2D(v20.getX(), (v20.getY()*(m2/m1-1) + v10.getY()*2)/(1+m2/m1));
        }
        
        Vector2D[] newVelocities = {v1, v2};
        return newVelocities;
        
    }
    
    private void squaresOrSquareCircleRelationHelp(Component a1, Component a2){
        
        double[] parts = howAxisIntersection(a1, a2);
        double partX = parts[0];
        double partY = parts[1];
        double part;

        Vector2D[] newVelocities;

        // Пересечение слева или справа!
        if(partX < partY){
            part = partX;
            newVelocities = newVelocitiesByHorizontalIntersection(a1, a2);
        }

        // Пересечение сверху или снизу!
        else{ // if (partX >= partY){
            part = partY;
            newVelocities = newVelocitiesByVerticalIntersection(a1, a2);
        }

        Vector2D v1 = newVelocities[0];
        Vector2D v2 = newVelocities[1]; 
        changeVelocityAndCalcD(a1, a2, v1, v2, part);
        writeEventHits(a1, a2);
        
    }
    
    /////////////////
    
    private void squareSquareRelation(Component a1, Component a2){
        
        double w1 = a1.size.getX(), w2 = a2.size.getX(), h1 = a1.size.getY(), h2 = a2.size.getY(); 
        Vector2D c10 = a1.location, c20 = a2.location;
        Vector2D v10 = a1.velocity.scalarMultiply(1/n/frame), v20 = a2.velocity.scalarMultiply(1/n/frame);

        // Определим, есть ли соударение
        if(abs((c10.getX() + v10.getX()) - (c20.getX() + v20.getX())) < (w1/2 + w2/2) && abs((c10.getY() + v10.getY()) - (c20.getY() + v20.getY())) < (h1/2 + h2/2)){

            squaresOrSquareCircleRelationHelp(a1, a2);
        }
        else{
            a1.d = a1.velocity.scalarMultiply(1/n/frame);
            a2.d = a2.velocity.scalarMultiply(1/n/frame);
        }
        
    }
    
    private void squareCircleRelation(Component a1, Component a2){
        
        // Определим, кто круг, а кто прямоугольник
        Component aCirc;
        Component aRect;
        if(a1.isCircle){ 
            aCirc = a1; 
            aRect = a2;
        }
        else{
            aCirc = a2;
            aRect = a1;
        }

        double w = aRect.size.getX();
        double h = aRect.size.getY();  // Размеры прямоугольника
        double r = aCirc.r;                          // Размеры круга
        Vector2D Cc0 = aCirc.location;                   // Начальная координата круга
        Vector2D Vc0 = aCirc.velocity.scalarMultiply(1/n/frame);              // Начальная скорость круга
        Vector2D Cr0 = aRect.location;                   // Начальная координата прямоугольника
        Vector2D Vr0 = aRect.velocity.scalarMultiply(1/n/frame);              // Начальная скорость прямоугольника

        // Определим, есть ли соударение
        if(abs((Cc0.getX() + Vc0.getX()) - (Cr0.getX() + Vr0.getX())) < (r + w/2) && abs((Cc0.getY() + Vc0.getY()) - (Cr0.getY() + Vr0.getY())) < (r + h/2)){

            squaresOrSquareCircleRelationHelp(a1, a2);
            
        }
        else{
            a1.d = a1.velocity.scalarMultiply(1/n/frame);
            a2.d = a2.velocity.scalarMultiply(1/n/frame);
        }
        
    }
    
    private void circleCircleRelation(Component a1, Component a2){
        
        // Столкновение 2 окружностей
        double dr = a1.r + a2.r;
        double m1 = a1.m, m2 = a2.m;

        // Обозначим скорости
        Vector2D v10 = a1.velocity.scalarMultiply(1/n/frame);
        Vector2D v20 = a2.velocity.scalarMultiply(1/n/frame);

        if (a1.location.add(v10).subtract(a2.location.add(v20)).getNorm() < dr){ // Что дает getNorm, надо 

            // Находим точку соударения(вернее,a - часть пути до соударения???)
            double cx = a2.location.getX() - a1.location.getX();
            double cy = a2.location.getY() - a1.location.getY();
            double vx = v20.getX() - v10.getX();
            double vy = v20.getY() - v10.getY(); 
            double part;
            if(vx != 0 || vy != 0){
                double D = -(cy*vx - cx*vy)*(cy*vx - cx*vy) + dr*dr*(vx*vx + vy*vy);
                if (D < 0) D = 0;
                part = ( - (cx*vx + cy*vy) - sqrt(D))/(vx*vx + vy*vy);
            }
            else{
                part = 0.99;
            }

            // Обозначим точки удара
            Vector2D coor1 = a1.location.add(part, v10);
            Vector2D coor2 = a2.location.add(part, v20);

            // Находим нормальные и тангенсальные составляющие скоростей
            Vector2D vOs = coor2.subtract(coor1);
            Vector2D vn1 = new Vector2D(0, 0);
            Vector2D vn2 = new Vector2D(0, 0);
            Vector2D vt1 = new Vector2D(0, 0);
            Vector2D vt2 = new Vector2D(0, 0);
            Vector2D v1 = new Vector2D(0, 0);
            Vector2D v2 = new Vector2D(0, 0);
            if (v10.getNorm() != 0){
                double cos1 = v10.dotProduct(vOs)/( v10.getNorm()*vOs.getNorm() ); // Скалярное произведением векторов?
                vn1 = vOs.scalarMultiply(v10.getNorm() * cos1/vOs.getNorm());
                vt1 = v10.subtract(vn1);
            }
            else{
                vn1 = vt1 = new Vector2D(0, 0);
            }

            if(v20.getNorm() != 0){
                double cos2 = v20.dotProduct(vOs)/(v20.getNorm() * vOs.getNorm());
                vn2 = vOs.scalarMultiply(v20.getNorm() * cos2/vOs.getNorm());
                vt2 = v20.subtract(vn2);
            }
            else{
                vn2 = vt2 = new Vector2D(0, 0);
            }

            // Изменяем скорости
            if(a1.infinityMass && a2.infinityMass && !a2.isStaticComponent){
                v1 = vn2.add(vt1);
                v2 = vn1.add(vt2);
            }
            else if(a1.infinityMass && !a2.infinityMass){
                v1 = vn1.add(vt1);
                v2 = vn1.scalarMultiply(2).subtract(vn2).add(vt2);
            }
            else if(!a1.infinityMass && a2.infinityMass){
                v1 = vn2.scalarMultiply(2).subtract(vn1).add(vt1);
                v2 = vn2.add(vt2);
            }
            else if(!a1.infinityMass && !a2.infinityMass){
                v1 = (vn1.scalarMultiply(m1/m2-1).add(2, vn2)).scalarMultiply(m2/(1+m1)).add(vt1);
                v2 = (vn2.scalarMultiply(m2/m1-1).add(2, vn1)).scalarMultiply(m1/(1+m2)).add(vt2);
            }
            
            changeVelocityAndCalcD(a1, a2, v1, v2, part);
            writeEventHits(a1, a2);

        }
        else{
            a1.d = a1.velocity.scalarMultiply(1/n/frame);
            a2.d = a2.velocity.scalarMultiply(1/n/frame);
        }
        
    }
    
}
