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

     @RequestMapping("/test/update_data")
     public void testUpdate()
     {
         try {
             sqlInquirer.update("testTable", "var2='5555'", "id", ">", "5");
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }

     @RequestMapping("/test/insert_data")
     public void testInsert()
     {
         try {
             sqlInquirer.insert("testTable", "var2", "6666");
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }

     @RequestMapping("/test/delete_data")
     public void testDelete()
     {
         try {
             sqlInquirer.delete("testTable", "var2='6666' OR var2='5555'");
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }

     @RequestMapping("/test/insert_multiple")
     public void testInsertMultiple()
     {
         String[] values = {"6666", "4444", "8988"};
         try {
             sqlInquirer.insertMultiple("testTable", "var2", values);
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }

     @RequestMapping("/test/database_connection")
     public boolean testConDB() {
         return sqlInquirer.isConnecting();
     }
}
