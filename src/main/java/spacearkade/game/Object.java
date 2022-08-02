package spacearkade.game;

import com.badlogic.gdx.graphics.Texture;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public abstract class Object {
    
    public String className;
    public String typeName;
    public int id;
    public Vector2D d = new Vector2D(0, 0);
    public Vector2D size = new Vector2D(10, 10);
    public double r = 10;
    public double m = 1;
    public Vector2D location = new Vector2D(0, 0);
    public Vector2D velocity = new Vector2D(0., 0.);
    public boolean isCircle = true;// сли false, то объект - прямоугольный

    // Цвет
    //sf::Color background = sf::Color(255,255,128);

    // Текстура
    //sf::Texture *texture = new sf::Texture();

    // Тень
    //sf::Color shadowColor = sf::Color(0,0,0);
    //double shadowBlur = 0;
    //sf::Vector2f shadowOffset = {0,0};
    //bool haveShadow = false;

    // Порядок прорисовки (более высокие перекрываются более низкими) - пока не используется
    //int layer = 0;

    //public Object(){
        //texture = &ye::globalTexture;
    //}
    
    /* Переменные, используемые в рассчете соударений\
    Переменная collision обозначает, было ли проверено соударение актора с другим объектом\
    Рассчет коллизий данного актора идет ровно до первого обнаружения коллизии, после чего\
    переменная collision становится true, а акторы с таким значением collision не участвуют\
    в обнаружении коллизий. Для компонентов мира не используется, так как они участвуют во\
    всех обнаружениях коллизий акторов. Для виджетов не работает ввиду того, что они не являются\
    физическими объектами*/
    public boolean collision = false; // используется только для акторов
    public boolean enableCollision = true; // Для виджетов всегда false
    public boolean infinityMass = false; // Правда для компонентов

    // Массив данных соударения
    public ArrayList<EventHit> eventHit;
    
    //////////////////////// Методы ////////////////////////

    public Object(Vector2D location, int id) {
        this.location = location;
    }

    public Vector2D getLocation() {
        return location;
    }

    public void setLocation(Vector2D location) {
        this.location = location;
    }

    // Функции вывода
    public Vector2D getSize(){
        return size;
    }
    double getRadius(){
        return r;
    }
    int getId(){
        return id;
    }

    // Функции ввода
    void setSize(Vector2D v2){
        size = v2;
    }
    void setRadius(double r_vvod){
        if(r_vvod < 0.01) r_vvod = 0.01;
        r = r_vvod;
    }  
    
}
