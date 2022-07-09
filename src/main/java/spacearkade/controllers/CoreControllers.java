package spacearkade.controllers;

//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class CoreControllers {
    
    @GetMapping("/test")
    public String homePage() {
        return "hello";
    }
    
}
