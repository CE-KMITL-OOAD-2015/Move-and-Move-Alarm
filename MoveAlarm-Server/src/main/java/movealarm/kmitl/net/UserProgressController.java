package movealarm.kmitl.net;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@RestController
public class UserProgressController {

    private Converter converter = Converter.getInstance();
    private DatabaseInterface databaseInquirer = SQLInquirer.getInstance();

    @RequestMapping("/userProgress/createLog")
    public String createLog(@RequestParam(value = "JSON", required = true, defaultValue = "0")String JSON)
    {
        HashMap<String, Object> data = converter.JSONToHashMap(JSON);
        HashMap<String, Object> userData = converter.JSONToHashMap(converter.toString(data.get("user")));
        HashMap<String, Object> logData = converter.JSONToHashMap(converter.toString(data.get("activityLog")));

        User user = User.find(converter.toInt(userData.get("id")));
        if(user == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "User does not exist."));

        if(logData == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Server cannot receive log data."));

        UserActivityProgress progress = new UserActivityProgress();
        progress.setUser(user);
        progress.setDate((Date) logData.get("date"));
        progress.setNumberOfAccept(converter.toInt(logData.get("numberOfAccept")));
        progress.setNumberOfCancel(converter.toInt(logData.get("numberOfCancel")));
        progress.setCancelActivity(converter.toInt(logData.get("cancelActivity")));

        return converter.HashMapToJSON(progress.save());
    }

    @RequestMapping("/userProgress/getAllUserLogs")
    public String getAllLog(@RequestParam(value = "JSON", required = true, defaultValue = "0")String JSON)
    {
        HashMap<String, Object> data = converter.JSONToHashMap(JSON);

        User user = User.find(converter.toInt(data.get("id")));
        if(user == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "User does not exist."));

        ArrayList<HashMap<String, Object>> queryData;
        try {
            queryData = databaseInquirer.where("userActivity_progress", "userID", "=", converter.toString(user.getID()));
        } catch (Exception e) {
            e.printStackTrace();
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "An error has occurred while qurying data from the database."));
        }

        if(queryData == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "No result found."));

        HashMap<String, Object>[] logs = queryData.toArray(new HashMap[queryData.size()]);
        HashMap<String, Object> response = StatusDescription.createProcessStatus(true);
        response.put("logs", logs);

        return converter.HashMapToJSON(response);
    }

    @RequestMapping("/userProgress/getByDate")
    public String getByDate(@RequestParam(value = "JSON", required = true, defaultValue = "0")String JSON)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        HashMap<String, Object> data = converter.JSONToHashMap(JSON);
        HashMap<String, Object> userData = converter.JSONToHashMap(converter.toString(data.get("user")));

        String startDate = null;
        String endDate = null;
        try {
            startDate = sdf.format(sdf.parse(converter.toString(data.get("startDate"))));
            endDate = sdf.format(sdf.parse(converter.toString(data.get("endDate"))));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        User user = User.find(converter.toInt(userData.get("id")));
        if(user == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "User does not exist."));

        ArrayList<HashMap<String, Object>> queryData;
        try {
            queryData = databaseInquirer.where("userActivity_progress", "date", "BETWEEN", startDate + "' AND '" + endDate);
        } catch (Exception e) {
            e.printStackTrace();
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "An error has occurred while qurying data from the database."));
        }

        if(queryData == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "No result found."));

        HashMap<String, Object>[] logs = queryData.toArray(new HashMap[queryData.size()]);
        HashMap<String, Object> response = StatusDescription.createProcessStatus(true);
        response.put("logs", logs);

        return converter.HashMapToJSON(response);
    }
}
