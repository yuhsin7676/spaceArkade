package spacearkade.config;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import spacearkade.DB.SQLConnection;
import spacearkade.game.GameLoop;

@Configuration
@ComponentScan("spaceArkade")
@PropertySource("application.properties")
public class AppConfig {
 
    @Bean
    public HeadlessApplication getApplication(GameLoop gameLoop){
        return new HeadlessApplication(gameLoop);
    }
    
    @Bean
    public SQLConnection getSQLConnection(){
        return new SQLConnection();
    }
    
    
    
}
