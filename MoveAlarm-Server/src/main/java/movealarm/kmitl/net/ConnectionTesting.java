package movealarm.kmitl.net;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by Moobi on 15-Oct-15.
 */
 @RestController
 public class ConnectionTesting {
     private SQLInquirer sqlInquirer;

     @RequestMapping("/testConnection")
     public String testConnection()
     {
         return "Connection success!";
     }

     @RequestMapping("/testPassParameter")
     public String testPassParameter(@RequestParam(value="param", required = true, defaultValue = "Server cannot recieve any value." +
             "Please check your sending process.") String param)
     {
         return "Your pass value: " + param;
     }

     @RequestMapping("/testQuerying")
     public String testQuerying()
     {
         sqlInquirer = new SQLInquirer();
         ArrayList<HashMap<String, Object>> temp = null;
         try {
             temp = sqlInquirer.where("testTable", "var2", "=", "text");
         } catch (SQLException e) {
             e.printStackTrace();
         }
         return "id: " + temp.get(0).get("id") + " value: " + temp.get(0).get("var2") + "<br>id: " +
                 temp.get(1).get("id") + " value: " + temp.get(1).get("var2");
         //return temp.get(0).toString();
     }

     @RequestMapping("connectToDB")
     public boolean testConDB() {
         SQLInquirer sql = new SQLInquirer();
         sql.startConnection();
         return sql.isConnecting();
     }

     @RequestMapping("/find")
     public String testFind(@RequestParam(value="id") int id,@RequestParam(value = "table") String table) throws SQLException {
         SQLInquirer sql = new SQLInquirer();
         sql.startConnection();
         HashMap<String,Object> data = sql.find(id,table);
         return data.toString();
     }

     @RequestMapping("/save")
     public String testSave(@RequestParam(value = "json")String json,@RequestParam(value = "table")String table) throws SQLException {
         SQLInquirer sql = new SQLInquirer();
         sql.startConnection();
         sql.save(json,table);
         return "save complete!";
     }
}
