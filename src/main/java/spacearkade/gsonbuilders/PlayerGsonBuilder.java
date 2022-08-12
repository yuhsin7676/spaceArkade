package spacearkade.gsonbuilders;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PlayerGsonBuilder {
    
    public static Gson get(){
        
        Gson gson = new GsonBuilder()
            .setExclusionStrategies(new ExclusionStrategy() {

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }

                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    
                    String fieldName = f.getName();
                    return (fieldName.equals("player1") ||
                            fieldName.equals("player2") ||
                            fieldName.equals("arkadeWorld") ||
                            fieldName.equals("addQueue") ||
                            fieldName.equals("balls"));
                }

             })
            .create();
        
        return gson;
        
    }
    
}
