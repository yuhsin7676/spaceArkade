package spacearkade.controllers;

import spacearkade.engine.World;
import spacearkade.Global;

//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import com.google.gson.Gson;
import java.util.Map;



@RestController
public class CoreControllers {
    
    @GetMapping("/test")
    public String test() {
        return "hello";
    }
    
    @GetMapping("/createWorld")
    public int createWorld(){
    
        int worldId = Global.createWorld();
        return worldId;
    
    }
    
    @GetMapping("/getWorld")
    public String getWorld(@RequestParam Map<String,String> params){
    
        int worldId = Integer.parseInt(params.get("id"));
        World world = Global.mapWorld.get(worldId);
        return new Gson().toJson(world);
    
    }
    
    
    
}
