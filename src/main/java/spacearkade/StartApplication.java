package spacearkade;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class StartApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }
    
}
