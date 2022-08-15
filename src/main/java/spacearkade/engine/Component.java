package spacearkade.engine;

import java.util.ArrayList;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public abstract class Component {
    
    protected String className = "Component";
    protected String typeName = "Component";
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
    protected boolean collision = false;
    protected boolean calculated = false;
    protected EnableRelation enableRelation = EnableRelation.COLLISION;
    protected boolean infinityMass = false;
    protected ArrayList<Event> eventIntersection;
    protected ArrayList<Event> eventHit;
    protected ArrayList<String> sounds = new ArrayList<String>();
    
    //////////////////////// Методы ////////////////////////

    public Component() {
    }
    
    //
    public final void removed(){
        canBeRemove = true;
    }
    
    public final void addSound(String strSound){
        this.sounds.add("strSound");
    }

    // Функции вывода
    public final Vector2D getLocation() {
        return location;
    }
    
    public final Vector2D getVelocity() {
        return this.velocity;
    }
    
    public final Vector2D getSize(){
        return size;
    }
    
    public final double getRadius(){
        return r;
    }
    
    public final int getId(){
        return id;
    }
    
    public boolean getIsInfinityMass() {
        return infinityMass;
    }

    // Функции ввода (возвращает данный объект, так как он использует паттерн строитель)
    public final Component setSize(Vector2D v2){
        this.size = v2;
        return this;
    }
    
    public final Component setSize(double x, double y){
        this.size = new Vector2D(x, y);
        return this;
    }
    
    public final Component setVelocity(Vector2D v2) {
        this.velocity = v2;
        return this;
    }
    
    public final Component setVelocity(double x, double y) {
        this.velocity = new Vector2D(x, y);
        return this;
    }
    
    public final Component setLocation(Vector2D location) {
        this.location = location;
        return this;
    }
    
    public final Component setLocation(double x, double y) {
        this.location = new Vector2D(x, y);
        return this;
    }
    
    public final Component setRadius(double r){
        if(r < 0.01) r = 0.01;
        this.r = r;
        this.size = new Vector2D(2*r, 2*r);
        return this;
    }
    
    public final Component setIsCircle(boolean isCircle) {
        this.isCircle = isCircle;
        return this;
    }

    public final Component setInfinityMass(boolean infinityMass) {
        this.infinityMass = infinityMass;
        return this;
    }
    
    // Действия
    public void eventHitListener(Event event){
    }
    
    public void eventIntersectionListener(Event event){
    }
    
    public void update(){
    }
    
}
