package spacearkade.DB;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spacearkade.context.ApplicationContextHolder;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("local")
@TestPropertySource(locations = "classpath:application-local.properties")
public class SQLConnetcionTest {
    
    /**
     * Тестирование метода ReadDB(). Не должен упасть.
     */
    @Test
    public void testReadDB(){
        System.out.println("ReadDB");

        try{
            SQLConnection connection = ApplicationContextHolder.getApplicationContext().getBean("getSQLConnection",SQLConnection.class);
            System.out.print(connection.ReadDB().toString());
        }
        catch(Exception e){
            System.out.print(e.getMessage());
        }
        
    }
    
}
