package movealarm.kmitl.net;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

 @RestController
 public class ConnectionTesting {
     private SQLInquirer sqlInquirer = SQLInquirer.getInstance();
     private Converter converter = Converter.getInstance();

     @RequestMapping("/test/serverConnection")
     public String testServerConnection()
     {
         return converter.HashMapToJSON(StatusDescription.createProcessStatus(true, "connected."));
     }

     @RequestMapping("/test/databaseServerConnection")
     public String testDatabaseServerConnection()
     {
         sqlInquirer.startConnection();
         if(sqlInquirer.isConnecting())
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(true, "connected."));

         return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Cannot connect to the database server."));
     }

     @RequestMapping("/test/passParameter")
     public String testPassParameter(@RequestParam(value="param", required = true, defaultValue = "Server cannot receive any value.") String param)
     {
         if(param.length() > 0 && !param.equals("Server cannot receive any value.")) {
             HashMap<String, Object> JSON = StatusDescription.createProcessStatus(true);
             JSON.put("param", param);
             return converter.HashMapToJSON(JSON);
         }

         return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Server cannot receive any value."));
     }

     @RequestMapping("/test/queryingData")
     public String testQuerying()
     {
         ArrayList<HashMap<String, Object>> temp = null;
         try {
             temp = sqlInquirer.where("testTable", "var2", "=", "8988");
         } catch (SQLException e) {
             e.printStackTrace();
         }

         if(temp != null) {
             HashMap<String, Object> JSON = StatusDescription.createProcessStatus(true);
             JSON.put("data", temp);
             return converter.HashMapToJSON(JSON);
         }

         return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "An error has occurred while querying data from the database."));
     }
}
