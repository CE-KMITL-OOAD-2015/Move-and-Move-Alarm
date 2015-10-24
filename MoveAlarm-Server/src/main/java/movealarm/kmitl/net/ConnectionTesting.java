package movealarm.kmitl.net;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.SQLException;
import java.util.HashMap;

 @RestController
 public class ConnectionTesting {
     private SQLInquirer sqlInquirer = SQLInquirer.getInstance();

     @RequestMapping("/test/server_connection")
     public String testConnection()
     {
         return "Connection success!";
     }

     @RequestMapping("/test/pass_parameter")
     public String testPassParameter(@RequestParam(value="param", required = true, defaultValue = "Server cannot recieve any value." +
             "Please check your sending process.") String param)
     {
         return "Your pass value: " + param;
     }

     @RequestMapping("/test/querying")
     public String testQuerying()
     {
         ArrayList<HashMap<String, Object>> temp = null;
         try {
             temp = sqlInquirer.where("testTable", "var2", "=", "text");
         } catch (SQLException e) {
             e.printStackTrace();
         }
         return "id: " + temp.get(0).get("id") + " value: " + temp.get(0).get("var2") + "<br>id: " +
                 temp.get(1).get("id") + " value: " + temp.get(1).get("var2");
     }

     @RequestMapping("/test/database_connection")
     public boolean testConDB() {
         return sqlInquirer.isConnecting();
     }

     @RequestMapping("/test/find")
     public String testFind(@RequestParam(value="id") int id,@RequestParam(value = "table") String table) throws SQLException {
         HashMap<String,Object> data = sqlInquirer.find(id,table);
         return data.toString();
     }
}
