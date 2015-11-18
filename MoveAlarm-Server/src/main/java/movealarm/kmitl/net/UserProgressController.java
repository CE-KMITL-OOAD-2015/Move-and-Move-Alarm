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

    @RequestMapping("/userProgress/getByUser")
    public String getByUser(@RequestParam(value = "JSON", required = true, defaultValue = "0")String JSON)
    {
        HashMap<String, Object> data = converter.JSONToHashMap(JSON);
        HashMap<String, Object> userData = converter.JSONToHashMap(converter.toString(data.get("user")));

        User user = User.find(converter.toInt(userData.get("id")));
        if(user == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "user does not exist."));

        UserActivityProgress progress = UserActivityProgress.findByUser(user);
        if(progress == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "No user activity progress."));

        HashMap<String, Object> response = StatusDescription.createProcessStatus(true);
        response.put("progress", progress.getGeneralValues());

        return converter.HashMapToJSON(response);
    }

    @RequestMapping("/userProgress/save")
    public String updateProgress(@RequestParam(value = "JSON", required = true, defaultValue = "0")String JSON)
    {
        HashMap<String, Object> data = converter.JSONToHashMap(JSON);
        HashMap<String, Object> userData = converter.JSONToHashMap(converter.toString(data.get("user")));
        HashMap<String, Object> progressData = converter.JSONToHashMap(converter.toString(data.get("activityProgress")));

        User user = User.find(converter.toInt(userData.get("id")));
        if(user == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "user does not exist."));

        UserActivityProgress progress = UserActivityProgress.findByUser(user);
        if(progress == null) {
            progress = new UserActivityProgress();
            progress.setUser(user);
        }

        progress.setNumberOfAccept(converter.toInt(progressData.get("numberOfAccept")));
        progress.setCancelActivity(converter.toInt(progressData.get("cancelActivity")));

        try {
            progress.setDate((Date) progressData.get("date"));
            progress.setNumberOfCancel(converter.toInt(progressData.get("numberOfCancel")));
        } catch (Exception e) {

        }

        return converter.HashMapToJSON(progress.save());
    }

}
