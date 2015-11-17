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
         sqlInquirer.startConnection(); //always re-start database connection when this method is called to make sure that service server can create new connection to database server
         if(sqlInquirer.isConnecting()) //check connection status
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(true, "connected.")); //respond inform of JSON

         return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Cannot connect to the database server."));
     }

     @RequestMapping("/test/passParameter")
     public String testPassParameter(@RequestParam(value="param", required = true, defaultValue = "Server cannot receive any value.") String param)
     {
         if(param.length() > 0 && !param.equals("Server cannot receive any value.")) { //if received parameter is not null and not equals to default value
             HashMap<String, Object> JSON = StatusDescription.createProcessStatus(true); //create response object
             JSON.put("param", param); //put the received parameter
             return converter.HashMapToJSON(JSON); //return inform of JSON
         }

         return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Server cannot receive any value."));
     }

     @RequestMapping("/test/queryingData")
     public String testQuerying() //test if an API server can queries data from the database server
     {
         ArrayList<HashMap<String, Object>> temp = null;
         try {
             temp = sqlInquirer.where("testTable", "var2", "=", "8988"); //query predefined data
         } catch (SQLException e) {
             e.printStackTrace();
         }

         if(temp != null) { //if data != null
             HashMap<String, Object> JSON = StatusDescription.createProcessStatus(true); //create response object
             JSON.put("data", temp); //put the data
             return converter.HashMapToJSON(JSON); //return inform of JSON
         }

         return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "An error has occurred while querying data from the database."));
     }
}
