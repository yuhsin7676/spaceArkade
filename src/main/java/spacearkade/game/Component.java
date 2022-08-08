package spacearkade.game;

import java.util.ArrayList;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public abstract class Component {
    
    protected String className;
    protected String typeName;
    protected int id;
    protected Vector2D d = new Vector2D(0, 0);
    protected Vector2D size = new Vector2D(20, 20);
    protected double r = 10;
    protected double m = 1;
    protected Vector2D location = new Vector2D(0, 0);
    protected Vector2D velocity = new Vector2D(0., 0.);
    protected boolean isCircle = true;// eсли false, то объект - прямоугольный
    
    protected boolean canBeRemove = false; //Если true, то в следующем update компонент будет удален
    protected boolean isStaticComponent = false;
    
    /* Переменные, используемые в рассчете соударений\
    Переменная collision обозначает, было ли проверено соударение актора с другим объектом\
    Рассчет коллизий данного актора идет ровно до первого обнаружения коллизии, после чего\
    переменная collision становится true, а акторы с таким значением collision не участвуют\
    в обнаружении коллизий. Для статических компонентов мира не используется, так как они участвуют во\
    всех обнаружениях коллизий динамических компонентов.*/
    protected boolean collision = false; // используется только для акторов
    protected boolean enableCollision = true; // Для виджетов всегда false
    protected boolean infinityMass = false; // Правда для статических компонентов

    // Массив данных соударения
    protected ArrayList<EventHit> eventHit;
    
    //////////////////////// Методы ////////////////////////

    public Component() {
    }

    // Функции вывода
    public Vector2D getLocation() {
        return location;
    }
    
    public Vector2D getVelocity() {
        return this.velocity;
    }
    
    public Vector2D getSize(){
        return size;
    }
    
    public double getRadius(){
        return r;
    }
    
    public int getId(){
        return id;
    }
    
    public boolean getIsInfinityMass() {
        return infinityMass;
    }

    // Функции ввода (возвращает данный объект, так как он использует паттерн строитель)
    public Component setSize(Vector2D v2){
        this.size = v2;
        return this;
    }
    
    public Component setSize(double x, double y){
        this.size = new Vector2D(x, y);
        return this;
    }
    
    public Component setVelocity(Vector2D v2) {
        this.velocity = v2;
        return this;
    }
    
    public Component setVelocity(double x, double y) {
        this.velocity = new Vector2D(x, y);
        return this;
    }
    
    public Component setLocation(Vector2D location) {
        this.location = location;
        return this;
    }
    
    public Component setLocation(double x, double y) {
        this.location = new Vector2D(x, y);
        return this;
    }
    
    public Component setRadius(double r){
        if(r < 0.01) r = 0.01;
        this.r = r;
        this.size = new Vector2D(2*r, 2*r);
        return this;
    }
    
    public Component setIsCircle(boolean isCircle) {
        this.isCircle = isCircle;
        return this;
    }

    public Component setInfinityMass(boolean infinityMass) {
        this.infinityMass = infinityMass;
        return this;
    }
    
    // Действия при соударении
    public void eventHitListener(){
    }
    
}
