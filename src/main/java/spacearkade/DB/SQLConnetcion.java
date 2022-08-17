package spacearkade.DB;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

public class SQLConnetcion {
    
    // Данные для подключения к БД (Берем из application.properties)
    //@Value("${db.url}")
    private String URL = "jdbc:sqlite:/home/ilya/spaceArkade.s3db";
    
    //@Value("${db.dbName}") надо придумать, как сделать их через аннотации
    private String DATA_BASE_NAME;
    
    //@Value("${db.username}")
    private String USER_NAME;
    
    //@Value("${db.password}")
    private String PASSWORD;
    
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    // Вывод таблицы
    public String[][][] ReadDB() throws ClassNotFoundException, SQLException{

        this.connection = DriverManager.getConnection(this.URL);
        this.statement = this.connection.createStatement();
        this.resultSet = this.statement.executeQuery("select * from begincomponents");
        
        Type type = new TypeToken<String[][]>(){}.getType();
        ArrayList<String[][]> arrayListBeginComponents = new ArrayList<String[][]>();
        while(resultSet.next()){
            String components = resultSet.getString("components");
            String[][] beginComponents = new Gson().fromJson(components, type);
            arrayListBeginComponents.add(beginComponents);
        }	
        
        connection.close();
        statement.close();
        resultSet.close();
        
        String[][][] arrayBeginComponents = new String[arrayListBeginComponents.size()][20][10];
        for(int i = 0; i < arrayListBeginComponents.size(); i++)
            arrayBeginComponents[i] = arrayListBeginComponents.get(i);
        
        return arrayBeginComponents;
    }
}
