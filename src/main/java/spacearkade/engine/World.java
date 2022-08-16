package spacearkade.engine;

import static java.lang.Math.sqrt;
import static java.lang.Math.abs;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class World {
    
    // Переменные
    private final int width;
    private final int height;
    public final int id;
    private int primary = 0;
    private ArrayDeque<Component> addQueue = new ArrayDeque<Component>();
    private ArrayList<String> sounds = new ArrayList<String>();
    protected Map<Integer, Component> components = new HashMap<Integer, Component>();
    protected double n = 1; // Точность расчета коллизий
    protected int frame = 60; // Учет количества кадров

    /**
     * Создает мир c заданными размерами.
     * @param id - id создаваемого мира
     * @param width - ширина мира. Если width <= 0, то мир бесконечный по оси x
     * @param height - высота мира. Если height <= 0, то мир бесконечный по оси y
     */
    public World(int id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
    }
    
    ////////////////// Методы /////////////////////////

    /**
     * Добавляет Component в мир. Возвращает ссылку на него.
     */
    public Component addComponent(Component component){
        
        component.id = this.primary;
        component.world = this;
        components.put(this.primary, component);
        this.primary++;
        return component;
        
    }
    
    /**
     * Добавляет Component в очередь для вставки в мир.
     * Возвращает ссылку на него.
     * Чтобы перекинуть компонент из очереди в сам мир, необходимо вызвать commitAddQueue().
     */
    public Component addComponentToQueue(Component component){
        addQueue.addLast(component);
        return component;
    }
    
    /**
     * Добавляет компоненты из очереди в мир.
     * Очередь очищается
     */
    public void commitAddQueue(){
        while(addQueue.peek()!=null)
            this.addComponent(addQueue.pop());
    }

    /**
     * Возвращает компонент с выбранным id или null в случае отсутствия такового.
     * @param id - id выбираемого Component
     */
    public Component getComponent(int id){
        return components.get(id);
    }
    
    /**
     * Удаляет Component с выбранным id из мира.
     * @param id - id удаляемого Component
     */
    public void deleteComponent(int id){
        components.remove(id);
    }
    
    /**
     * Удаляет все объекты Component в мире.
     */
    public void clearWorld(){
        components.clear();
    };
    
    /**
     * Добавляет звуки.
     */
    public void addSound(String strSound){
        this.sounds.add(strSound);
    }
    
    /**
     * Очищает массив звуков.
     */
    public void clearSounds(){
        this.sounds.clear();
    }
    
    /**
     * Выполняет действия каждый кадр.
     */
    public void update(){
        clearSounds();
        calculateLocationsVelocities();
        commitAddQueue();
    }
    
    /////////////////////////////////////////
    //
    // Ниже идет физика
    //
    /////////////////////////////////////////
    
    // Пересчитывает координаты и скорости всех Component.
    private final void calculateLocationsVelocities(){

        // Обнуление eventHit
        for(Map.Entry<Integer, Component> entry : components.entrySet()){
            entry.getValue().eventHit = new ArrayList<Event>();
            entry.getValue().eventIntersection = new ArrayList<Event>();
        }

        // Проверка столкновений акторов и пересчет координат по закону сохранения импульса
        for(int k = 0; k < n; k++){
            for(Map.Entry<Integer, Component> component : components.entrySet()){
                
                // Пересчитаем координаты для нестатических компонентов
                if(!component.getValue().isStaticComponent){
                    
                    // Если для компонента столкновения еще не обнаружено, то начальное смещение ставим равным скорости, помноженной на время.
                    if(!component.getValue().collision)
                        component.getValue().d = component.getValue().velocity.scalarMultiply(1/n/frame);
                    
                    // Если мир имеет положительные размеры, рассмотрим взаимодействие компонента с его краем
                    if(this.width > 0 && this.height > 0)
                        relationshipWorld(component.getValue());
                    
                    // Рассмотрим взаимодействие компонента с другими компонентами
                    for(Map.Entry<Integer, Component> component2 : components.entrySet())
                        if(component2.getValue().isStaticComponent || !component2.getValue().calculated && !component.getValue().collision && !component.getValue().equals(component2.getValue()))
                            relationshipObjects(component.getValue(), component2.getValue());
                        
                    // Изменим местоположение компонента после всех взаимодействий
                    component.getValue().location = component.getValue().location.add(component.getValue().d);
                    component.getValue().calculated = true;
                    
                }
                
            }

            // Сброс свойств collision и calculated (что это такое, описано ниже)
            for(Map.Entry<Integer, Component> component : components.entrySet())
                component.getValue().collision = component.getValue().calculated = false;
        }
        
        // Удаляем через явный итератор, иначе будет ошибка ConcurrentModificationException
        Iterator<Entry<Integer, Component>> i = components.entrySet().iterator();
        while(i.hasNext())
            if(i.next().getValue().canBeRemove)
                i.remove();
        
        // вызовем update() у компонентов
        for(Map.Entry<Integer, Component> entry : components.entrySet())
            entry.getValue().update();

    }    
    
    // Проверка пересечений объекта с миром
    private void relationshipWorld(Component dynamicCombonent){
	
        if(dynamicCombonent.enableRelation.ordinal() >= EnableRelation.ONLY_INTERSECTION.ordinal()){
        
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
            if (this.width > 0){
                if (cx + vx < w/2){
                    if(dynamicCombonent.enableRelation == EnableRelation.COLLISION){
                        dynamicCombonent.velocity = dynamicCombonent.velocity.subtract(2, new Vector2D(dynamicCombonent.velocity.getX(), 0));
                        dynamicCombonent.d = new Vector2D(-vx + w - 2*cx, dynamicCombonent.d.getY());
                        dynamicCombonent.collision = true;
                        writeEventHitWithWorld(dynamicCombonent);
                    }
                    writeEventIntersectionWithWorld(dynamicCombonent);
                }
                else if (cx + vx > this.width - w/2){
                    if(dynamicCombonent.enableRelation == EnableRelation.COLLISION){
                        dynamicCombonent.velocity = dynamicCombonent.velocity.subtract(2, new Vector2D(dynamicCombonent.velocity.getX(), 0));
                        dynamicCombonent.d = new Vector2D(-vx + 2*(this.width - w/2) - 2*cx, dynamicCombonent.d.getY());
                        dynamicCombonent.collision = true;
                        writeEventHitWithWorld(dynamicCombonent);
                    }
                    writeEventIntersectionWithWorld(dynamicCombonent);
                }
            }

            // Столкновение с краем мира по y
            if (this.height > 0){
                if (cy + vy < h/2){
                    if(dynamicCombonent.enableRelation == EnableRelation.COLLISION){
                        dynamicCombonent.velocity = dynamicCombonent.velocity.subtract(2, new Vector2D(0, dynamicCombonent.velocity.getY()));
                        dynamicCombonent.d = new Vector2D(dynamicCombonent.d.getX(), -vy + h - 2*cy);
                        dynamicCombonent.collision = true;
                        writeEventHitWithWorld(dynamicCombonent);
                    }
                    writeEventIntersectionWithWorld(dynamicCombonent);
                }
                else if (cy + vy > this.height - h/2){
                    if(dynamicCombonent.enableRelation == EnableRelation.COLLISION){
                        dynamicCombonent.velocity = dynamicCombonent.velocity.subtract(2, new Vector2D(0, dynamicCombonent.velocity.getY()));
                        dynamicCombonent.d = new Vector2D(dynamicCombonent.d.getX(), -vy + 2*(this.height - h/2) - 2*cy);
                        dynamicCombonent.collision = true;
                        writeEventHitWithWorld(dynamicCombonent);
                    }
                    writeEventIntersectionWithWorld(dynamicCombonent);
                }
            }
            
        }
        
    }
    
    // Проверка пересечений объектов друг с другом
    private void relationshipObjects(Component a1, Component a2){
        if (!a1.collision && (!a2.collision || a2.isStaticComponent) && 
             a1.enableRelation.ordinal() >= EnableRelation.ONLY_INTERSECTION.ordinal() &&
             a2.enableRelation.ordinal() >= EnableRelation.ONLY_INTERSECTION.ordinal()){

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
        a1.collision = a2.collision = true;
        Event eventHit1 = new Event();
        Event eventHit2 = new Event();
        eventHit1.id = a2.id;
        eventHit2.id = a1.id;
        eventHit1.typeName = a2.typeName;
        eventHit2.typeName = a1.typeName;
        eventHit1.className = a2.className;
        eventHit2.className = a1.className;
        eventHit1.velocity = a2.velocity;
        eventHit2.velocity = a1.velocity;
        eventHit1.location = a2.location;
        eventHit2.location = a1.location;
        eventHit1.size = a2.size;
        eventHit2.size = a1.size;
        a1.eventHit.add(eventHit1);
        a2.eventHit.add(eventHit2);
        a1.eventHitListener(eventHit1);
        a2.eventHitListener(eventHit2);
    }
    
    private void writeEventHitWithWorld(Component a){
        Event eventHit = new Event();
        eventHit.id = 0;
        eventHit.typeName = "World";
        eventHit.className = "World";
        a.eventHit.add(eventHit);
        a.eventHitListener(eventHit);
    }
    
    private void writeEventIntersections(Component a1, Component a2){
        Event eventIntersection1 = new Event();
        Event eventIntersection2 = new Event();
        eventIntersection1.id = a2.id;
        eventIntersection2.id = a1.id;
        eventIntersection1.typeName = a2.typeName;
        eventIntersection2.typeName = a1.typeName;
        eventIntersection1.className = a2.className;
        eventIntersection2.className = a1.className;
        eventIntersection1.velocity = a2.velocity;
        eventIntersection2.velocity = a1.velocity;
        eventIntersection1.location = a2.location;
        eventIntersection2.location = a1.location;
        eventIntersection1.size = a2.size;
        eventIntersection2.size = a1.size;
        a1.eventIntersection.add(eventIntersection1);
        a2.eventIntersection.add(eventIntersection2);
        a1.eventIntersectionListener(eventIntersection1);
        a2.eventIntersectionListener(eventIntersection2);
    }
    
    private void writeEventIntersectionWithWorld(Component a){
        Event eventIntersection = new Event();
        eventIntersection.id = 0;
        eventIntersection.typeName = "World";
        eventIntersection.className = "World";
        a.eventIntersection.add(eventIntersection);
        a.eventIntersectionListener(eventIntersection);
    }
    
    /////////////////
    
    // Считает новые скорости по закону сохранения импульса и задает смещение компонентам (абсолютно упругие удары)
    private void changeVelocityAndD(Component a1, Component a2, Vector2D vn1, Vector2D vn2, Vector2D vt1, Vector2D vt2, double part){
        
        // Посчитаем новые скорости компонентов
        Vector2D v1 = new Vector2D(0, 0);
        Vector2D v2 = new Vector2D(0, 0);
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
            double m1 = a1.m, m2 = a2.m;
            v1 = (vn1.scalarMultiply(m1/m2-1).add(2, vn2)).scalarMultiply(m2/(1+m1)).add(vt1);
            v2 = (vn2.scalarMultiply(m2/m1-1).add(2, vn1)).scalarMultiply(m1/(1+m2)).add(vt2);
            
        }
        
        // Изменяем скорости
        Vector2D v10 = a1.velocity.scalarMultiply(1/n/frame);
        Vector2D v20 = a2.velocity.scalarMultiply(1/n/frame);
        a1.velocity = v1.scalarMultiply(n*frame);
        a2.velocity = v2.scalarMultiply(n*frame);
        
        // Запишем смещения
        a1.d = v10.scalarMultiply(part).add(1-part, v1);
        a2.d = v20.scalarMultiply(part).add(1-part, v2);
        a1.collision = true;
        a2.collision = true;

    }
    
    // Проверяет, по какой оси пересечение будет раньше. Возвращает часть пути до соударения по x и по y. Если пересечение возможно, но далеко, x = y = 2 
    private Vector2D calculatePartsXYBeforeIntersection(Component a1, Component a2){
    
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
        
        try{
            /////// По какой оси пересечение раньше? ////////////
            // Слева или справа
            if( location10.getX() <= location20.getX())
                partX = (w1/2 + w2/2 + location10.getX() - location20.getX())/(velocity20.getX() - velocity10.getX());
            else if( location10.getX() > location20.getX() )
                partX = (w1/2 + w2/2 + location20.getX() - location10.getX())/(velocity10.getX() - velocity20.getX());

            // Либо сверху или снизу
            if( location10.getY() <= location20.getY() )
                partY = (h1/2 + h2/2 + location10.getY() - location20.getY())/(velocity20.getY() - velocity10.getY());
            else if( location10.getY() > location20.getY() )
                partY = (h1/2 + h2/2 + location20.getY() - location10.getY())/(velocity10.getY() - velocity20.getY());
            }
        catch(Exception e){
            partX = partY = 1;
            e.printStackTrace();
        }
        
        // Обозначим возможные точки соударения
        Vector2D c2x = location20.add(partX, velocity20);
        Vector2D c1x = location10.add(partX, velocity10);

        Vector2D c2y = location20.add(partY, velocity20);
        Vector2D c1y = location10.add(partY, velocity10);

        // Там ли произошли пересечения?
        if (!((c2x.getY() + h2/2 >= c1x.getY() - h1/2) && (c2x.getY() - h2/2 <= c1x.getY() + h1/2))) partX = 2;
        if (!((c2y.getX() + w2/2 >= c1y.getX() - w1/2) && (c2y.getX() - w2/2 <= c1y.getX() + w1/2))) partY = 2;
        
        Vector2D parts = new Vector2D(partX, partY);
        return parts;
        
    }
    
    // Алгоритм изменения скоростей и записи событий соударения
    private void SquareRelationAlgoritm(Component a1, Component a2){
        
        Vector2D v10 = a1.velocity.scalarMultiply(1/n/frame);
        Vector2D v20 = a2.velocity.scalarMultiply(1/n/frame);

        Vector2D parts = calculatePartsXYBeforeIntersection(a1, a2);
        double partX = parts.getX();
        double partY = parts.getY();
        double part;

        // Пересечение слева или справа!
        if(partX < partY){
            part = partX;
            Vector2D vn1 = new Vector2D(v10.getX(), 0);
            Vector2D vn2 = new Vector2D(v20.getX(), 0);
            Vector2D vt1 = new Vector2D(0, v10.getY());
            Vector2D vt2 = new Vector2D(0, v20.getY());
            
            changeVelocityAndD(a1, a2, vn1, vn2, vt1, vt2, part);
        }

        // Пересечение сверху или снизу!
        else{ // if (partX >= partY){
            part = partY;
            Vector2D vn1 = new Vector2D(0, v10.getY());
            Vector2D vn2 = new Vector2D(0, v20.getY());
            Vector2D vt1 = new Vector2D(v10.getX(), 0);
            Vector2D vt2 = new Vector2D(v20.getX(), 0);
            
            changeVelocityAndD(a1, a2, vn1, vn2, vt1, vt2, part);
        }

        writeEventHits(a1, a2);
        
    }
    
    /////////////////
    
    // Проверка взаимодействия 2-х прямоугольников
    private void squareSquareRelation(Component a1, Component a2){
        
        double w1 = a1.size.getX(), w2 = a2.size.getX(), h1 = a1.size.getY(), h2 = a2.size.getY(); 
        Vector2D c10 = a1.location, c20 = a2.location;
        Vector2D v10 = a1.velocity.scalarMultiply(1/n/frame), v20 = a2.velocity.scalarMultiply(1/n/frame);

        // Определим, есть ли пересечение
        if(abs((c10.getX() + v10.getX()) - (c20.getX() + v20.getX())) < (w1/2 + w2/2) && abs((c10.getY() + v10.getY()) - (c20.getY() + v20.getY())) < (h1/2 + h2/2)){
            if(a1.enableRelation == EnableRelation.COLLISION && a2.enableRelation == EnableRelation.COLLISION)
                SquareRelationAlgoritm(a1, a2);
            writeEventIntersections(a1, a2); 
        }
        else{
            a1.d = v10;
            a2.d = v20;
        }
        
    }
    
    // Проверка взаимодействия прямоугольника с кругом
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

        // Определим, есть ли пересечение (здесь круг считается как квадрат стороной 2r)
        if(abs((Cc0.getX() + Vc0.getX()) - (Cr0.getX() + Vr0.getX())) < (r + w/2) && abs((Cc0.getY() + Vc0.getY()) - (Cr0.getY() + Vr0.getY())) < (r + h/2)){
            if(a1.enableRelation == EnableRelation.COLLISION && a2.enableRelation == EnableRelation.COLLISION)
                SquareRelationAlgoritm(a1, a2);
            writeEventIntersections(a1, a2); 
        }
        else{
            a1.d = a1.velocity.scalarMultiply(1/n/frame);
            a2.d = a2.velocity.scalarMultiply(1/n/frame);
        }
        
    }
    
    // Проверка взаимодействия 2-х кругов
    private void circleCircleRelation(Component a1, Component a2){
        
        // Столкновение 2 окружностей
        double dr = a1.r + a2.r;

        // Обозначим скорости
        Vector2D v10 = a1.velocity.scalarMultiply(1/n/frame);
        Vector2D v20 = a2.velocity.scalarMultiply(1/n/frame);

        // Определим, есть ли пересечение
        if (a1.location.add(v10).subtract(a2.location.add(v20)).getNorm() < dr){

            if(a1.enableRelation == EnableRelation.COLLISION && a2.enableRelation == EnableRelation.COLLISION){
                
                // Находим точку соударения(вернее, part - часть пути до соударения???)
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

                // 
                if(!coor1.equals(coor2)){
                    // Находим нормальные и тангенсальные составляющие скоростей
                    Vector2D vOs = coor2.subtract(coor1);
                    Vector2D vn1 = new Vector2D(0, 0);
                    Vector2D vn2 = new Vector2D(0, 0);
                    Vector2D vt1 = new Vector2D(0, 0);
                    Vector2D vt2 = new Vector2D(0, 0);

                    if (v10.getNorm() != 0){
                        double cos1 = v10.dotProduct(vOs)/( v10.getNorm()*vOs.getNorm() ); // Скалярное произведением векторов
                        vn1 = vOs.scalarMultiply(v10.getNorm() * cos1/vOs.getNorm());
                        vt1 = v10.subtract(vn1);
                    }

                    if(v20.getNorm() != 0){
                        double cos2 = v20.dotProduct(vOs)/(v20.getNorm() * vOs.getNorm());
                        vn2 = vOs.scalarMultiply(v20.getNorm() * cos2/vOs.getNorm());
                        vt2 = v20.subtract(vn2);
                    }
                
                    // Изменяем скорости
                    changeVelocityAndD(a1, a2, vn1, vn2, vt1, vt2, part);
                }
                
                // Если начальные координаты совпали, то пусть шары летят куда летели
                else{
                    a1.d = v10;
                    a2.d = v20;
                }
                writeEventHits(a1, a2);
                
            }
            
            writeEventIntersections(a1, a2);
            
        }
        else{
            a1.d = v10;
            a2.d = v20;
        }
        
    }
    
}
