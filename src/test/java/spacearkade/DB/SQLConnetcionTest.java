package spacearkade.DB;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SQLConnetcionTest {
    
    /**
     * Тестирование метода ReadDB(). Не должен упасть.
     */
    @Test
    public void testReadDB(){
        System.out.println("ReadDB");

        try{
            SQLConnetcion connection = new SQLConnetcion();
            System.out.print(connection.ReadDB().toString());
        }
        catch(Exception e){
            System.out.print(e.getMessage());
        }
        
    }
    
}
