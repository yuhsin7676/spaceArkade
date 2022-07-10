package spacearkade.game;

import java.util.Map;
import java.util.HashMap;

public class World {
    
    private int width;
    private int height;
    private int primary = 0;
    private Map<Integer, Object> Objects = new HashMap<Integer, Object>();

    public World(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public int createNewObject(Object object){
        Objects.put(this.primary, object);
        object.setId(this.primary);
        int worldId = this.primary;
        this.primary++;
        return worldId;
    }
    
    public int deleteObject(int id){
        Objects.remove(id);
        return primary;
    }
    
}
